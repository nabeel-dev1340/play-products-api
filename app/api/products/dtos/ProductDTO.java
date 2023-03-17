package api.products.dtos;

import api.products.models.Product;

public class ProductDTO {

    public String id;
    public String name;
    public float price;
    public String category;

    public ProductDTO() {
    }

    public ProductDTO(String id, String name, float price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public ProductDTO(Product data) {
        this.id = data.getId().toString();
        this.name = data.getName();
        this.price = data.getPrice();
        this.category = data.getCategory();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}