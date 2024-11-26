package product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Category 엔티티 클래스
 * 데이터베이스의 "category" 테이블과 매핑됩니다.
 */
@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name; // 카테고리 이름.

    @Column(name = "description", length = 255)
    private String description; // 카테고리 설명.

    @Column(name = "parent_id")
    private Long parentId; // 부모 카테고리 ID (계층 구조).

    @Column(name = "level")
    private Integer level; // 카테고리 계층 레벨.
}
