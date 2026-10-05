package org.example.loginprocessingservice.service.consumer;

public interface LoginEventConsumer {

	void processLogin(String msg);

}
