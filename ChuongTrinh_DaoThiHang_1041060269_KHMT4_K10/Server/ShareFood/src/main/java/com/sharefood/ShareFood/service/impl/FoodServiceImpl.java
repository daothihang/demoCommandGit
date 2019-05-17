package com.sharefood.ShareFood.service.impl;

import com.sharefood.ShareFood.model.Food;
import com.sharefood.ShareFood.repository.FoodRepository;
import com.sharefood.ShareFood.service.base.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    @Override
    public List<Food> findWithPage(int page) {
        int limit = 5;
        int offset = limit*(page - 1);
        return foodRepository.findWithPage(offset);
    }

    @Override
    public int countAll() {
        return foodRepository.countAll();
    }

    @Override
    public Food findFoodById(int id) {
        return foodRepository.findFoodById(id);
    }

    @Override
    public Food insert(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public Food update(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public void delete(int id) {
        foodRepository.deleteById(id);
    }

    @Override
    public Collection<Food> gettAll() {
        return foodRepository.findAll();
    }
}
