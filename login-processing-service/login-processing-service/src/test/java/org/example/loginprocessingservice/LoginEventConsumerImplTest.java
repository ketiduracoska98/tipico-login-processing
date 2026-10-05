package org.example.loginprocessingservice;

import org.example.loginprocessingservice.config.exception.InvalidLoginEventException;
import org.example.loginprocessingservice.config.exception.LoginEventDeserializationException;
import org.example.loginprocessingservice.model.Client;
import org.example.loginprocessingservice.model.LoginEvent;
import org.example.loginprocessingservice.model.LoginResult;
import org.example.loginprocessingservice.model.LoginTrackingResult;
import org.example.loginprocessingservice.service.consumer.LoginEventConsumerImpl;
import org.example.loginprocessingservice.service.producer.LoginTrackingResultProducer;
import org.example.loginprocessingservice.service.tracklogin.TrackLoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginEventConsumerImplTest {

	@Mock
	private TrackLoginServiceImpl trackLoginService;

	@Mock
	private LoginTrackingResultProducer loginTrackingResultProducer;

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private LoginEventConsumerImpl consumer;

	@Test
	void shouldProcessLoginEventSuccessfully() throws Exception {

		UUID customerId = UUID.fromString("550e8400-e29b-41d4-a716-446655440007");
		UUID messageId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

		LoginEvent loginEvent = new LoginEvent(customerId, "testuser", Client.WEB,
				Instant.parse("2026-10-05T10:00:00Z"), messageId, "127.0.0.1");

		String serializedMessage = "{\"customerId\":\"550e8400-e29b-41d4-a716-446655440007\","
				+ "\"username\":\"testuser\","
				+ "\"client\":\"web\","
				+ "\"timestamp\":\"2026-10-05T10:00:00Z\","
				+ "\"messageId\":\"550e8400-e29b-41d4-a716-446655440000\","
				+ "\"customerIp\":\"127.0.0.1\","
				+ "\"result\":\"successful\"}";

		when(objectMapper.readValue(serializedMessage, LoginEvent.class)).thenReturn(loginEvent);

		when(trackLoginService.trackLogin(loginEvent)).thenReturn(LoginResult.SUCCESSFUL);

		consumer.processLogin(serializedMessage);

		verify(objectMapper).readValue(serializedMessage, LoginEvent.class);

		verify(trackLoginService).trackLogin(loginEvent);

		verify(loginTrackingResultProducer).sendMessage(
				new LoginTrackingResult(customerId,
						"testuser", Client.WEB, Instant.parse("2026-10-05T10:00:00Z"), messageId,
						"127.0.0.1", LoginResult.SUCCESSFUL)
		);
	}

	@Test
	void shouldThrowExceptionWhenLoginEventCannotBeDeserialized() throws Exception {

		String message = "invalid-json";

		when(objectMapper.readValue(message, LoginEvent.class)).thenThrow(new RuntimeException("invalid json"));

		LoginEventDeserializationException exception = assertThrows(LoginEventDeserializationException.class,
				() -> consumer.processLogin(message));

		assertEquals("Could not deserialize login event", exception.getMessage());

		verify(trackLoginService, never()).trackLogin(any());
		verify(loginTrackingResultProducer, never()).sendMessage(any());
	}

	@Test
	void shouldThrowExceptionWhenLoginEventIsInvalid() throws Exception {

		LoginEvent loginEvent = new LoginEvent(
				null, "testuser", Client.WEB, Instant.parse("2026-10-05T10:00:00Z"),
				UUID.randomUUID(), "127.0.0.1");

		when(objectMapper.readValue(anyString(), eq(LoginEvent.class))).thenReturn(loginEvent);

		InvalidLoginEventException exception = assertThrows(InvalidLoginEventException.class,
				() -> consumer.processLogin("login-event"));

		assertEquals("Login event contains missing required fields", exception.getMessage());

		verify(trackLoginService, never()).trackLogin(any());
		verify(loginTrackingResultProducer, never()).sendMessage(any());
	}

	@Test
	void shouldSendUnsuccessfulResultWhenTrackingFails() throws Exception {

		UUID customerId = UUID.fromString("550e8400-e29b-41d4-a716-446655440007");
		UUID messageId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

		LoginEvent loginEvent = new LoginEvent(customerId, "testuser", Client.WEB,
				Instant.parse("2026-10-05T10:00:00Z"), messageId, "127.0.0.1");

		when(objectMapper.readValue(anyString(), eq(LoginEvent.class))).thenReturn(loginEvent);

		when(trackLoginService.trackLogin(loginEvent)).thenReturn(LoginResult.UNSUCCESSFUL);

		consumer.processLogin("login-event");

		verify(trackLoginService).trackLogin(loginEvent);

		verify(loginTrackingResultProducer).sendMessage(
				new LoginTrackingResult(customerId, "testuser", Client.WEB,
						Instant.parse("2026-10-05T10:00:00Z"), messageId, "127.0.0.1",
						LoginResult.UNSUCCESSFUL));
	}
}
