package com.sharefood.ShareFood.controller;

import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.response.base.Response;
import com.sharefood.ShareFood.response.extend.BooleanResponse;
import com.sharefood.ShareFood.response.extend.CountResponse;
import com.sharefood.ShareFood.response.extend.RestaurantResponse;
import com.sharefood.ShareFood.response.extend.RestaurantsResponse;
import com.sharefood.ShareFood.service.base.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class RestaurantController extends AbstractController {

    private RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, value = "/restaurants")
    public ResponseEntity<Response> getAllWithPage(@RequestParam("page") int page){
        List<Restaurant> restaurants = restaurantService.findWithPage(page);
        return responseData(new RestaurantsResponse(restaurants));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restaurants/getAll")
    public ResponseEntity<Response> getAllWithPage(){
        List<Restaurant> restaurants = (List<Restaurant>) restaurantService.gettAll();
        return responseData(new RestaurantsResponse(restaurants));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restaurants/count")
    public ResponseEntity<Response> getCount() {
        return responseData(new CountResponse(restaurantService.countAll()));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/restaurants/delete")
    public ResponseEntity<Response> deleteRestaurant(@RequestParam("id_restaurant") int id_restaurant){
        restaurantService.delete(id_restaurant);
        return responseData(new BooleanResponse(true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restaurants/lock")
    public ResponseEntity<Response> lockRestaurant(@RequestParam("id_restaurant") int id_restaurant){
        int result = restaurantService.blockRestaurant(id_restaurant);
        if (result == 1){
            return responseData(new BooleanResponse(true));
        }else {
            return responseData(new BooleanResponse(false));
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restaurants/unlock")
    public ResponseEntity<Response> unlockRestaurant(@RequestParam("id_restaurant") int id_restaurant){
        int result = restaurantService.unblockRestaurant(id_restaurant);
        if (result == 1){
            return responseData(new BooleanResponse(true));
        }else {
            return responseData(new BooleanResponse(false));
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restaurants")
    public ResponseEntity<Response> insert(@RequestBody Restaurant restaurant){
        return responseData(new RestaurantResponse(restaurantService.insert(restaurant)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/restaurants")
    public ResponseEntity<Response> update(@RequestBody Restaurant restaurant){
        return responseData(new RestaurantResponse(restaurantService.update(restaurant)));
    }

}
