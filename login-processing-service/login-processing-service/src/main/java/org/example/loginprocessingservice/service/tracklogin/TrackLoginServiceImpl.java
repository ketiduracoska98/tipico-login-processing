package org.example.loginprocessingservice.service.tracklogin;

import lombok.Getter;
import lombok.Setter;
import org.example.loginprocessingservice.config.webclient.TrackLoginWebConfig;
import org.example.loginprocessingservice.model.LoginEvent;
import org.example.loginprocessingservice.model.LoginResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;

@Service
@Getter
@Setter
public class TrackLoginServiceImpl implements  TrackLoginService {

	private static final Logger log = LoggerFactory.getLogger(TrackLoginServiceImpl.class);

	private final WebClient trackLoginWebClient;
	private final TrackLoginWebConfig trackLoginWebConfig;

	public TrackLoginServiceImpl(@Qualifier("trackLoginWebClient") WebClient trackLoginWebClient, TrackLoginWebConfig trackLoginWebConfig) {
		this.trackLoginWebClient = trackLoginWebClient;
		this.trackLoginWebConfig = trackLoginWebConfig;

	}

	@Override
	public LoginResult trackLogin(LoginEvent loginEvent) {
		log.info("Starting POST API call for message id: {}", loginEvent.getMessageId());
		return trackLoginWebClient
				.post()
				.uri(trackLoginWebConfig.getTrackLoginPath())
				.bodyValue(loginEvent)
				.exchangeToMono(response -> {
					if (response.statusCode().value() == 200) {
						return Mono.just(LoginResult.SUCCESSFUL);
					}
					return Mono.error(new RuntimeException("Track login service returned HTTP " + response.statusCode().value()));
				})
				.retryWhen(Retry.backoff(3, Duration.ofMillis(500)))
				.onErrorReturn(LoginResult.UNSUCCESSFUL)
				.block();
	}
}
