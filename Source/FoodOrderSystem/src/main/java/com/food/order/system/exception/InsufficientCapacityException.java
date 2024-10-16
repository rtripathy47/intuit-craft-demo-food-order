package com.food.order.system.exception;

public class InsufficientCapacityException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientCapacityException(String message) {
        super(message);
    }
}
