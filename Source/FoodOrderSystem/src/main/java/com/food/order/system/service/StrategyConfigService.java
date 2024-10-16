package com.food.order.system.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.order.system.service.stratergy.RestaurantSelectionStrategy;

@Service
public class StrategyConfigService {

 private Map<String, RestaurantSelectionStrategy> strategies = new HashMap<>();
 private String currentStrategyName;

 @Autowired
 public StrategyConfigService(final Map<String, RestaurantSelectionStrategy> strategiesMap) {
     strategies.putAll(strategiesMap);
     // Set default strategy
     currentStrategyName = "lowerCostStrategy";
 }

 /**
  * Retrieves the current restaurant selection strategy.
  *
  * @return The current strategy.
  */
 public RestaurantSelectionStrategy getCurrentStrategy() {
     return strategies.get(currentStrategyName);
 }

 /**
  * Updates the current restaurant selection strategy.
  *
  * @param strategyName The name of the new strategy.
  */
 public void updateStrategy(String strategyName) {
     if (!strategies.containsKey(strategyName)) {
         throw new IllegalArgumentException("Strategy not found: " + strategyName);
     }
     currentStrategyName = strategyName;
 }

 /**
  * Retrieves all available strategies.
  *
  * @return A list of strategy names.
  */
 public Map<String, RestaurantSelectionStrategy> getAllStrategies() {
     return strategies;
 }
}

