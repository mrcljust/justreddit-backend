package de.justitsolutions.justreddit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.justitsolutions.justreddit.dto.PostRequest;
import de.justitsolutions.justreddit.dto.PostResponse;
import de.justitsolutions.justreddit.service.PostService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {
	private final PostService postService;

	@PostMapping
	public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
		.body(postService.save(postRequest));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(postService.getPost(id));
	}
	
	@GetMapping("/")
	public ResponseEntity<List<PostResponse>> getAllPosts() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(postService.getAllPosts());
	}
	
	@GetMapping("/by-subreddit/{id}")
	public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(postService.getPostsBySubreddit(id));
	}
	
	@GetMapping("/by-user/{username}")
	public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(postService.getPostsByUsername(username));
	}
}
