package de.justitsolutions.justreddit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.justitsolutions.justreddit.model.Post;
import de.justitsolutions.justreddit.model.Subreddit;
import de.justitsolutions.justreddit.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findBySubreddit(Subreddit subreddit);
	List<Post> findByUser(User user);
}
