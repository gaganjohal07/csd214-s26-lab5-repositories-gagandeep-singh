package bookstore.repositories;

import bookstore.entities.ProductEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InMemoryMapRepository implements IRepository<ProductEntity> {

    private final Map<Long, ProductEntity> map = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public ProductEntity save(ProductEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot save a null entity.");
        }

        if (entity.getId() == null) {
            entity.setId(idCounter++);
        }

        map.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public ProductEntity findById(Long id) {
        if (id == null) {
            return null;
        }

        return map.get(id);
    }

    @Override
    public ProductEntity findByProductId(String productId) {
        if (productId == null) {
            return null;
        }

        for (ProductEntity entity : map.values()) {
            if (Objects.equals(entity.getProductId(), productId)) {
                return entity;
            }
        }

        return null;
    }

    @Override
    public List<ProductEntity> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            map.remove(id);
        }
    }

    @Override
    public long count() {
        return map.size();
    }

    @Override
    public int deleteAll() {
        int deleted = map.size();
        map.clear();
        return deleted;
    }

    @Override
    public String getDataSourceType() {
        return "VOLATILE RAM (HashMap - Indexed Search)";
    }

    @Override
    public void close() {
        map.clear();
    }
}