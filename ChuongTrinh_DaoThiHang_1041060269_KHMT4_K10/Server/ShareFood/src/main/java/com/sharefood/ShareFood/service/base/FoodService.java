package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.Food;

import java.util.List;

public interface FoodService extends Service<Food> {

    List<Food> findAll();

    List<Food> findWithPage(int page);

    int countAll();

    Food findFoodById(int id);

}
