package de.justitsolutions.justreddit.model;

import java.time.Instant;
import java.util.List;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subreddit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subredditId;
	
	@NotBlank(message = "Subreddit name is required")
	@Column(unique = true)
	private String name;
	
	@NotBlank(message = "Subreddit description is required")
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<Post> posts;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	private Instant creationDate;
}
