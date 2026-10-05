package org.example.loginprocessingservice.model;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LoginEvent {

	private UUID customerId;

	private String username;

	private Client client;

	private Instant timestamp;

	private UUID messageId;

	private String customerIp;

}
