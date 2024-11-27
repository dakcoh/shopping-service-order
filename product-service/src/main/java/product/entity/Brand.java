package product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Brand 엔티티 클래스
 * 데이터베이스의 "brand" 테이블과 매핑됩니다.
 */
@Entity
@Table(name = "brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 브랜드 ID (Primary Key).

    @Column(name = "name", nullable = false, length = 100)
    private String name; // 브랜드 이름.

    @Column(name = "description", length = 255)
    private String description; // 브랜드 설명.
}
