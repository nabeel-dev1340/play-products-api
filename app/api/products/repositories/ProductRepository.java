package api.products.repositories;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

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

    public CompletionStage<List<Product>> findAll() {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction((EntityManager em) -> {
                List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
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
