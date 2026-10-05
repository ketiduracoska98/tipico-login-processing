package org.example.trackloginservice.service;

import org.example.trackloginservice.model.LoginEvent;

import java.util.Optional;
import java.util.UUID;

public interface LoginService {

	void saveLoginEvent(LoginEvent loginEvent);

	Optional<LoginEvent> getLogin(UUID messageId);
}
