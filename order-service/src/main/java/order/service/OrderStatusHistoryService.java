package order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import order.entity.Order;
import order.entity.OrderStatus;
import order.entity.OrderStatusHistory;
import order.repository.OrderRepository;
import order.repository.OrderStatusHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryService {

    private final OrderRepository orderRepository; // 주문 정보를 조회하기 위한 OrderRepository
    private final OrderStatusHistoryRepository historyRepository; // 주문 상태 이력을 저장하기 위한 OrderStatusHistoryRepository


    /**
     * 주문 상태 이력을 생성합니다.
     * @param orderId 주문 ID
     * @param customerId 고객 ID
     * @param status 주문 상태
     */
    @Transactional
    public void create(Long orderId, Long customerId, OrderStatus status) {
        // Order 객체 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setOrderDate(LocalDate.now().atStartOfDay());
        history.setCustomerId(customerId);
        history.setStatus(status);

        historyRepository.save(history);
    }

    /**
     * 특정 주문 ID에 대한 모든 상태 이력을 조회합니다.
     * @param orderId 주문 ID
     * @return 주문 상태 이력 리스트
     */
    @Transactional
    public List<OrderStatusHistory> searchByOrderId(Long orderId) {
        return historyRepository.findByOrderId(orderId);
    }

    /**
     * 특정 고객 ID와 주문 상태로 주문 이력을 조회합니다.
     * @param customerId 고객 ID
     * @param status 주문 상태
     * @return 주문 상태 이력 리스트
     */
    @Transactional
    public List<OrderStatusHistory> searchByCustomerIdAndStatus(Long customerId, String status) {
        return historyRepository.findByCustomerIdAndStatus(customerId, status);
    }
}