package com.firebasejunitexample.app.vo;

/**
 * Value Object for products
 */
public class Product {
    private final String key;
    private String name;
    private int rating;

    public Product() {
        key = null;
    }

    public Product(String key, String name, int rating) {
        this.key = key;
        this.name = name;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Product product = (Product) o;

        if (rating != product.rating) {
            return false;
        }
        if (key != null ? !key.equals(product.key) : product.key != null) {
            return false;
        }
        return !(name != null ? !name.equals(product.name) : product.name != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + rating;
        return result;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
