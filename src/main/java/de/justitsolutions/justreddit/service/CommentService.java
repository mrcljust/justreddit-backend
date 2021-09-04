package de.justitsolutions.justreddit.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import de.justitsolutions.justreddit.dto.CommentDto;
import de.justitsolutions.justreddit.exception.JustRedditException;
import de.justitsolutions.justreddit.mapper.CommentMapper;
import de.justitsolutions.justreddit.model.Comment;
import de.justitsolutions.justreddit.model.NotificationEmail;
import de.justitsolutions.justreddit.model.Post;
import de.justitsolutions.justreddit.model.User;
import de.justitsolutions.justreddit.repository.CommentRepository;
import de.justitsolutions.justreddit.repository.PostRepository;
import de.justitsolutions.justreddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j //logger
public class CommentService {

	private final CommentMapper commentMapper;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final AuthService authService;
	private final MailService mailService;
	
	public CommentDto save(CommentDto commentDto) {
        
		Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new JustRedditException("Corresponding post not found, ID " + commentDto.getPostId().toString()));
		userRepository.findByUsername(commentDto.getUsername()).orElseThrow(() -> new JustRedditException("Corresponding user not found, username " + commentDto.getUsername()));
		
		Optional<User> postOwner = Optional.ofNullable(post.getUser());
		postOwner.orElseThrow(() -> new JustRedditException("Post owner user not found"));
		
		
		Comment commentToSave = commentMapper.mapDtoToComment(commentDto, post, authService.getCurrentUser());
		Comment savedComment = commentRepository.save(commentToSave);
		commentDto.setId(savedComment.getCommentId());
		commentDto.setCreationDate(savedComment.getCreationDate());
		
        NotificationEmail commentEmail = new NotificationEmail("Neuer Kommentar unter Ihrem Beitrag bei JustReddit", post.getUser().getEmail(), post.getUser().getUsername() + " hat einen Kommentar unter Ihrem Post (" + post.getPostName() + ") bei JustReddit gepostet.");// <br/> <br/> <a href=\"" + post.getUrl() + "\">Zum Post</a>");
        //Ist Async für schnellere REST Response Time
  		mailService.sendMail(commentEmail);
  		log.info("Comment added successfully");
        
		return commentDto;
	}

	public List<CommentDto> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new JustRedditException("Corresponding post not found, ID " + postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapCommentToDto)
                .collect(Collectors.toList());
	}

	public List<CommentDto> getCommentsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new JustRedditException("Corresponding user not found, username " + username));
        return commentRepository.findByUser(user)
                .stream()
                .map(commentMapper::mapCommentToDto)
                .collect(Collectors.toList());
	}

}
