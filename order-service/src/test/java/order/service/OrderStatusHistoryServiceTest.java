package order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import order.entity.OrderStatus;
import order.entity.OrderStatusHistory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@RequiredArgsConstructor
public class OrderStatusHistoryServiceTest {

    private final OrderStatusHistoryService service;

    @Test
    @Transactional
    public void testCreateAndSearch() {
        // 1. 상태 이력 생성
        service.create(1L, 1001L, OrderStatus.PENDING);

        // 2. 주문 ID로 검색
        List<OrderStatusHistory> histories = service.searchByOrderId(1L);
        //assertEquals("2", histories.size());

        // 3. 고객 ID와 상태로 검색
        List<OrderStatusHistory> confirmedHistories = service.searchByCustomerIdAndStatus(1001L, "CONFIRMED");
        //assertEquals("1", confirmedHistories.size());
    }
}