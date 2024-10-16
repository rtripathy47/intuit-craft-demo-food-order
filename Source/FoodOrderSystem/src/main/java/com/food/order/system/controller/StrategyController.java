package com.food.order.system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.system.service.StrategyConfigService;

/**
* Controller to manage restaurant selection strategies.
*/
@RestController
@RequestMapping("/api/strategies")
public class StrategyController {

 @Autowired
 private StrategyConfigService strategyConfigService;

 /**
  * Retrieves the current selection strategy.
  *
  * @return The name of the current strategy.
  */
 @GetMapping("/current")
 public ResponseEntity<String> getCurrentStrategy() {
     // Assuming the strategy names are unique identifiers
     String currentStrategy = strategyConfigService.getCurrentStrategy().getClass().getSimpleName();
     return ResponseEntity.ok(currentStrategy);
 }

 /**
  * Updates the current selection strategy.
  *
  * @param strategyName The name of the new strategy.
  * @return Success message.
  */
 @PutMapping("/current")
 public ResponseEntity<String> updateCurrentStrategy(@RequestParam String strategyName) {
     try {
         strategyConfigService.updateStrategy(strategyName);
         return ResponseEntity.ok("Strategy updated to: " + strategyName);
     } catch (IllegalArgumentException e) {
         return ResponseEntity.badRequest().body(e.getMessage());
     }
 }

 /**
  * Retrieves all available strategies.
  *
  * @return List of strategy names.
  */
 @GetMapping
 public ResponseEntity<?> getAllStrategies() {
     return ResponseEntity.ok(strategyConfigService.getAllStrategies().keySet());
 }
}

