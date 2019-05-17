package com.sharefood.ShareFood.controller;

import com.sharefood.ShareFood.model.Food;
import com.sharefood.ShareFood.model.FoodComment;
import com.sharefood.ShareFood.model.Manager;
import com.sharefood.ShareFood.response.base.Response;
import com.sharefood.ShareFood.response.extend.*;
import com.sharefood.ShareFood.service.base.FoodCommentService;
import com.sharefood.ShareFood.service.base.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class FoodCommentController extends AbstractController {

    private FoodCommentService foodCommentService;
    private FoodService foodService;

    @RequestMapping(method = RequestMethod.GET, value = "/foodComments")
    public ResponseEntity<Response> getAll(){
        List<FoodComment> foodComments = (List<FoodComment>) foodCommentService.gettAll();
        return responseData(new FoodCommentsResponse(foodComments));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/foodComments")
    public ResponseEntity<Response> insert(@RequestBody FoodComment foodComment){
        return responseData(new FoodCommentResponse(foodCommentService.insert(foodComment)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/foodComments")
    public ResponseEntity<Response> update(@RequestBody FoodComment foodComment){
        return responseData(new FoodCommentResponse(foodCommentService.update(foodComment)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/foodComments")
    public ResponseEntity<Response> delete(@RequestParam("id_comment") int id_comment){
        foodCommentService.delete(id_comment);
        return responseData(new BooleanResponse(true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/foodComments/get")
    public ResponseEntity<Response> getWithFood(@RequestParam("id_food") int id_food){
        Food food = foodService.findFoodById(id_food);
        List<FoodComment> foodComments = foodCommentService.findAllByFood(food);
        return responseData(new FoodCommentsResponse(foodComments));
    }
}
