package org.example.loginprocessingservice;

import org.example.loginprocessingservice.config.exception.LoginTrackingResultSerializationException;
import org.example.loginprocessingservice.model.Client;
import org.example.loginprocessingservice.model.LoginResult;
import org.example.loginprocessingservice.model.LoginTrackingResult;
import org.example.loginprocessingservice.service.producer.LoginTrackingResultProducerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class LoginTrackingResultProducerImplTest {

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private LoginTrackingResultProducerImpl producer;

	@Test
	void shouldSerializeAndSendLoginTrackingResult() throws Exception {

		UUID messageId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

		LoginTrackingResult trackingResult = new LoginTrackingResult
				(UUID.fromString("550e8400-e29b-41d4-a716-446655440007"), "testuser", Client.WEB,
						Instant.parse("2026-10-05T10:00:00Z"), messageId, "127.0.0.1",
						LoginResult.SUCCESSFUL);

		String serializedMessage = "{\"customerId\":\"550e8400-e29b-41d4-a716-446655440007\","
				+ "\"username\":\"testuser\","
				+ "\"client\":\"web\","
				+ "\"timestamp\":\"2026-10-05T10:00:00Z\","
				+ "\"messageId\":\"550e8400-e29b-41d4-a716-446655440000\","
				+ "\"customerIp\":\"127.0.0.1\","
				+ "\"result\":\"successful\"}";

		when(objectMapper.writeValueAsString(trackingResult)).thenReturn(serializedMessage);

		ReflectionTestUtils.setField(producer, "clientTopic", "login-tracking-result");

		producer.sendMessage(trackingResult);

		verify(objectMapper).writeValueAsString(trackingResult);

		verify(kafkaTemplate).send("login-tracking-result", messageId.toString(), serializedMessage);
	}

	@Test
	void shouldThrowExceptionWhenSerializationFails() throws Exception {

		LoginTrackingResult trackingResult = new LoginTrackingResult(
				UUID.fromString("550e8400-e29b-41d4-a716-446655440007"), "testuser", Client.WEB,
				Instant.parse("2026-10-05T10:00:00Z"), UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
				"127.0.0.1", LoginResult.SUCCESSFUL);

		RuntimeException serializationException = new RuntimeException("serialization failed");

		when(objectMapper.writeValueAsString(trackingResult)).thenThrow(serializationException);

		LoginTrackingResultSerializationException exception = assertThrows(LoginTrackingResultSerializationException.class,
						() -> producer.sendMessage(trackingResult));

		assertEquals("Could not serialize login tracking result", exception.getMessage());

		assertSame(serializationException, exception.getCause());

		verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
	}

	@Test
	void shouldUseMessageIdAsKafkaKey() throws Exception {
		UUID customerId = UUID.randomUUID();
		UUID messageId = UUID.randomUUID();

		LoginTrackingResult trackingResult = new LoginTrackingResult(customerId, "testuser", Client.WEB,
				Instant.parse("2026-10-05T10:00:00Z"), messageId, "127.0.0.1", LoginResult.SUCCESSFUL);

		String serializedMessage = "{\"customerId\":\"" + customerId + "\","
				+ "\"username\":\"testuser\","
				+ "\"client\":\"web\","
				+ "\"timestamp\":\"2026-10-05T10:00:00Z\","
				+ "\"messageId\":\"" + messageId + "\","
				+ "\"customerIp\":\"127.0.0.1\","
				+ "\"result\":\"successful\"}";

		when(objectMapper.writeValueAsString(trackingResult)).thenReturn(serializedMessage);

		ReflectionTestUtils.setField(producer, "clientTopic", "login-tracking-result");

		producer.sendMessage(trackingResult);

		verify(kafkaTemplate).send("login-tracking-result", messageId.toString(), serializedMessage);
	}

}
