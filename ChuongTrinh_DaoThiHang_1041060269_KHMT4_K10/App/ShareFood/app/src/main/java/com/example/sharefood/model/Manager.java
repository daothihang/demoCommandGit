package com.example.sharefood.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Manager extends AbstractModel {


    private int id;


    private User user;


    private Restaurant restaurant;

    public Manager(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
    }
}
