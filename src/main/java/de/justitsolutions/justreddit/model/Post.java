package de.justitsolutions.justreddit.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.validator.constraints.NotBlank;

@Data //getter setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postId;
	
	@NotBlank(message = "Post name is required")
	private String postName;
	
	@Nullable
	private String url;
	
	@Nullable
	@Lob //großer Text
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "subredditId")
	private Subreddit subreddit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private User user;

	private Integer voteCount;
	
	private Instant creationDate;
}
