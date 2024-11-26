package product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Color 엔티티 클래스
 * 데이터베이스의 "color" 테이블과 매핑됩니다.
 */
@Entity
@Table(name = "color")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name; // 색상 이름.

    @Column(name = "rgb_code", length = 20)
    private String rgbCode; // RGB 코드.
}
