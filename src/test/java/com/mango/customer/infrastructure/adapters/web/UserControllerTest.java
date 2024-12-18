package com.mango.customer.infrastructure.adapters.web;

import com.mango.customer.application.slogan.SloganDto;
import com.mango.customer.application.slogan.SloganUseCase;
import com.mango.customer.application.user.UserUseCase;
import com.mango.customer.domain.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class UserControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	@Autowired
	private UserUseCase userUseCase;

	@Autowired
	private SloganUseCase sloganUseCase;

	@InjectMocks
	private UserController userController;

	private User user;
	private SloganDto sloganDto;

	@BeforeEach
	public void setUp() {
		user = new User();
		user.setId("user123");
		user.setName("John Doe");
		user.setCity("New York");
		user.setAddress("123 Street");
		user.setEmail("john.doe@example.com");

		sloganDto = new SloganDto();
		sloganDto.setUserId("user123");
		sloganDto.setSlogan("Best product ever!");

		reactiveMongoTemplate.save(user).block();
	}

	@Test
	public void testGetAllUsers() {
		webTestClient.get().uri("/api/user")
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(User.class)
			.hasSize(1)
			.contains(user);
	}

//	@Test
//	public void testGetUserById() {
//		webTestClient.get().uri("/api/user/{userId}", "user123")
//			.exchange()
//			.expectStatus().isOk()
//			.expectBody(User.class)
//			.isEqualTo(user);
//	}

//	@Test
//	public void testGetUserById_UserNotFound() {
//		webTestClient.get().uri("/api/user/{userId}", "user456")
//			.exchange()
//			.expectStatus().isNotFound()
//			.expectBody()
//			.jsonPath("$.message").isEqualTo("User: user456 not found");
//	}

	@Test
	public void testCreateUser() {
		webTestClient.post().uri("/api/user")
			.bodyValue(user)
			.exchange()
			.expectStatus().isCreated()
			.expectBody(User.class)
			.isEqualTo(user);
	}

	@Test
	public void testUpdateUser() {
		user.setName("Updated Name");
		webTestClient.put().uri("/api/user")
			.bodyValue(user)
			.exchange()
			.expectStatus().isOk()
			.expectBody(User.class)
			.isEqualTo(user);
	}

	@Test
	public void testDeleteUser() {
		webTestClient.delete().uri("/api/user/{id}", "user123")
			.exchange()
			.expectStatus().isNoContent();
	}

	@Test
	public void testCreateSlogan() {
		webTestClient.post().uri("/api/user/slogan")
			.bodyValue(sloganDto)
			.exchange()
			.expectStatus().isCreated()
			.expectBody(User.class)
			.value(user -> {
				Assertions.assertThat(user.getSlogans()).contains("Best product ever!");
			});
	}

}
