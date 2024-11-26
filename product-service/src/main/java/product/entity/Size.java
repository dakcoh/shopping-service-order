package product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Size 엔티티 클래스
 * 데이터베이스의 "size" 테이블과 매핑됩니다.
 */
@Entity
@Table(name = "size")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name; // 사이즈 이름.

    @Column(name = "description", length = 50)
    private String description; // 사이즈 설명.
}
