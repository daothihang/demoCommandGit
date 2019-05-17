package com.sharefood.ShareFood.controller;

import com.sharefood.ShareFood.model.Food;
import com.sharefood.ShareFood.model.FoodComment;
import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.model.RestaurantComment;
import com.sharefood.ShareFood.response.base.Response;
import com.sharefood.ShareFood.response.extend.*;
import com.sharefood.ShareFood.service.base.RestaurantCommentService;
import com.sharefood.ShareFood.service.base.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class RestaurantCommentController extends AbstractController{

    private RestaurantCommentService restaurantCommentService;
    private RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, value = "/restaurantComments")
    public ResponseEntity<Response> getAll(){
        List<RestaurantComment> restaurantComments = (List<RestaurantComment>) restaurantCommentService.gettAll();
        return responseData(new RestaurantCommentsResponse(restaurantComments));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restaurantComments")
    public ResponseEntity<Response> insert(@RequestBody RestaurantComment restaurantComment){
        return responseData(new RestaurantCommentResponse(restaurantCommentService.insert(restaurantComment)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/restaurantComments")
    public ResponseEntity<Response> update(@RequestBody RestaurantComment restaurantComment){
        return responseData(new RestaurantCommentResponse(restaurantCommentService.update(restaurantComment)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/restaurantComments")
    public ResponseEntity<Response> delete(@RequestParam("id_comment") int id_comment){
        restaurantCommentService.delete(id_comment);
        return responseData(new BooleanResponse(true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restaurantComments/get")
    public ResponseEntity<Response> getWithRestaurant(@RequestParam("id_restaurant") int id_restaurant){
        Restaurant restaurant = restaurantService.findRestaurantById(id_restaurant);
        List<RestaurantComment> restaurantComments = restaurantCommentService.findAllByRestaurant(restaurant);
        return responseData(new RestaurantCommentsResponse(restaurantComments));
    }
}
