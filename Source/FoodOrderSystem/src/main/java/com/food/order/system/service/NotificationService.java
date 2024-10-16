package com.food.order.system.service;

import static com.food.order.system.config.RabbitMQConfig.DISPATCH_QUEUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.order.system.dto.DispatchNotification;
import com.food.order.system.entity.Restaurant;
import com.food.order.system.exception.FoodOrderSystemException;
import com.food.order.system.repository.RestaurantRepository;

import jakarta.persistence.OptimisticLockException;

@Service
public class NotificationService {

	    @Autowired
	    private RestaurantRepository restaurantRepository;

	    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	    
	    @Autowired
	    private RedisService redisService;


	    @RabbitListener(queues = DISPATCH_QUEUE)
	    public void handleDispatchNotification(DispatchNotification notification) {
	        resetRestaurantsProcessingCapacity(notification);
	    }

	    @Retryable(
		        retryFor = { OptimisticLockException.class },
		        maxAttempts = 3,
		        backoff = @Backoff(delay = 100)
		    )
		    @Transactional
		public void resetRestaurantsProcessingCapacity(DispatchNotification notification) {
			try {
	        	//waiting for 15s to denote order processing time by restaurant 
	        	Thread.sleep(15000);
	        	
	        	logger.info("Processing done for Order ID: {} from Restaurant ID: {}",notification.getOrderItemId(),notification.getRestaurantId());
	            Long restaurantId = notification.getRestaurantId();
	            int quantityDispatched = notification.getQuantityDispatched();

	            // Fetch the restaurant from the database
	            Restaurant restaurant = restaurantRepository.findById(restaurantId)
	                    .orElseThrow(() -> new FoodOrderSystemException("Restaurant not found"));

	            // Decrease currentLoad
	            int newCurrentLoad = restaurant.getCurrentLoad() - quantityDispatched;
	            
	            //this can never happen ideally cause we are maintaining acid properties using optimistic locking
	            if (newCurrentLoad < 0) {
	                throw new IllegalStateException("Current load cannot be negative");
	            }
	            restaurant.setCurrentLoad(newCurrentLoad);

	            // Save the updated restaurant
	            restaurantRepository.save(restaurant);

	            // fetch the current load from redis is commented as there can be temporary inconsistencies even though optimistic lock is present
//	            redisService.increaseCapacity(restaurantId, quantityDispatched);


	        } catch (Exception e) {
	        	 logger.error("Error processing DispatchNotification for Order ID: {}",
	                     notification.getOrderItemId(), e);
	                 throw new RuntimeException(e); // Rethrow to trigger retry
	        }
		}

	    @Recover
	    public void recover(OptimisticLockException e, DispatchNotification notification) {
	    	//TODO put to a DDQ
	    	throw new IllegalStateException("Couldnot reset the state of system after dispatch");
	    }
	}

