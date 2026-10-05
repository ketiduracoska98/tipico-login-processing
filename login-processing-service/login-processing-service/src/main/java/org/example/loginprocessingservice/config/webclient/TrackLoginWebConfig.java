package org.example.loginprocessingservice.config.webclient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class TrackLoginWebConfig {

	@Value("${rest.track-login-service.url}")
	private String trackLoginServiceUrl;

	@Value("${rest.track-login-service.track.login.path}")
	private String trackLoginPath;

}
