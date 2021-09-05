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
import de.justitsolutions.justreddit.dto.CommentDto;
import de.justitsolutions.justreddit.service.CommentService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comment/")
@AllArgsConstructor
public class CommentController {
	private final CommentService commentService;
	
	@PostMapping
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
		.body(commentService.save(commentDto));
	}
	
	@GetMapping("by-post/{id}")
	public ResponseEntity<List<CommentDto>> getCommentsForPost(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(commentService.getCommentsByPost(id));
	}
	
	@GetMapping("by-user/{username}")
	public ResponseEntity<List<CommentDto>> getCommentsByUsername(@PathVariable String username) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(commentService.getCommentsByUsername(username));
	}
}
