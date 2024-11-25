package order.repository;

import order.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {

    // 특정 주문 ID에 대한 상태 이력 조회
    List<OrderStatusHistory> findByOrders_Id(Long orderId);

    // 특정 고객 ID와 상태로 상태 이력 조회
    List<OrderStatusHistory> findByCustomerIdAndStatus(Long customerId, String status);
}