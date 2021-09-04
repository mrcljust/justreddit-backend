package de.justitsolutions.justreddit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import de.justitsolutions.justreddit.dto.PostRequest;
import de.justitsolutions.justreddit.dto.PostResponse;
import de.justitsolutions.justreddit.model.Post;
import de.justitsolutions.justreddit.model.Subreddit;
import de.justitsolutions.justreddit.model.User;
import de.justitsolutions.justreddit.repository.CommentRepository;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
	
    @Autowired
    private CommentRepository commentRepository;
    
	@Mapping(target = "creationDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
	public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

	@Mapping(target = "id", source = "postId")
	@Mapping(target = "subredditName", source = "subreddit.name")
	@Mapping(target = "username", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
	public abstract PostResponse mapToDto(Post post);
	
    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
    	return TimeAgo.using(post.getCreationDate().toEpochMilli());
    }
}
