package org.example.trackloginservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "login_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginEventEntity {

	@Id
	@Column(name = "message_id", nullable = false)
	private UUID messageId;

	@Column(name = "customer_id", nullable = false)
	private UUID customerId;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "client", nullable = false)
	private String client;

	@Column(name = "timestamp", nullable = false)
	private Instant timestamp;

	@Column(name = "customer_ip", nullable = false)
	private String customerIp;
}
