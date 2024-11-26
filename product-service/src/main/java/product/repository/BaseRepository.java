package .productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    Optional<T> findById(ID id); // 기본 ID로 조회.
}
