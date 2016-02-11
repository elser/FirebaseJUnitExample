package com.firebasejunitexample.app.services;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebasejunitexample.app.vo.Product;

/**
 * Simple service to be tested. Performs CRUD operations on "products" firebase node.
 */
public class ProductsService {
    private Firebase firebaseNode;

    public ProductsService(Firebase firebase) {
        this.firebaseNode = firebase.child("products");
    }

    public void save(Product product) {
        firebaseNode.child(product.getKey()).setValue(product);
    }

    public void load(String key, final ValueEventListener listener) {
        firebaseNode.child(key).addListenerForSingleValueEvent(listener);
    }

    public void remove(String key) {
        firebaseNode.child(key).removeValue();
    }
}
