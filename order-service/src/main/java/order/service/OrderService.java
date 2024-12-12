package order.service;

import jakarta.transaction.Transactional;
import order.producer.PaymentRequestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import shared.request.OrderItemRequest;
import shared.request.OrderRequest;
import order.dto.OrderResponse;
import order.entity.OrderDetail;
import order.entity.Orders;
import order.entity.OrderStatus;
import order.event.OrderCreatedEvent;
import order.event.OrderStatusUpdatedEvent;
import order.event.OrderCancelledEvent;
import order.exception.OrderNotFoundException;
import order.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static order.entity.OrderStatus.PENDING;

/**
 * OrderService 클래스는 주문에 대한 비즈니스 로직을 처리합니다.
 * 모든 주문 관련 작업은 이 클래스에서 구현됩니다.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository; // 주문 데이터를 관리하는 Repository
    private final OrderStatusHistoryService orderStatusHistoryService; // 주문 상태 이력을 관리하는 서비스
    private final PaymentRequestProducer paymentRequestProducer; // Kafka를 통해 결제 요청을 전송하는 Producer
    private final ApplicationEventPublisher eventPublisher; // 이벤트 발행기


    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderStatusHistoryService orderStatusHistoryService,
                        PaymentRequestProducer paymentRequestProducer,
                        ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.orderStatusHistoryService = orderStatusHistoryService;
        this.paymentRequestProducer = paymentRequestProducer;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 모든 주문을 조회합니다.
     *
     * @return 주문 목록을 OrderResponse 형태로 반환합니다.
     */
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderResponse) // 각 주문을 DTO로 변환
                .collect(Collectors.toList());
    }

    /**
     * 특정 주문을 ID로 조회합니다.
     *
     * @param id 주문의 고유 ID
     * @return 주문의 상세 정보를 OrderResponse 형태로 반환합니다.
     */
    public OrderResponse getOrderById(Long id) {
        Optional<Orders> order = orderRepository.findById(id); // 주문 ID로 조회
        return order.map(this::convertToOrderResponse)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id)); // 주문이 없으면 예외 발생
    }

    /**
     * 새로운 주문을 생성하고, Kafka를 통해 결제 요청을 전송합니다.
     *
     * @param orderRequest 주문 요청 객체
     * @return 생성된 주문의 상세 정보를 OrderResponse 형태로 반환합니다.
     */
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // 1. 유효성 검사
        if (orderRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be null or empty");
        }

        // 2. 주문 생성
        Orders order = new Orders();
        order.setOrderDate(LocalDate.now().atStartOfDay()); // 주문 날짜 설정
        order.setCustomerId(orderRequest.getCustomerId()); // 고객 ID 설정
        order.setStatus(PENDING); // 주문 초기 상태 설정

        int totalQuantity = 0; // 총 수량
        BigDecimal totalAmount = BigDecimal.ZERO; // 총 금액

        // 주문 항목을 추가
        for (OrderItemRequest item : orderRequest.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setProductOptionId(item.getProductOptionId());
            detail.setQuantity(item.getQuantity());
            detail.setAmount(item.getAmount());

            order.addOrderDetail(detail);

            totalQuantity += item.getQuantity();
            totalAmount = totalAmount.add(item.getAmount());
        }

        order.setTotalQuantity(totalQuantity);
        order.setTotalAmount(totalAmount);

        Orders savedOrder = orderRepository.save(order); // 주문 저장
        // 3. 주문 상태 이력 기록
        orderStatusHistoryService.create(savedOrder.getId(), savedOrder.getCustomerId(), savedOrder.getStatus());

        // 4. Kafka를 통해 결제 요청 전송
        sendPaymentRequestToKafka(savedOrder);

        // 주문 생성 이벤트 발행
        eventPublisher.publishEvent(new OrderCreatedEvent(savedOrder.getId(), savedOrder.getCustomerId()));

        return convertToOrderResponse(savedOrder); // 생성된 주문 정보를 반환
    }

    /**
     * 주문 상태를 업데이트하고 상태 이력을 기록합니다.
     *
     * @param orderId 주문의 고유 ID
     * @param status 변경할 주문 상태
     * @return 업데이트된 주문의 상세 정보를 OrderResponse 형태로 반환합니다.
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        // 주문 조회
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        // 주문 상태 업데이트
        order.setStatus(status);
        Orders updatedOrder = orderRepository.save(order); // 업데이트된 주문 저장

        // 상태 이력 기록
        orderStatusHistoryService.create(updatedOrder.getId(), updatedOrder.getCustomerId(), updatedOrder.getStatus());

        // 주문 상태 업데이트 이벤트 발행
        eventPublisher.publishEvent(new OrderStatusUpdatedEvent(updatedOrder.getId(), status));

        return convertToOrderResponse(updatedOrder);
    }

    /**
     * 주문 상태를 'PAID'로 업데이트합니다.
     *
     * @param orderId 주문의 고유 ID
     * @throws OrderNotFoundException 주문이 존재하지 않을 경우 발생
     */
    @Transactional
    public void updateOrderStatusToPaid(String orderId) {
        Orders order = orderRepository.findById(Long.valueOf(orderId))
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        // 주문 상태를 PAID로 변경
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        // 주문 상태 이력 기록
        orderStatusHistoryService.create(order.getId(), order.getCustomerId(), order.getStatus());

        // 주문 상태 업데이트 이벤트 발행
        eventPublisher.publishEvent(new OrderStatusUpdatedEvent(order.getId(), OrderStatus.PAID));

        System.out.println("Order status updated to PAID for orderId: " + orderId);
    }

    /**
     * 주문 상태를 'CANCELLED'로 업데이트하고 실패 사유를 기록합니다.
     *
     * @param orderId 주문의 고유 ID
     * @param failureReason 결제 실패 사유
     * @throws OrderNotFoundException 주문이 존재하지 않을 경우 발생
     */
    @Transactional
    public void updateOrderStatusToCancelled(String orderId, String failureReason) {
        Orders order = orderRepository.findById(Long.valueOf(orderId))
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        // 주문 상태를 CANCELLED로 변경
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // 주문 상태 이력 기록
        orderStatusHistoryService.create(order.getId(), order.getCustomerId(), order.getStatus());

        // 주문 상태 업데이트 이벤트 발행
        eventPublisher.publishEvent(new OrderCancelledEvent(order.getId(), failureReason));

        System.out.println("Order status updated to CANCELLED for orderId: " + orderId + ". Reason: " + failureReason);
    }

    /**
     * Kafka를 통해 결제 요청 메시지를 전송합니다.
     *
     * @param order 주문 객체
     */
    private void sendPaymentRequestToKafka(Orders order) {
        // Kafka를 통해 결제 요청 메시지 전송
        paymentRequestProducer.sendPaymentRequest(
                String.valueOf(order.getId()), // 주문 ID
                String.format("Order %d payment of %s", order.getId(), order.getTotalAmount()) // 결제 정보
        );
    }

    /**
     * Order 엔티티를 OrderResponse DTO로 변환합니다.
     *
     * @param order 변환할 Order 엔티티
     * @return 변환된 OrderResponse DTO
     */
    private OrderResponse convertToOrderResponse(Orders order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStatus(order.getStatus());
        return response;
    }
}