package com.sharefood.ShareFood.service.impl;

import com.sharefood.ShareFood.model.Food;
import com.sharefood.ShareFood.model.FoodComment;
import com.sharefood.ShareFood.repository.FoodCommentRepository;
import com.sharefood.ShareFood.service.base.FoodCommentService;
import com.sharefood.ShareFood.service.base.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class FoodCommentServiceImpl implements FoodCommentService {

    @Autowired
    private FoodCommentRepository foodCommentRepository;

    @Override
    public FoodComment insert(FoodComment foodComment) {
        return foodCommentRepository.save(foodComment);
    }

    @Override
    public FoodComment update(FoodComment foodComment) {
        return foodCommentRepository.save(foodComment);
    }

    @Override
    public void delete(int id) {
        foodCommentRepository.deleteById(id);
    }

    @Override
    public Collection<FoodComment> gettAll() {
        return (Collection<FoodComment>) foodCommentRepository.findAll();
    }

    @Override
    public List<FoodComment> findAllByFood(Food food) {
        return foodCommentRepository.findAllByFood(food);
    }
}
