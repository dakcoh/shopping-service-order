package order.repository;

import order.entity.OrderDetail;
import order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByStatus(OrderStatus status);
}