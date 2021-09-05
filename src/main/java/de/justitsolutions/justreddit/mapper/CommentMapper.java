package de.justitsolutions.justreddit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import de.justitsolutions.justreddit.dto.CommentDto;
import de.justitsolutions.justreddit.model.Comment;
import de.justitsolutions.justreddit.model.Post;
import de.justitsolutions.justreddit.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "creationDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment mapDtoToComment(CommentDto commentsDto, Post post, User user);

    @Mapping(target = "id", source = "commentId")
	@Mapping(target = "username", source = "user.username")
	@Mapping(target = "postId", source = "post.postId")
	CommentDto mapCommentToDto(Comment comment);
}
