package com.example.project1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import com.example.project1.bean.OrderRequest;
import com.example.project1.bean.OrderResponse;
import com.example.project1.entity.Order;
import com.example.project1.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

	@MockBean
	private OrderService orderService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	@Test
	void shouldReturnOrderList() throws Exception {
		OrderResponse orderResponse1=new OrderResponse();
		orderResponse1.setUserId(11L);
		orderResponse1.setStatus("CREATED");
		orderResponse1.setAmount(212D);
		OrderResponse orderResponse2=new OrderResponse();
		orderResponse2.setUserId(11L);
		orderResponse2.setStatus("CREATED");
		orderResponse2.setAmount(212D);
		List<OrderResponse> orders =new ArrayList<>();
		orders.add(orderResponse1);
		orders.add(orderResponse2);

		when(orderService.findAllOrder()).thenReturn(orders);

		mockMvc.perform(get("/order"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()").value(orders.size()))
			.andExpect(jsonPath("$[0].id").value(orders.get(0).getId()))
			.andExpect(jsonPath("$[0].status").value(orders.get(0).getStatus()))
			.andExpect(jsonPath("$[0].userId").value(orders.get(0).getUserId()))
			.andExpect(jsonPath("$[0].amount").value(orders.get(0).getAmount()))
			.andExpect(jsonPath("$[1].id").value(orders.get(1).getId()))
			.andExpect(jsonPath("$[1].status").value(orders.get(1).getStatus()))
			.andExpect(jsonPath("$[1].userId").value(orders.get(1).getUserId()))
			.andExpect(jsonPath("$[1].amount").value(orders.get(1).getAmount()))
			.andDo(System.out::println);
	}


	@Test
	void shouldCreateNewOrder() throws Exception {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setUserId(999L);
		orderRequest.setAmount(232D);

		OrderResponse orderResponse=new OrderResponse();
		orderResponse.setUserId(11L);
		orderResponse.setStatus("CREATED");
		orderResponse.setAmount(212D);

		when(orderService.addOrder(any(OrderRequest.class))).thenReturn(orderResponse);

		mockMvc.perform(post("/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(orderResponse.getId()))
			.andExpect(jsonPath("$.status").value(orderResponse.getStatus()))
			.andExpect(jsonPath("$.userId").value(orderResponse.getUserId()))
			.andExpect(jsonPath("$.amount").value(orderResponse.getAmount()))
			.andDo(System.out::println);
	}

	@Test
	void shouldReturnOrderById() throws Exception {
		long orderId = 1L;
		OrderResponse orderResponse=new OrderResponse();
		orderResponse.setUserId(11L);
		orderResponse.setStatus("CREATED");
		orderResponse.setAmount(212D);

		when(orderService.findById(orderId)).thenReturn(orderResponse);

		mockMvc.perform(get("/order/{id}", orderId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(orderResponse.getId()))
			.andExpect(jsonPath("$.status").value(orderResponse.getStatus()))
			.andExpect(jsonPath("$.userId").value(orderResponse.getUserId()))
			.andExpect(jsonPath("$.amount").value(orderResponse.getAmount()))
			.andDo(System.out::println);
	}

	@Test
	void shouldUpdateOrderStatus() throws Exception {
		long orderId = 1L;
		Order.Status newStatus = Order.Status.IN_DELIVERY;
		OrderResponse orderResponse=new OrderResponse();
		orderResponse.setUserId(11L);
		orderResponse.setStatus("CREATED");
		orderResponse.setAmount(212D);

		when(orderService.updateOrderStatus(orderId, newStatus)).thenReturn(orderResponse);

		mockMvc.perform(put("/order/{id}/status", orderId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newStatus)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(orderResponse.getId()))
			.andExpect(jsonPath("$.status").value(orderResponse.getStatus()))
			.andExpect(jsonPath("$.userId").value(orderResponse.getUserId()))
			.andExpect(jsonPath("$.amount").value(orderResponse.getAmount()))
			.andDo(System.out::println);
	}
}
