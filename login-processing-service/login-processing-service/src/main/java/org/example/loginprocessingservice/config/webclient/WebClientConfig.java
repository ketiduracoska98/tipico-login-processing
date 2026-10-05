package org.example.loginprocessingservice.config.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("${rest.track-login-service.username}")
	private String trackLoginServiceUsername;

	@Value("${rest.track-login-service.password}")
	private String trackLoginServicePassword;

	@Bean
	public WebClient trackLoginWebClient(WebClient.Builder builder, TrackLoginWebConfig config) {

		return builder
				.baseUrl(config.getTrackLoginServiceUrl())
				.defaultHeaders(headers ->
						headers.setBasicAuth(trackLoginServiceUsername, trackLoginServicePassword))
				.build();
	}
}
