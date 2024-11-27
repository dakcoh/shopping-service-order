package product.service;

import java.util.List;

public interface BaseService<T, ID> {
    T create(T entity); // 생성.
    T getById(ID id); // 단일 조회.
    List<T> getAll(); // 전체 조회.
    T update(ID id, T entity); // 수정.
    void delete(ID id); // 삭제.
}
