package com.sharefood.ShareFood.controller;

import com.sharefood.ShareFood.model.Image;
import com.sharefood.ShareFood.model.Price;
import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.response.base.Response;
import com.sharefood.ShareFood.response.extend.ImageResponse;
import com.sharefood.ShareFood.response.extend.ImgResponse;
import com.sharefood.ShareFood.response.extend.PricesResponse;
import com.sharefood.ShareFood.service.base.ImageService;
import com.sharefood.ShareFood.service.base.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class ImageController extends AbstractController {

    private ImageService imageService;
    private RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, value = "/images")
    public ResponseEntity<Response> getAll(@RequestParam("id_restaurant") int id_restaurant){
        Restaurant restaurant = restaurantService.findRestaurantById(id_restaurant);
        List<Image> images = imageService.findAllByRestaurant(restaurant);
        return responseData(new ImageResponse(images));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/images")
    public ResponseEntity<Response> post(@RequestBody Image image){
        return responseData(new ImgResponse(imageService.insert(image)));
    }


}
