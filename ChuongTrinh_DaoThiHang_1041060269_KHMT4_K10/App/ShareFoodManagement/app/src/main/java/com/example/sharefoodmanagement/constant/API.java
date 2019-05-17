package com.example.sharefoodmanagement.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class API {

    public static final String API_LOCATION = "https://geocoder.api.here.com/6.2/geocode.json?app_id=crNVEMg0fda61lPd8Xqp&app_code=heHRyrE91izOL_Vpo_YFRw&searchtext=";
    private static final String IP = "http://192.168.1.11:9996/v1/";
    public static final String STORAGE = "http://192.168.1.11:9996/storage_share_food/";
    //user
    public static final String LOGIN = IP + "users";
    public static final String FORGOT_PASS = IP + "/users/forgot";
    public static final String GET_ALL_USER = IP + "users/all";

    //restaurant
    public static final String GET_ALL_RESTAURANT = IP + "restaurants/getAll";
    public static final String DELETE_RESTAURANT = IP + "restaurants/delete";
    public static final String BLOCK_RESTAURANT = IP + "restaurants/lock";
    public static final String UNBLOCK_RESTAURANT = IP + "restaurants/unlock";
    public static final String POST_RESTAURANT = IP + "restaurants";

    //image
    public static final String GET_ALL_IMAGE = IP + "images";

    //category
    public static final String GET_ALL_CATEGORY = IP + "categories";

    //manager
    public static final String GET_MANAGER = IP + "managers/get";

    //price
    public static final String COUNT_PRICE = IP + "prices/count";
    public static final String SEARCH_PRICE = IP + "prices/search";
    public static final String GET_PRICE = IP + "prices/get";
    public static final String SEARCH_PRICE_WITH_CATEGORY = IP + "prices/category";
    public static final String PRICE_WITH_PAGE = IP + "prices";

    //food
    public static final String POST_FOOD = IP + "foods";
}
