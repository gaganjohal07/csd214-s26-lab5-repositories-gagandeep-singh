package bookstore.repositories;

import bookstore.entities.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class MySqlRepository implements IRepository<ProductEntity> {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public MySqlRepository() {
        this.emf = Persistence.createEntityManagerFactory("mysql-pu");
        this.em = emf.createEntityManager();
    }

    @Override
    public ProductEntity save(ProductEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot save a null entity.");
        }

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            if (entity.getId() == null) {
                em.persist(entity);
            } else {
                entity = em.merge(entity);
            }

            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    @Override
    public ProductEntity findById(Long id) {
        if (id == null) {
            return null;
        }

        return em.find(ProductEntity.class, id);
    }

    @Override
    public ProductEntity findByProductId(String productId) {
        if (productId == null) {
            return null;
        }

        try {
            return em.createQuery(
                            "SELECT p FROM ProductEntity p WHERE p.productId = :prodId",
                            ProductEntity.class)
                    .setParameter("prodId", productId)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public List<ProductEntity> findAll() {
        return em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class)
                .getResultList();
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            ProductEntity entity = em.find(ProductEntity.class, id);

            if (entity != null) {
                em.remove(entity);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(p) FROM ProductEntity p", Long.class)
                .getSingleResult();
    }

    @Override
    public int deleteAll() {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            int deletedCount = em.createQuery("DELETE FROM ProductEntity")
                    .executeUpdate();

            tx.commit();
            return deletedCount;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    @Override
    public String getDataSourceType() {
        return "PERSISTENT MySQL (Production - Port 3333)";
    }

    @Override
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }

        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}