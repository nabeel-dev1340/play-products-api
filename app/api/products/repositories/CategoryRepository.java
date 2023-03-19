package api.products.repositories;

import api.products.models.Category;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CategoryRepository {

    private final JPAApi jpaApi;

    @Inject
    public CategoryRepository(JPAApi api) {
        this.jpaApi = api;
    }

    public CompletionStage<Category> create(Category category) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction((EntityManager em) -> {
                em.persist(category);
                return category;
            });
        });
    }

    public List<Category> findAll() {
        return jpaApi.withTransaction(em -> {
            TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c", Category.class);
            return query.getResultList();
        });
    }    

}
