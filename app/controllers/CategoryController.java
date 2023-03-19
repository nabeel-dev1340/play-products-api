package controllers;

import api.products.dtos.CategoryDTO;
import api.products.models.Category;
import api.products.services.CategoryService;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;

import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

public class CategoryController extends Controller {

    private final CategoryService categoryService;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public CategoryController(CategoryService categoryService, HttpExecutionContext httpExecutionContext) {
        this.categoryService = categoryService;
        this.httpExecutionContext = httpExecutionContext;
    }

    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        CategoryDTO categoryDTO = fromJson(json, CategoryDTO.class);

        return categoryService.createCategory(categoryDTO)
                .thenApplyAsync(category -> {
                    CategoryDTO dto = new CategoryDTO(category.getName());
                    return created(Json.toJson(dto));
                }, httpExecutionContext.current());
    }

    public Result findAll() {
        List<Category> categories = categoryService.findAllCategories();
        JsonNode json = Json.toJson(categories);
        return ok(json);
    }

}
