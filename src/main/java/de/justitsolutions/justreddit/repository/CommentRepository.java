package de.justitsolutions.justreddit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.justitsolutions.justreddit.model.Comment;
import de.justitsolutions.justreddit.model.Post;
import de.justitsolutions.justreddit.model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findByUser(User user);
}
