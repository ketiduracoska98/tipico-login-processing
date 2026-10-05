package org.example.trackloginservice.controller;

import jakarta.validation.Valid;
import org.example.trackloginservice.model.LoginEvent;
import org.example.trackloginservice.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	private final LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(@Valid @RequestBody LoginEvent loginEvent) {
		log.info("Received login event: {}", loginEvent);
		loginService.saveLoginEvent(loginEvent);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/getLogin/{messageId}")
	public ResponseEntity<LoginEvent> getLogin(@PathVariable UUID messageId) {
		log.info("Received getLogin for messageId: {}", messageId);
		return loginService.getLogin(messageId).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
