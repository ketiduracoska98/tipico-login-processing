package org.example.loginprocessingservice.config.exception;

public class LoginEventDeserializationException extends RuntimeException {
	public LoginEventDeserializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
