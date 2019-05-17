package com.example.sharefoodmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Food extends AbstractModel{
    private int id;
    private String name;
    private String description;
    private String image;
    private String recipe;
    private Category category;

    public Food(String name, String description, String image, String recipe, Category category) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.recipe = recipe;
        this.category = category;
    }
}
