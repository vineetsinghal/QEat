
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;


  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
        
        List<Restaurant> listOfRestaurants = new ArrayList<>();
        GetRestaurantsResponse getRestaurantsResponse = new GetRestaurantsResponse();

        int hr = currentTime.getHour();
        int min = currentTime.getMinute();
        LocalTime time = LocalTime.of(hr,min);

        if((time.compareTo(LocalTime.of(8,0))>=0 && time.compareTo(LocalTime.of(10,0))<=0) || (time.compareTo(LocalTime.of(13,0))>=0 && time.compareTo(LocalTime.of(14,0))<=0) || (time.compareTo(LocalTime.of(19,0))>=0 && time.compareTo(LocalTime.of(21,0))<=0)){
          listOfRestaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), currentTime, peakHoursServingRadiusInKms);
        }
        else{
          listOfRestaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), currentTime, normalHoursServingRadiusInKms);
        }
        
        getRestaurantsResponse.setRestaurants(listOfRestaurants);

     return getRestaurantsResponse;
  }


}

