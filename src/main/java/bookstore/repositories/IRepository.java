package bookstore.repositories;

import java.util.List;

public interface IRepository<T> {
    T save(T entity);
    T findById(Long id);
    List<T> findAll();
    void delete(Long id);
    long count();
    int deleteAll();   // Returns total count of deleted database records
    void close();
}