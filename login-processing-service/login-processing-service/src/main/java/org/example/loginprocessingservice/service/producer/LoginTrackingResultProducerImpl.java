package org.example.loginprocessingservice.service.producer;

import org.example.loginprocessingservice.config.exception.LoginTrackingResultSerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import org.example.loginprocessingservice.model.LoginTrackingResult;
import org.springframework.kafka.core.KafkaTemplate;

@Component
public class LoginTrackingResultProducerImpl implements LoginTrackingResultProducer {

	private static final Logger log = LoggerFactory.getLogger(LoginTrackingResultProducerImpl.class);

	@Value("${kafka-save-login-event-client-topic}")
	private String clientTopic;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public LoginTrackingResultProducerImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	@Override
	public void sendMessage(LoginTrackingResult trackingResult) {

		String resultMessage;

		try {
			resultMessage = objectMapper.writeValueAsString(trackingResult);
		} catch (Exception e) {
			throw new LoginTrackingResultSerializationException("Could not serialize login tracking result", e);
		}
		log.info("Sending message with key: {} to client topic: {}", trackingResult.getMessageId(), clientTopic);
		kafkaTemplate.send(clientTopic, trackingResult.getMessageId().toString(), resultMessage);
	}

}
