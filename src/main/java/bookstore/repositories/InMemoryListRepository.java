package bookstore.repositories;

import bookstore.entities.ProductEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryListRepository implements IRepository<ProductEntity> {

    private final List<ProductEntity> list = new ArrayList<>();
    private Long idCounter = 1L;

    @Override
    public ProductEntity save(ProductEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot save a null entity.");
        }

        if (entity.getId() == null) {
            entity.setId(idCounter++);
            list.add(entity);
        } else {

            int foundIndex = -1;

            for (int i = 0; i < list.size(); i++) {
                if (Objects.equals(list.get(i).getId(), entity.getId())) {
                    foundIndex = i;
                    break;
                }
            }

            if (foundIndex != -1) {
                list.set(foundIndex, entity);
            } else {
                list.add(entity);
            }
        }

        return entity;
    }

    @Override
    public ProductEntity findById(Long id) {

        if (id == null) {
            return null;
        }

        for (ProductEntity entity : list) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }

        return null;
    }

    @Override
    public ProductEntity findByProductId(String productId) {

        if (productId == null) {
            return null;
        }

        for (ProductEntity entity : list) {
            if (Objects.equals(entity.getProductId(), productId)) {
                return entity;
            }
        }

        return null;
    }

    @Override
    public List<ProductEntity> findAll() {
        return new ArrayList<>(list);
    }

    @Override
    public void delete(Long id) {

        if (id == null) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(list.get(i).getId(), id)) {
                list.remove(i);
                break;
            }
        }
    }

    @Override
    public long count() {
        return list.size();
    }

    @Override
    public int deleteAll() {
        int deleted = list.size();
        list.clear();
        return deleted;
    }

    @Override
    public String getDataSourceType() {
        return "VOLATILE RAM (ArrayList - Sequential Search)";
    }

    @Override
    public void close() {
        list.clear();
    }
}