package de.justitsolutions.justreddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubredditDto {
	private Long id;
	private String name;
	private String description;
	private Integer numberOfPosts;
}
