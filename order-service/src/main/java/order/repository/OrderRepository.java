package order.repository;

import order.entity.OrderStatusHistory;
import order.entity.Orders;
import order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByStatus(OrderStatus status);
}