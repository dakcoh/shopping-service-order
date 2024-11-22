package order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import order.entity.Order;
import order.entity.OrderStatus;
import order.entity.OrderStatusHistory;
import order.repository.OrderStatusHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryService {

    private final OrderStatusHistoryRepository repository;

    /**
     * 주문 상태 이력을 생성합니다.
     * @param orderId 주문 ID
     * @param customerId 고객 ID
     * @param status 주문 상태
     */
    @Transactional
    public void create(Long orderId, Long customerId, OrderStatus status) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrderId(orderId);
        history.setOrderDate(LocalDate.now().atStartOfDay());
        history.setCustomerId(customerId);
        history.setStatus(status);

        repository.save(history);
    }

    /**
     * 특정 주문 ID에 대한 모든 상태 이력을 조회합니다.
     * @param orderId 주문 ID
     * @return 주문 상태 이력 리스트
     */
    @Transactional
    public List<OrderStatusHistory> searchByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    /**
     * 특정 고객 ID와 주문 상태로 주문 이력을 조회합니다.
     * @param customerId 고객 ID
     * @param status 주문 상태
     * @return 주문 상태 이력 리스트
     */
    @Transactional
    public List<OrderStatusHistory> searchByCustomerIdAndStatus(Long customerId, String status) {
        return repository.findByCustomerIdAndStatus(customerId, status);
    }
}