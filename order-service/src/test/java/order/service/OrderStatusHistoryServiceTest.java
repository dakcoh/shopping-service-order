package order.service;

import static org.mockito.Mockito.*;

import order.entity.OrderStatus;
import order.entity.Orders;
import order.repository.OrderRepository;
import order.repository.OrderStatusHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Mockito 테스트 환경 구성
public class OrderStatusHistoryServiceTest {

    @InjectMocks
    private OrderStatusHistoryService service; // 테스트 대상 클래스

    @Mock
    private OrderRepository orderRepository; // Mock Repository

    @Mock
    private OrderStatusHistoryRepository historyRepository; // Mock 상태 이력 Repository

    private Orders mockOrder;

    @BeforeEach
    public void setup() {
        // Mock 주문 객체 초기화
        mockOrder = new Orders();
        mockOrder.setId(1L);
        mockOrder.setCustomerId("dakcoh");
    }

    @Test
    public void testCreateOrderStatusHistory_Success() {
        // Given: Mock 동작 설정
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        // When: create 호출
        service.create(mockOrder.getId(), mockOrder.getCustomerId(), OrderStatus.PENDING);

        // Then: 저장 동작 검증
        verify(orderRepository, times(1)).findById(mockOrder.getId());
        verify(historyRepository, times(1)).save(any());
    }
}
