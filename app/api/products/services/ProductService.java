package api.products.services;

import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;

import javax.inject.Inject;

import api.products.dtos.ProductDTO;
import api.products.models.Product;
import api.products.repositories.ProductRepository;

import java.nio.charset.CharacterCodingException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductService {

    private final ProductRepository repository;

    @Inject
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public CompletionStage<ProductDTO> create(ProductDTO productDTO) {
        return repository.create(productDTO).thenApplyAsync(product -> new ProductDTO(product));
    }

    public CompletionStage<List<ProductDTO>> getAllProducts() {
        return repository.findAll()
                .thenApplyAsync(products -> products.stream()
                        .map(ProductDTO::new)
                        .collect(Collectors.toList()));
    }

    public CompletionStage<ProductDTO> findById(Long id) {
        return repository.findById(id).thenApplyAsync(product -> new ProductDTO(product));
    }

    public CompletionStage<Boolean> deleteProduct(Long id) {
        return repository.delete(id).thenApplyAsync(rowsAffected -> {
            return rowsAffected > 0;
        });
    }

    public CompletionStage<Product> updateProduct(Long id, ProductDTO product) {
        return repository.update(id, product);
    }

}
