package payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import payment.dto.PaymentRequest;
import payment.entity.Payment;
import payment.entity.PaymentStatus;
import payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate; // Bean으로 주입받음

    /**
     * 결제를 처리합니다.
     * @param paymentRequest 결제 요청 정보
     * @return 생성된 결제 정보
     */
    public Payment processPayment(PaymentRequest paymentRequest) {
        // 1. 결제 객체 생성
        Payment payment = new Payment(
                paymentRequest.getOrderId(),
                PaymentStatus.PENDING,
                paymentRequest.getAmount(),
                LocalDateTime.now()
        );

        Payment savedPayment = paymentRepository.save(payment);

        // 2. 결제 성공 시 로직
        if (mockPaymentSuccess()) {
            savedPayment.setStatus(PaymentStatus.SUCCESS);
            notifyOrderService(savedPayment.getOrderId(), "PAID");
        } else {
            savedPayment.setStatus(PaymentStatus.FAILED);
            notifyOrderService(savedPayment.getOrderId(), "FAILED");
        }

        return paymentRepository.save(savedPayment);
    }

    /**
     * 결제 상태를 조회합니다.
     * @param id 결제 ID
     * @return 결제 정보
     */
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + id));
    }

    /**
     * 주문 서비스에 결제 상태를 알립니다.
     * @param orderId 주문 ID
     * @param status 결제 상태 (PAID, FAILED 등)
     */
    private void notifyOrderService(Long orderId, String status) {
        String orderServiceUrl = "http://api-gateway/orders/" + orderId + "/payment-status";

        try {
            restTemplate.postForEntity(orderServiceUrl, status, Void.class);
        } catch (HttpStatusCodeException e) {
            // 결제 상태 전달 실패 시 로그 처리
            System.err.println("Failed to notify order-service: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to notify order-service");
        }
    }

    /**
     * 결제 성공 여부를 시뮬레이션합니다.
     * @return 결제 성공 여부 (true: 성공, false: 실패)
     */
    private boolean mockPaymentSuccess() {
        return Math.random() > 0.2; // 80% 성공률
    }
}
