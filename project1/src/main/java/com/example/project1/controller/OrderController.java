package com.example.project1.controller;

import com.example.project1.bean.OrderRequest;
import com.example.project1.bean.OrderResponse;
import com.example.project1.entity.Order;
import com.example.project1.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController

public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/order")
	public ResponseEntity<List<OrderResponse>> getAllOrder() {
		try {

			List<OrderResponse> orders = orderService.findAllOrder();

			if (orders.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(orders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/order")
	OrderResponse newOrder(@RequestBody OrderRequest newOrder) {
		return orderService.addOrder(newOrder);
	}

	@GetMapping("/order/{id}")
	OrderResponse one(@PathVariable Long id) {
		return orderService.findById(id);
	}


	@PutMapping("/order/{id}/status")
	public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestBody Order.Status status) {
		OrderResponse updatedOrderDTO = orderService.updateOrderStatus(id, status);

		if (updatedOrderDTO != null) {
			return new ResponseEntity<>(updatedOrderDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}

