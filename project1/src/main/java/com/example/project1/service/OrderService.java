package com.example.project1.service;

import com.example.project1.bean.OrderRequest;
import com.example.project1.bean.OrderResponse;
import com.example.project1.entity.Order;
import org.springframework.stereotype.Service;
import com.example.project1.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final KafkaService kafkaService;

	public OrderService(OrderRepository orderRepository, KafkaService kafkaService) {
		this.orderRepository = orderRepository;
		this.kafkaService = kafkaService;
	}

	public OrderResponse addOrder(OrderRequest order) {
		Order order1 = convertToOrder(order);
		Order save = orderRepository.save(order1);
		kafkaService.sendData(save);
		return convertToOrderResponse(save);
	}

	public List<OrderResponse> findAllOrder() {
		List<Order> orders = orderRepository.findAll();

		return orders.stream()
			.map(this::convertToOrderResponse)
			.collect(Collectors.toList());
	}


	public OrderResponse findById(Long id) {
		Order order = orderRepository.findById(id).orElse(null);

		if (order != null) {
			return  convertToOrderResponse(order);
		} else {
			return null;
		}
	}
	public OrderResponse updateOrderStatus(Long id, Order.Status newStatus) {
		Optional<Order> orderOptional = orderRepository.findById(id);

		if (orderOptional.isPresent()) {
			Order order = orderOptional.get();
			Order.Status currentStatus = order.getStatus();

			if (isValidStatusChange(currentStatus, newStatus)) {
				order.setStatus(newStatus);
				order.setLastUpdatedAt(LocalDateTime.now());
				Order updatedOrder = orderRepository.save(order);
				kafkaService.sendData(updatedOrder);

				return convertToOrderResponse(updatedOrder);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private boolean isValidStatusChange(Order.Status currentStatus, Order.Status newStatus) {
		if (currentStatus == Order.Status.CREATED) {
			return newStatus == Order.Status.IN_DELIVERY || newStatus == Order.Status.CANCELLED;
		} else if (currentStatus == Order.Status.IN_DELIVERY) {
			return newStatus == Order.Status.DELIVERED || newStatus == Order.Status.CANCELLED;
		} else if (currentStatus == Order.Status.DELIVERED) {
			return newStatus == Order.Status.CANCELLED;
		} else {
			return false;
		}
	}
	private Order convertToOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setStatus(Order.Status.CREATED);
		order.setUserId(orderRequest.getUserId());
		order.setAmount(orderRequest.getAmount());
		order.setCreatedAt(LocalDateTime.now());
		order.setLastUpdatedAt(LocalDateTime.now());
		return order;
	}
	private OrderResponse convertToOrderResponse(Order order) {
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setId(order.getId());
		orderResponse.setStatus(order.getStatus().toString());
		orderResponse.setUserId(order.getUserId());
		orderResponse.setAmount(order.getAmount());
		orderResponse.setCreatedAt(order.getCreatedAt());
		orderResponse.setLastUpdatedAt(order.getLastUpdatedAt());
		return orderResponse;
	}
}
