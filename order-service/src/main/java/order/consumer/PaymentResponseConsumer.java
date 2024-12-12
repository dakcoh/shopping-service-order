package order.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import order.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shared.response.PaymentResponse;

@Component
public class PaymentResponseConsumer {
    private final OrderService orderService; // 주문 상태 업데이트를 위한 서비스

    public PaymentResponseConsumer(OrderService orderService) {
        this.orderService = orderService;
    }
    @KafkaListener(topics = "${order.kafka.topic.payment-response}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePaymentResponse(String message) {
        System.out.println("Received payment response: " + message);

        try {
            // JSON 메시지를 DTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            PaymentResponse paymentResponse = objectMapper.readValue(message, PaymentResponse.class);

            // 결제 상태 처리
            if ("COMPLETED".equals(paymentResponse.getPaymentStatus())) {
                orderService.updateOrderStatusToPaid(paymentResponse.getOrderId());
            } else if ("FAILED".equals(paymentResponse.getPaymentStatus())) {
                orderService.updateOrderStatusToCancelled(paymentResponse.getOrderId(), paymentResponse.getFailureReason());
            } else {
                System.err.println("Unknown payment status: " + paymentResponse.getPaymentStatus());
            }
        } catch (Exception e) {
            System.err.println("Error processing payment response: " + e.getMessage());
        }
    }
}
