package de.justitsolutions.justreddit.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@NotBlank(message = "Username is required")
	@Column(unique = true)
	private String username;
	
	@NotBlank(message = "Password is required")
	private String password;
	
	@Email(message = "Invalid Email")
	@NotEmpty(message = "Email is required")
	@Column(unique = true)
	private String email;
	
	private Instant creationDate;
	private boolean activated;
	private Instant activationDate;
}
