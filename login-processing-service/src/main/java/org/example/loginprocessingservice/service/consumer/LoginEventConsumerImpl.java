package org.example.loginprocessingservice.service.consumer;

import org.example.loginprocessingservice.config.exception.InvalidLoginEventException;
import org.example.loginprocessingservice.config.exception.LoginEventDeserializationException;
import org.example.loginprocessingservice.model.LoginEvent;
import org.example.loginprocessingservice.model.LoginResult;
import org.example.loginprocessingservice.model.LoginTrackingResult;
import org.example.loginprocessingservice.service.producer.LoginTrackingResultProducer;
import org.example.loginprocessingservice.service.tracklogin.TrackLoginServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class LoginEventConsumerImpl implements LoginEventConsumer {

	private static final Logger log = LoggerFactory.getLogger(LoginEventConsumerImpl.class);

	private final TrackLoginServiceImpl trackLoginService;
	private final LoginTrackingResultProducer loginTrackingResultProducer;
	private final ObjectMapper objectMapper;

	public LoginEventConsumerImpl(TrackLoginServiceImpl trackLoginService, ObjectMapper objectMapper, LoginTrackingResultProducer loginTrackingResultProducer) {
		this.trackLoginService = trackLoginService;
		this.loginTrackingResultProducer = loginTrackingResultProducer;
		this.objectMapper = objectMapper;
	}

	@Override
	@KafkaListener(topics = "${kafka-save-login-event-business-topic}")
	public void processLogin(String msg) {
		log.info("Login Event received: {}", msg);

		LoginEvent loginEvent;

		try {
			loginEvent = objectMapper.readValue(msg, LoginEvent.class);
		} catch (Exception e) {
			throw new LoginEventDeserializationException("Could not deserialize login event", e);
		}
		validateLoginEvent(loginEvent);

		LoginResult loginResult = trackLoginService.trackLogin(loginEvent);
		log.info("LoginResult for message id: {} is: {}", loginEvent.getMessageId(), loginResult.getValue());

		LoginTrackingResult trackingResult = new LoginTrackingResult(
				loginEvent.getCustomerId(),
				loginEvent.getUsername(),
				loginEvent.getClient(),
				loginEvent.getTimestamp(),
				loginEvent.getMessageId(),
				loginEvent.getCustomerIp(),
				loginResult);

		log.info("Sending LoginTrackingResult to client topic");
		loginTrackingResultProducer.sendMessage(trackingResult);

	}

	private void validateLoginEvent(LoginEvent loginEvent) {
		if (loginEvent.getCustomerId() == null
				|| loginEvent.getUsername() == null
				|| loginEvent.getClient() == null
				|| loginEvent.getTimestamp() == null
				|| loginEvent.getMessageId() == null
				|| loginEvent.getCustomerIp() == null) {

			throw new InvalidLoginEventException("Login event contains missing required fields");
		}
	}

}
