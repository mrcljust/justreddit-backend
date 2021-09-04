package de.justitsolutions.justreddit.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import de.justitsolutions.justreddit.dto.SubredditDto;
import de.justitsolutions.justreddit.model.Post;
import de.justitsolutions.justreddit.model.Subreddit;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
	@Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
	@Mapping(target = "id", source = "subredditId")
	SubredditDto mapSubredditToDto(Subreddit subreddit);

	default Integer mapPosts(List<Post> numberOfPosts) {
		return numberOfPosts.size();
	}
	
	@InheritInverseConfiguration
	@Mapping(target = "posts", ignore = true)
    @Mapping(target = "creationDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "user", ignore = true)
	Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}