package de.justitsolutions.justreddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.justitsolutions.justreddit.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
