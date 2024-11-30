package order.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName;

    public PaymentRequestProducer(KafkaTemplate<String, String> kafkaTemplate,
                                  @Value("${order.kafka.topic.payment-request}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendPaymentRequest(String orderId, String paymentDetails) {
        String message = String.format("{\"orderId\": \"%s\", \"details\": \"%s\"}", orderId, paymentDetails);
        kafkaTemplate.send(topicName, orderId, message);
    }
}
