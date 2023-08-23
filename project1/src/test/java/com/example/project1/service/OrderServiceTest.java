package com.example.project1.service;

import com.example.project1.bean.OrderRequest;
import com.example.project1.bean.OrderResponse;
import com.example.project1.entity.Order;
import com.example.project1.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {


	@Autowired
	private OrderRepository orderRepository;
	@Mock
	private KafkaService kafkaService;

	private OrderService orderService;

	@BeforeEach
	void setUp() {
		orderService= new OrderService(orderRepository, kafkaService);
		doNothing().when(kafkaService).sendData(any()); // hiçbir durumda bu metotu çalışırma
	}

	@Test
	void testAddOrder() {
		// given
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setUserId(1L);
		orderRequest.setAmount(100.0);

		Order expectedOrder = new Order();
		expectedOrder.setStatus(Order.Status.CREATED);
		expectedOrder.setUserId(orderRequest.getUserId());
		expectedOrder.setAmount(orderRequest.getAmount());
		expectedOrder.setCreatedAt(LocalDateTime.now());
		expectedOrder.setLastUpdatedAt(LocalDateTime.now());

		Order save = orderRepository.save(expectedOrder);

		// when
		Optional<Order> actualResponse = orderRepository.findById(save.getId());

		// then

		assertThat(actualResponse).isNotEmpty();
		assertThat(actualResponse.get().getUserId()).isEqualTo(expectedOrder.getUserId());
		assertThat(actualResponse.get().getAmount()).isEqualTo(expectedOrder.getAmount());
		assertThat(actualResponse.get().getStatus()).isEqualTo(expectedOrder.getStatus());

		orderRepository.deleteAll();
	}

	@Test
	void testFindAllOrder() {
		// given
		List<Order> orders = new ArrayList<>();
		Order orderTest1 = new Order();
		orderTest1.setUserId(99L);
		orderTest1.setStatus(Order.Status.CREATED);
		orderTest1.setAmount(123D);
		Order orderTest2 = new Order();
		orderTest2.setUserId(88L);
		orderTest2.setStatus(Order.Status.CREATED);
		orderTest2.setAmount(1234D);

		orderRepository.save(orderTest1);
		orderRepository.save(orderTest2);

		// when
		List<Order> actualResponse = orderRepository.findAll();

		//then
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse).hasSize(2);
		assertThat(actualResponse).extracting(Order::getId, Order::getUserId, Order::getStatus, Order::getAmount).containsExactlyInAnyOrder(
			tuple(orderTest1.getId(),orderTest1.getUserId(),orderTest1.getStatus(), orderTest1.getAmount()),
			tuple(orderTest2.getId(),orderTest2.getUserId(),orderTest2.getStatus(), orderTest2.getAmount())
		);
		orderRepository.deleteAll();
	}

	@Test
	void testFindById() {
		// given
		Long orderId = 999L;
		Order orderTest = new Order();
		orderTest.setUserId(999L);
		orderTest.setStatus(Order.Status.CREATED);
		orderTest.setAmount(232D);

		Order save = orderRepository.save(orderTest);

		// when
		Optional<Order> actualResponse = orderRepository.findById(save.getId());

		// then
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse.get().getId()).isEqualTo(save.getId());
		assertThat(actualResponse.get().getUserId()).isEqualTo(save.getUserId());
		assertThat(actualResponse.get().getAmount()).isEqualTo(save.getAmount());
		assertThat(actualResponse.get().getStatus()).isEqualTo(save.getStatus());
		orderRepository.deleteAll();
	}


	@Test
	void testUpdateOrderStatus() {
		// given
		Order orderTest = new Order();
		orderTest.setUserId(999L);
		orderTest.setStatus(Order.Status.CREATED);
		orderTest.setAmount(232D);
		Order.Status newStatus = Order.Status.IN_DELIVERY;

		Order save = orderRepository.save(orderTest);

		// when
		OrderResponse test = orderService.updateOrderStatus(save.getId(), newStatus);

		// then
		assertNotNull(test);
		assertThat(test.getStatus()).isEqualTo(newStatus.toString());
		orderRepository.deleteAll();
	}

	@Test
	void testWrongStatusCanNotBeUpdated() {

		// given
		Order orderTest = new Order();
		orderTest.setUserId(999L);
		orderTest.setStatus(Order.Status.IN_DELIVERY);
		orderTest.setAmount(232D);
		Order.Status newStatus = Order.Status.CREATED;

		Order save = orderRepository.save(orderTest);
		orderRepository = spy(OrderRepository.class);
		// when
		OrderResponse test = orderService.updateOrderStatus(save.getId(), newStatus);

		// then
		assertNull(test);
		verify(orderRepository, times(0)).save(any()); // order a da ekle
		orderRepository.deleteAll();
	}
}
