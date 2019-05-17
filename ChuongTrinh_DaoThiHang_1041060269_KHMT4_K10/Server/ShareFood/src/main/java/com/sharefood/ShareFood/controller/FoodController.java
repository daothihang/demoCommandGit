package com.sharefood.ShareFood.controller;

import com.sharefood.ShareFood.model.Food;
import com.sharefood.ShareFood.response.base.Response;
import com.sharefood.ShareFood.response.extend.CountResponse;
import com.sharefood.ShareFood.response.extend.FoodResponse;
import com.sharefood.ShareFood.response.extend.FoodsResponse;
import com.sharefood.ShareFood.service.base.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class FoodController extends AbstractController {

    private FoodService foodService;

    @RequestMapping(method = RequestMethod.GET, value = "/foods/all")
    public ResponseEntity<Response> getAll(){
        List<Food> foods = foodService.findAll();
        return responseData(new FoodsResponse(foods));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/foods")
    public ResponseEntity<Response> getAllWithPage(@RequestParam("page") int page){
        List<Food> foods = foodService.findWithPage(page);
        return responseData(new FoodsResponse(foods));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/foods/count")
    public ResponseEntity<Response> getCount() {
        return responseData(new CountResponse(foodService.countAll()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/foods")
    public ResponseEntity<Response> insert(@RequestBody Food food){
        return responseData(new FoodResponse(foodService.insert(food)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/foods")
    public ResponseEntity<Response> update(@RequestBody Food food){
        return responseData(new FoodResponse(foodService.update(food)));
    }
}
