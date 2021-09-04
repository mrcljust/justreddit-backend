package de.justitsolutions.justreddit.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import de.justitsolutions.justreddit.dto.PostRequest;
import de.justitsolutions.justreddit.dto.PostResponse;
import de.justitsolutions.justreddit.exception.JustRedditException;
import de.justitsolutions.justreddit.mapper.PostMapper;
import de.justitsolutions.justreddit.model.Post;
import de.justitsolutions.justreddit.model.Subreddit;
import de.justitsolutions.justreddit.model.User;
import de.justitsolutions.justreddit.repository.PostRepository;
import de.justitsolutions.justreddit.repository.SubredditRepository;
import de.justitsolutions.justreddit.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
//@Slf4j //logger
public class PostService {
	private final SubredditRepository subredditRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final PostMapper postMapper;
	
	@Transactional
	public PostResponse save(PostRequest postRequest) {
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
		.orElseThrow(() -> new JustRedditException("Subreddit not found"));
		User currentUser = authService.getCurrentUser();
		
		Post postToSave = postMapper.map(postRequest, subreddit, currentUser);
		Post savedPost = postRepository.save(postToSave);
		
		return postMapper.mapToDto(savedPost);
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsBySubreddit(Long subredditId) {
		Subreddit subreddit = subredditRepository.findById(subredditId)
				.orElseThrow(() -> new JustRedditException("Subreddit not found, id " + subredditId));
		List<Post> posts = postRepository.findBySubreddit(subreddit);
		return posts.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new JustRedditException("User not found, username " + username));
		List<Post> posts = postRepository.findByUser(user);
		return posts.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getAllPosts() {
		return postRepository.findAll()
				.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());

	}

	@Transactional(readOnly = true)
	public PostResponse getPost(Long id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new JustRedditException("Post not found, id " + id.toString()));
		return postMapper.mapToDto(post);
	}
}
