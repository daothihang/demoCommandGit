package com.sharefood.ShareFood.service.base;

import com.sharefood.ShareFood.model.Food;
import com.sharefood.ShareFood.model.FoodComment;

import java.util.List;

public interface FoodCommentService extends Service<FoodComment> {

    List<FoodComment> findAllByFood(Food food);
}
