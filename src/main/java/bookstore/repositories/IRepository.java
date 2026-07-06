package bookstore.repositories;

import java.util.List;

public interface IRepository<T> {
    T save(T entity);
    T findById(Long id);
    T findByProductId(String productId);
    List<T> findAll();
    void delete(Long id);
    long count();
    int deleteAll();
    String getDataSourceType();
    void close();
}