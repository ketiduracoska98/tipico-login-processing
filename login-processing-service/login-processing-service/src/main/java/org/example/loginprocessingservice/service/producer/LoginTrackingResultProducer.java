package org.example.loginprocessingservice.service.producer;

import org.example.loginprocessingservice.model.LoginTrackingResult;

public interface LoginTrackingResultProducer {

	void sendMessage(LoginTrackingResult trackingResult);
}
