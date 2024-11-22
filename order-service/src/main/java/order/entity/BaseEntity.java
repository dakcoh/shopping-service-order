
package order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 모든 테이블에 대한 공통 필드를 포함하는 상속 Entity
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    /**
     * 사용 구분을 반영
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    /**
     * 처음 생성한 시간을 반영
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 마지막 업데이트 시간을 반영
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
