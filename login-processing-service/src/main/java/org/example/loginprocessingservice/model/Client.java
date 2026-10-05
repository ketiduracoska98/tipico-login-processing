package org.example.loginprocessingservice.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Client {

	WEB("web"),
	ANDROID("android"),
	IOS("ios");

	@JsonValue
	private final String value;

	public static Client fromValue(String value) {
		return Arrays.stream(values())
				.filter(client -> client.value.equalsIgnoreCase(value))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unknown client: " + value));
	}
}
