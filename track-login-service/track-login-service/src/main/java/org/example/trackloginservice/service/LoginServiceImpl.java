package org.example.trackloginservice.service;

import org.example.trackloginservice.entity.LoginEventEntity;
import org.example.trackloginservice.model.Client;
import org.example.trackloginservice.model.LoginEvent;
import org.example.trackloginservice.repository.LoginEventRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

	private final LoginEventRepository loginEventRepository;

	public LoginServiceImpl(LoginEventRepository loginEventRepository) {
		this.loginEventRepository = loginEventRepository;
	}

	@Override
	public void saveLoginEvent(LoginEvent loginEvent) {
		if (loginEventRepository.existsById(loginEvent.getMessageId())) {
			return;
		}
		LoginEventEntity loginEventEntity = mapToEntity(loginEvent);
		loginEventRepository.save(loginEventEntity);
	}

	@Override
	public Optional<LoginEvent> getLogin(UUID messageId) {
		return loginEventRepository.findById(messageId)
				.map(entity -> new LoginEvent(
						entity.getCustomerId(),
						entity.getUsername(),
						Client.fromValue(entity.getClient()),
						entity.getTimestamp(),
						entity.getMessageId(),
						entity.getCustomerIp()
				));
	}

	private static LoginEventEntity mapToEntity(LoginEvent loginEvent) {
		return new LoginEventEntity(
				loginEvent.getMessageId(),
				loginEvent.getCustomerId(),
				loginEvent.getUsername(),
				loginEvent.getClient().getValue(),
				loginEvent.getTimestamp(),
				loginEvent.getCustomerIp());
	}

}
