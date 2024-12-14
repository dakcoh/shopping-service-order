package payment.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import shared.request.PaymentRequest;

@Component
public class PaymentRequestProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName;

    @Autowired
    public PaymentRequestProducer(KafkaTemplate<String, String> kafkaTemplate,
                                  @Value("${order.kafka.topic.payment-request}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendPaymentRequest(PaymentRequest paymentRequest) {
        String message = paymentRequest.toJson(); // JSON 직렬화
        kafkaTemplate.send(topicName, String.valueOf(paymentRequest.getOrderId()), message);
    }
}