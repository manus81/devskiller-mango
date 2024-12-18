package com.mango.customer.application.slogan;

import com.mango.customer.application.exceptions.UserNotFoundException;
import com.mango.customer.domain.exceptions.MaxSlogansException;
import com.mango.customer.domain.model.User;
import com.mango.customer.domain.ports.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class SloganServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private SloganService sloganService;

	private SloganDto sloganDto;
	private User user;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		sloganDto = new SloganDto();
		sloganDto.setUserId("user123");
		sloganDto.setSlogan("Best product ever!");

		user = new User();
		user.setId("user123");
		user.setName("John Doe");
		user.setCity("New York");
		user.setAddress("123 Street");
		user.setEmail("john.doe@example.com");
		user.addSlogan("Slogan 1");
		user.addSlogan("Slogan 2");
	}

	@Test
	public void testCreateSlogan_UserExists() {
		when(userRepository.findById("user123")).thenReturn(Mono.just(user));
		when(userRepository.save(user)).thenReturn(Mono.just(user));

		Mono<User> result = sloganService.createSlogan(sloganDto);

		StepVerifier.create(result)
			.expectNextMatches(updatedUser ->
				updatedUser.getSlogans().contains("Best product ever!")
			)
			.verifyComplete();

		verify(userRepository).findById("user123");
		verify(userRepository).save(user);
	}

	@Test
	public void testCreateSlogan_UserNotFound() {
		when(userRepository.findById("user123")).thenReturn(Mono.empty());

		Mono<User> result = sloganService.createSlogan(sloganDto);

		StepVerifier.create(result)
			.expectErrorMatches(throwable ->
				throwable instanceof UserNotFoundException &&
					throwable.getMessage().equals("User: user123 not found")
			)
			.verify();

		verify(userRepository).findById("user123");
		verify(userRepository, never()).save(any());
	}

	@Test
	public void testCreateSlogan_MaxSlogansExceeded() {
		user.addSlogan("Slogan 3");

		when(userRepository.findById("user123")).thenReturn(Mono.just(user));

		Mono<User> result = sloganService.createSlogan(sloganDto);

		StepVerifier.create(result)
			.expectErrorMatches(throwable ->
				throwable instanceof MaxSlogansException &&
					throwable.getMessage().equals("You have reached the max slogan possible (3)")
			)
			.verify();

		verify(userRepository).findById("user123");
		verify(userRepository, never()).save(any());
	}
}
