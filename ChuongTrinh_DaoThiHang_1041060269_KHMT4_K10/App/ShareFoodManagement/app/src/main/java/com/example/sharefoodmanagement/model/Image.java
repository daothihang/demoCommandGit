package com.example.sharefoodmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image extends AbstractModel {

    private int id_image;
    private String url;
    private Restaurant restaurant;
}
