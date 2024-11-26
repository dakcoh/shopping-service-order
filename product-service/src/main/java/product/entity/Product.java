package product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product 엔티티 클래스
 * 데이터베이스의 "product" 테이블과 매핑됩니다.
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "storage_price", nullable = false)
    private BigDecimal storagePrice; // 입고 금액.

    @Column(name = "storage_date", nullable = false)
    private LocalDateTime storageDate; // 입고일.
}
