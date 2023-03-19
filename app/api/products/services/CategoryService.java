package api.products.services;

import api.products.dtos.CategoryDTO;
import api.products.models.Category;
import api.products.repositories.CategoryRepository;

import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Inject
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CompletionStage<Category> createCategory(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO.getName());
        return categoryRepository.create(category);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }    

}
