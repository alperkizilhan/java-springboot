package com.example.project1.repository;

import com.example.project1.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;


@ActiveProfiles("test")
@SpringBootTest
class OrderRepositoryTest {

	@Autowired
	public OrderRepository orderRepository;

	@BeforeEach
	void setUp() {

	}

	@Test
	void testFindById() {
		// given
		Order expectedOrder = new Order();
		expectedOrder.setStatus(Order.Status.CREATED);
		expectedOrder.setUserId(1111L);
		expectedOrder.setAmount(123D);
		Order save = orderRepository.save(expectedOrder);

		// when
		Optional<Order> actualOrder = orderRepository.findById(save.getId());

		// then
		assertThat(actualOrder).isNotNull();
		assertThat(actualOrder.get().getId()).isEqualTo(expectedOrder.getId());
		assertThat(actualOrder.get().getUserId()).isEqualTo(expectedOrder.getUserId());
		assertThat(actualOrder.get().getAmount()).isEqualTo(expectedOrder.getAmount());
		assertThat(actualOrder.get().getStatus()).isEqualTo(expectedOrder.getStatus());
	}


	@Test
	void testFindAll() {
		// given
		Order order1 = new Order();
		order1.setUserId(11111L);
		order1.setStatus(Order.Status.CREATED);
		order1.setUserId(123L);
		Order order2 = new Order();
		order2.setUserId(22222L);
		order2.setAmount(456D);
		order2.setStatus(Order.Status.CREATED);

		orderRepository.save(order1);
		orderRepository.save(order2);


		// when
		List<Order> actualOrders = orderRepository.findAll();

		// then
		assertThat(actualOrders).isNotNull();
		assertThat(actualOrders).hasSize(2);
		assertThat(actualOrders).extracting(Order::getId, Order::getUserId, Order::getStatus, Order::getAmount).containsExactlyInAnyOrder(
		tuple(order1.getId(),order1.getUserId(),order1.getStatus(), order1.getAmount()),
		tuple(order2.getId(),order2.getUserId(),order2.getStatus(), order2.getAmount())
		);
	}

	@Test
	void testDeleteById() {
		// given

		Order orderToDelete = new Order();
		orderToDelete.setUserId(1999L);
		orderToDelete.setAmount(789D);
		orderToDelete.setStatus(Order.Status.CREATED);

		Order save =orderRepository.save(orderToDelete);


		// when
		Optional<Order> deleteUser = orderRepository.findById(save.getId());
		orderRepository.deleteById(deleteUser.get().getId());

		// then
		assertThat(orderRepository.existsById(save.getId())).isFalse();
	}
}
