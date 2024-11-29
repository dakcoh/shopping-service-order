package payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payment.dto.PaymentRequest;
import payment.entity.Payment;
import payment.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 결제 요청 처리
     * @param paymentRequest 결제 요청 정보
     * @return 생성된 결제 정보
     */
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequest paymentRequest) {
        Payment payment = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(payment);
    }

    /**
     * 결제 상태 조회
     * @param id 결제 ID
     * @return 결제 상태 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentStatus(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
}
