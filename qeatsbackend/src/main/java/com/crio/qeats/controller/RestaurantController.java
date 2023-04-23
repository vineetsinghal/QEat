/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.controller;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.services.RestaurantService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



// TODO: CRIO_TASK_MODULE_RESTAURANTSAPI
// Implement Controller using Spring annotations.
// Remember, annotations have various "targets". They can be class level, method level or others.

@RestController
@RequestMapping(RestaurantController.RESTAURANT_API_ENDPOINT)
public class RestaurantController {

  public static final String RESTAURANT_API_ENDPOINT = "/qeats/v1";
  public static final String RESTAURANTS_API = "/restaurants";
  public static final String MENU_API = "/menu";
  public static final String CART_API = "/cart";
  public static final String CART_ITEM_API = "/cart/item";
  public static final String CART_CLEAR_API = "/cart/clear";
  public static final String POST_ORDER_API = "/order";
  public static final String GET_ORDERS_API = "/orders";

  
  @Autowired
  private RestaurantService restaurantService;


  @GetMapping(RESTAURANTS_API)
  public ResponseEntity<GetRestaurantsResponse> getRestaurants(
    @Valid GetRestaurantsRequest getRestaurantsRequest){

    //log.info("getRestaurants called with {}", getRestaurantsRequest);
    GetRestaurantsResponse getRestaurantsResponse;

    List<Restaurant> listOfRestaurantsAfterUpdate = new ArrayList<>();
      
    if(getRestaurantsRequest.getLatitude() == 0.0 || getRestaurantsRequest.getLongitude() == 0.0)
      return ResponseEntity.badRequest().body(null);

    //CHECKSTYLE:OFF
    getRestaurantsResponse = restaurantService.findAllRestaurantsCloseBy(getRestaurantsRequest, LocalTime.now());

    if(getRestaurantsResponse.getRestaurants().size()!=0){
      List<Restaurant> listOfRestaurants = getRestaurantsResponse.getRestaurants();
      for(Restaurant restaurant : listOfRestaurants){
        String restaurantName = restaurant.getName();
        int index = restaurantName.indexOf('é');
        if(index!=-1){
          restaurantName = restaurantName.substring(0, index) + 'e' + restaurantName.substring(index + 1);;
          restaurant.setName(restaurantName);
        }
        listOfRestaurantsAfterUpdate.add(restaurant);
      }
      getRestaurantsResponse.setRestaurants(listOfRestaurantsAfterUpdate);
    }

    //log.info("getRestaurants returned {}", getRestaurantsResponse);
    //CHECKSTYLE:ON

    return ResponseEntity.ok().body(getRestaurantsResponse);
  }



  // TIP(MODULE_MENUAPI): Model Implementation for getting menu given a restaurantId.
  // Get the Menu for the given restaurantId
  // API URI: /qeats/v1/menu?restaurantId=11
  // Method: GET
  // Query Params: restaurantId
  // Success Output:
  // 1). If restaurantId is present return Menu
  // 2). Otherwise respond with BadHttpRequest.
  //
  // HTTP Code: 200
  // {
  //  "menu": {
  //    "items": [
  //      {
  //        "attributes": [
  //          "South Indian"
  //        ],
  //        "id": "1",
  //        "imageUrl": "www.google.com",
  //        "itemId": "10",
  //        "name": "Idly",
  //        "price": 45
  //      }
  //    ],
  //    "restaurantId": "11"
  //  }
  // }
  // Error Response:
  // HTTP Code: 4xx, if client side error.
  //          : 5xx, if server side error.
  // Eg:
  // curl -X GET "http://localhost:8081/qeats/v1/menu?restaurantId=11"


}

