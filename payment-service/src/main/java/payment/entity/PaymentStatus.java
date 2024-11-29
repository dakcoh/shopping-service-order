package payment.entity;

public enum PaymentStatus {
    PENDING,  // 결제 요청 대기
    SUCCESS,  // 결제 성공
    FAILED    // 결제 실패
}
