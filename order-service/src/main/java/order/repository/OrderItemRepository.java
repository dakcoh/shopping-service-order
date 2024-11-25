package order.repository;

import order.entity.OrderDetail;
import order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderDetail, Long> {

    // OrderDetail에서 order 상태를 기준으로 검색
    @Query("SELECT od FROM OrderDetail od WHERE od.orders.status = :status")
    List<OrderDetail> findByOrder_Status(@Param("status") OrderStatus status);
}