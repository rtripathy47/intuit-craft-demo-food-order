//package com.food.order.system.service;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.argThat;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.dao.OptimisticLockingFailureException;
//
//import com.food.order.system.dto.DispatchNotification;
//import com.food.order.system.entity.Restaurant;
//import com.food.order.system.exception.FoodOrderSystemException;
//import com.food.order.system.repository.RestaurantRepository;
//
//import jakarta.persistence.OptimisticLockException;
//
//@ExtendWith(MockitoExtension.class)
//class NotificationServiceTest {
//
//    @Mock
//    private RestaurantRepository restaurantRepository;
//
//    @Mock
//    private RedisService redisService;
//
//    @InjectMocks
//    private NotificationService notificationService;
//
//    private DispatchNotification notification;
//    private Restaurant restaurant;
//
//    @BeforeEach
//    void setUp() {
//        notification = new DispatchNotification();
//        notification.setOrderItemId(1L);
//        notification.setRestaurantId(1L);
//        notification.setQuantityDispatched(5);
//
//        restaurant = new Restaurant();
//        restaurant.setId(1L);
//        restaurant.setCurrentLoad(10);
//    }
//
//    @Test
//    void handleDispatchNotification_shouldCallResetRestaurantsProcessingCapacity() {
//        doNothing().when(notificationService).resetRestaurantsProcessingCapacity(any(DispatchNotification.class));
//
//        notificationService.handleDispatchNotification(notification);
//
//        verify(notificationService).resetRestaurantsProcessingCapacity(notification);
//    }
//
//    @Test
//    void resetRestaurantsProcessingCapacity_shouldUpdateRestaurantCurrentLoad() throws InterruptedException {
//        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
//
//        notificationService.resetRestaurantsProcessingCapacity(notification);
//
//        verify(restaurantRepository).save(argThat(r -> r.getCurrentLoad() == 5));
//    }
//
//    @Test
//    void resetRestaurantsProcessingCapacity_shouldThrowExceptionWhenRestaurantNotFound() {
//        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(FoodOrderSystemException.class, () -> notificationService.resetRestaurantsProcessingCapacity(notification));
//    }
//
//    @Test
//    void resetRestaurantsProcessingCapacity_shouldThrowExceptionWhenCurrentLoadBecomesNegative() {
//        restaurant.setCurrentLoad(3);
//        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
//
//        assertThrows(IllegalStateException.class, () -> notificationService.resetRestaurantsProcessingCapacity(notification));
//    }
//
//    @Test
//    void resetRestaurantsProcessingCapacity_shouldRetryOnOptimisticLockException() {
//        when(restaurantRepository.findById(1L))
//            .thenReturn(Optional.of(restaurant))
//            .thenThrow(OptimisticLockingFailureException.class)
//            .thenReturn(Optional.of(restaurant));
//
//        notificationService.resetRestaurantsProcessingCapacity(notification);
//
//        verify(restaurantRepository, times(3)).findById(1L);
//    }
//
//    @Test
//    void recover_shouldThrowIllegalStateException() {
//        assertThrows(IllegalStateException.class, () -> notificationService.recover(new OptimisticLockException("Test"), notification));
//    }
//}