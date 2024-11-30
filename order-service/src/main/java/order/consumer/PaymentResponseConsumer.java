package order.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentResponseConsumer {

    @KafkaListener(topics = "${order.kafka.topic.payment-response}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePaymentResponse(String message) {
        System.out.println("Received payment response: " + message);

        // 결제 상태 처리 로직 추가
    }
}
