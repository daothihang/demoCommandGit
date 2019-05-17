package com.example.sharefoodmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Price extends AbstractModel{

    private int id_price;
    private int price;
    private Food food;
    private Restaurant restaurant;

    public Price(int price, Food food, Restaurant restaurant) {
        this.price = price;
        this.food = food;
        this.restaurant = restaurant;
    }
}
