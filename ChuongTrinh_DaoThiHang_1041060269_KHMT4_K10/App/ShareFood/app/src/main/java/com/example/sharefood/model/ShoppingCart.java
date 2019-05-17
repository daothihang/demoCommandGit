package com.example.sharefood.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShoppingCart extends AbstractModel {
    private List<Cart> carts;

    public ShoppingCart() {
        carts = new ArrayList<>();
    }

    public void addCart(Cart cart){
        carts.add(cart);
    }

    public void removeCart(Cart cart){
        carts.remove(cart);
    }

    public void removeAll(){
        carts.clear();
    }
}
