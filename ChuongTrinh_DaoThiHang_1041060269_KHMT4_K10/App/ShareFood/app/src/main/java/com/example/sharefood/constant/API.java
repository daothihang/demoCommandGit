package com.example.sharefood.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class API {

    public static final String API_LOCATION = "https://geocoder.api.here.com/6.2/geocode.json?app_id=crNVEMg0fda61lPd8Xqp&app_code=heHRyrE91izOL_Vpo_YFRw&searchtext=";
    private static final String IP = "http://192.168.1.11:9998/v1/";

    public static final String STORAGE = "http://192.168.1.11/storage_share_food/";
    //price
    public static final String COUNT_PRICE = IP + "prices/count";
    public static final String SEARCH_PRICE = IP + "prices/search";
    public static final String SEARCH_PRICE_WITH_CATEGORY = IP + "prices/category";
    public static final String PRICE_WITH_PAGE = IP + "prices";

    //province
    public static final String GET_ALL_PROVINCE = IP + "provinces";

    //user
    public static final String LOGIN = IP + "users";
    public static final String GET_MANAGER = IP + "managers/get";
    public static final String FORGOT_PASS = IP + "/users/forgot";

    //category
    public static final String GET_ALL_CATEGORY = IP + "categories";

    //food_comment

    public static final String GET_COMMENT = IP + "foodComments/get";
    public static final String POST_COMMENT = IP + "foodComments";

    //restaurant
    public static final String POST_RESTAURANT = IP + "restaurants";

    //manager
    public static final String POST_MANAGER = IP + "managers";

    //image
    public static final String POST_IMAGE = IP + "images";
}
