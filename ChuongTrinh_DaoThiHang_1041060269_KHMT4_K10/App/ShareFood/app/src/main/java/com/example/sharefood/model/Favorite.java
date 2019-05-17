package com.example.sharefood.model;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Favorite extends AbstractModel {

    private List<Price> prices;

    public Favorite() {
        prices = new ArrayList<>();
    }

    public boolean isExists(int id){
        boolean check = false;
        for (int i=0; i<prices.size(); i++){
            if (prices.get(i).getId_price() == id){
                check = true;
                break;
            }
        }
        return check;
    }

    public void insert(Price price){
        prices.add(price);
    }

    @SuppressLint("NewApi")
    public void delete(int id){
        prices.removeIf(p -> (p.getId_price() == id));
    }
}
