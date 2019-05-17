package com.example.sharefood.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodComment extends AbstractModel{

    private int id;

    private String comment;

    private String created_at;

    private User user;

    private Food food;

    public FoodComment(String comment, String created_at, User user, Food food) {
        this.comment = comment;
        this.created_at = created_at;
        this.user = user;
        this.food = food;
    }
}
