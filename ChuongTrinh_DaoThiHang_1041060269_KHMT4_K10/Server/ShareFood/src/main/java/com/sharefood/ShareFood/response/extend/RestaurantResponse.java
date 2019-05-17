package com.sharefood.ShareFood.response.extend;

import com.sharefood.ShareFood.model.Restaurant;
import com.sharefood.ShareFood.response.base.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponse extends AbstractResponse {
    private Restaurant restaurant;
}
