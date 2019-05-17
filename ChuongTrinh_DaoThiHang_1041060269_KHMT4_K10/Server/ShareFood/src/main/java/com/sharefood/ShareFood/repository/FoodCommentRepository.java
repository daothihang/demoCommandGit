package com.sharefood.ShareFood.repository;

import com.sharefood.ShareFood.model.Food;
import com.sharefood.ShareFood.model.FoodComment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FoodCommentRepository extends CrudRepository<FoodComment, Integer> {
    List<FoodComment> findAllByFood(Food food);
}
