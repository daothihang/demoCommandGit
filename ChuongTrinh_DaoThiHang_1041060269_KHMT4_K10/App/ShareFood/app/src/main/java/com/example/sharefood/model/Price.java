package com.example.sharefood.model;

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
}
