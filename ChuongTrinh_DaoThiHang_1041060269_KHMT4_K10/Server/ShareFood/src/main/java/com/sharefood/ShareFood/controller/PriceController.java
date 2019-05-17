package com.sharefood.ShareFood.controller;

import com.sharefood.ShareFood.model.Price;
import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.response.base.Response;
import com.sharefood.ShareFood.response.extend.BooleanResponse;
import com.sharefood.ShareFood.response.extend.CountResponse;
import com.sharefood.ShareFood.response.extend.PriceResponse;
import com.sharefood.ShareFood.response.extend.PricesResponse;
import com.sharefood.ShareFood.service.base.PriceService;
import com.sharefood.ShareFood.service.base.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class PriceController extends AbstractController{

    private PriceService priceService;
    private RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, value = "/prices")
    public ResponseEntity<Response> getAllWithPage(@RequestParam("page") int page){
        List<Price> prices = priceService.findWithPage(page);
        return responseData(new PricesResponse(prices));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/prices/count")
    public ResponseEntity<Response> getCount(){
        return responseData(new CountResponse(priceService.countAll()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/prices/get")
    public ResponseEntity<Response> get(@RequestParam("id_restaurant") int id_restaurant){
        Restaurant restaurant = restaurantService.findRestaurantById_restaurant(id_restaurant);
        return responseData(new PricesResponse(priceService.findAllByRestaurant(restaurant)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/prices/search")
    public ResponseEntity<Response> search(@RequestParam("id_province") int id_province){
        return responseData(new PricesResponse(priceService.searchWithProvince(id_province)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/prices/category")
    public ResponseEntity<Response> searchWithCategory(@RequestParam("id_category") int id_category){
        return responseData(new PricesResponse(priceService.searchWithCategory(id_category)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/prices")
    public ResponseEntity<Response> update(@RequestBody Price price){
        return responseData(new PriceResponse(priceService.update(price)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/prices")
    public ResponseEntity<Response> insert(@RequestBody Price price){
        return responseData(new PriceResponse(priceService.insert(price)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/prices")
    public ResponseEntity<Response> delete(@RequestParam("id_price") int id_price){
        priceService.delete(id_price);
        return responseData(new BooleanResponse(true));
    }
}
