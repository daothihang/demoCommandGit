package com.example.sharefoodmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends AbstractModel {

    private int id_category;
    private String name;
    private String image;

    public Category(String name, String image) {
        this.name = name;
        this.image = image;
    }
}
