package org.example.trackloginservice.model;

import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LoginEvent {

	@NotNull
	private UUID customerId;

	@NotBlank
	private String username;

	@NotNull
	private Client client;

	@NotNull
	private Instant timestamp;

	@NotNull
	private UUID messageId;

	@NotBlank
	private String customerIp;

}
