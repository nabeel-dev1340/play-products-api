package api.products.repositories;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.function.Function;
import java.util.ArrayList;

import api.products.dtos.ProductDTO;
import api.products.models.Product;

import java.sql.SQLException;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class ProductRepository {

    private final JPAApi jpaApi;

    @Inject
    public ProductRepository(JPAApi api) {
        this.jpaApi = api;
    }

    public CompletionStage<Product> create(ProductDTO productDTO) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction((EntityManager em) -> {
                Product product = new Product(productDTO.getName(), productDTO.getPrice(), productDTO.getCategory());
                em.persist(product);
                return product;
            });
        });
    }

    public CompletionStage<List<Product>> findAll(int page, int pageSize, String sortBy) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction((EntityManager em) -> {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<Product> cq = cb.createQuery(Product.class);
                Root<Product> root = cq.from(Product.class);
                
                // sorting
                if(sortBy != null && !sortBy.isEmpty()) {
                    if(sortBy.equals("id")) {
                        cq.orderBy(cb.asc(root.get("id")));
                    } else {
                        cq.orderBy(cb.asc(root.get(sortBy)));
                    }
                }
                
                // pagination
                int offset = (page - 1) * pageSize;
                cq.select(root).orderBy(cb.asc(root.get("id")));
                TypedQuery<Product> query = em.createQuery(cq).setFirstResult(offset).setMaxResults(pageSize);
                List<Product> products = query.getResultList();
                
                return products;
            });
        }).thenApplyAsync(products -> new ArrayList<>(products));
    }

    public CompletionStage<Product> findById(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction((EntityManager em) -> {
                return em.find(Product.class, id);
            });
        });
    }

    public CompletionStage<Integer> delete(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction((EntityManager em) -> {
                Query query = em.createQuery("DELETE FROM Product p WHERE p.id = :id");
                query.setParameter("id", id);
                return query.executeUpdate();
            });
        });
    }

    public CompletionStage<Product> update(Long id, ProductDTO productDTO) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction((EntityManager em) -> {
                Product existingProduct = em.find(Product.class, id);
                if (existingProduct != null) {
                    existingProduct.setName(productDTO.getName());
                    existingProduct.setPrice(productDTO.getPrice());
                    existingProduct.setCategory(productDTO.getCategory());
                    em.merge(existingProduct);
                }
                return existingProduct;
            });
        });
    }

}
