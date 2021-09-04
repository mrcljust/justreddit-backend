package de.justitsolutions.justreddit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.justitsolutions.justreddit.model.Subreddit;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Boolean existsByName(String name);
	Optional<Subreddit> findByName(String name);

}
