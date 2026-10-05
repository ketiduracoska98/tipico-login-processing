package org.example.loginprocessingservice.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LoginResult {

	SUCCESSFUL("successful"),
	UNSUCCESSFUL("unsuccessful");

	@JsonValue
	private final String value;
}
