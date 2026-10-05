package org.example.trackloginservice;

import org.example.trackloginservice.entity.LoginEventEntity;
import org.example.trackloginservice.model.Client;
import org.example.trackloginservice.model.LoginEvent;
import org.example.trackloginservice.repository.LoginEventRepository;
import org.example.trackloginservice.service.LoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import java.time.Instant;
import java.util.UUID;
import java.util.Optional;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

	@Mock
	private LoginEventRepository loginEventRepository;

	@InjectMocks
	private LoginServiceImpl loginService;

	@Test
	void shouldSaveLoginEvent() {
		LoginEvent loginEvent = new LoginEvent(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),"testuser", Client.WEB, Instant.parse("2026-10-04T10:00:00Z"),UUID.fromString("550e8400-e29b-41d4-a716-446655440007"),"127.0.0.1");
		loginService.saveLoginEvent(loginEvent);

		verify(loginEventRepository).save(any(LoginEventEntity.class));
	}

	@Test
	void shouldThrowExceptionWhenSavingDuplicateMessageId() {
		LoginEvent loginEvent = new LoginEvent(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
				"testuser", Client.WEB, Instant.parse("2026-10-04T10:00:00Z"),
				UUID.fromString("550e8400-e29b-41d4-a716-446655440007"), "127.0.0.1");

		when(loginEventRepository.save(any(LoginEventEntity.class)))
				.thenThrow(new DataIntegrityViolationException("duplicate key"));

		assertThrows(DataIntegrityViolationException.class, () -> loginService.saveLoginEvent(loginEvent));

		verify(loginEventRepository).save(any(LoginEventEntity.class));
	}

	@Test
	void shouldGetLoginEvent() {

		UUID messageId = UUID.fromString("550e8400-e29b-41d4-a716-446655440007");

		LoginEventEntity loginEventEntity = new LoginEventEntity(messageId, UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
				"testuser", "web", Instant.parse("2026-10-04T10:00:00Z"), "127.0.0.1");

		when(loginEventRepository.findById(messageId)).thenReturn(Optional.of(loginEventEntity));

		Optional<LoginEvent> result = loginService.getLogin(messageId);

		assertTrue(result.isPresent());

		LoginEvent loginEvent = result.get();

		assertEquals(messageId, loginEvent.getMessageId());
		assertEquals("testuser", loginEvent.getUsername());
		assertEquals(Client.WEB, loginEvent.getClient());
		assertEquals("127.0.0.1", loginEvent.getCustomerIp());

		verify(loginEventRepository).findById(messageId);
	}

	@Test
	void shouldReturnEmptyWhenLoginEventDoesNotExist() {
		UUID messageId = UUID.fromString("550e8400-e29b-41d4-a716-446655440007");

		when(loginEventRepository.findById(messageId)).thenReturn(Optional.empty());

		Optional<LoginEvent> result = loginService.getLogin(messageId);

		assertTrue(result.isEmpty());

		verify(loginEventRepository).findById(messageId);
	}
}
