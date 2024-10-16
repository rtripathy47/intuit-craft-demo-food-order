# intuit-craft-demo-food-order

Food Ordering System

Implement an online food ordering system the details of
1.	All restaurants have menus that list all the items alongside their price.
2.	Users are shown a list of all items available on the system.
3.	Users can select multiple items from this list and proceed to ordering.
4.	An order is deliverable if all the items in the order can be fulfilled by at least one restaurant.
5.	There exists a restaurant selection strategy. This is a configurable tie-breaker strategy which in case multiple restaurants are offering the same item selects one of them for fulfilling an item in an order. Thus there could be a strategy that selects a restaurant that is offering an item at a lower cost and another one could select a restaurant with a higher rating.
6.	Each restaurant has a maximum processing capacity. Beyond that it won’t accept any further item requests until items which are being processed are dispatched.
7.	Each restaurant takes some time to prepare and dispatch food. Once the item is dispatched the restaurant informs the system and the processing capacity reservation for the item is released.
