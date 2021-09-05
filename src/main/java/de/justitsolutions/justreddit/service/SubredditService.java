package de.justitsolutions.justreddit.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import de.justitsolutions.justreddit.dto.SubredditDto;
import de.justitsolutions.justreddit.exception.JustRedditException;
import de.justitsolutions.justreddit.mapper.SubredditMapper;
import de.justitsolutions.justreddit.model.Subreddit;
import de.justitsolutions.justreddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
//@Slf4j //logger
public class SubredditService {

	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;
    private final AuthService authService;
	
	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subredditToSave = subredditMapper.mapDtoToSubreddit(subredditDto);
		subredditToSave.setCreationDate(Instant.now());
		subredditToSave.setUser(authService.getCurrentUser());
		Subreddit savedSubreddit = subredditRepository.save(subredditToSave);
		subredditDto.setId(savedSubreddit.getSubredditId());
		return subredditDto;
	}
	
	@Transactional(readOnly = true)
	public List<SubredditDto> getAll() {
		return subredditRepository.findAll()
		.stream()
		.map(subredditMapper::mapSubredditToDto)
		.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public SubredditDto getSubreddit(Long id) {
		Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new JustRedditException("Subreddit corresponding to ID not found"));
		return subredditMapper.mapSubredditToDto(subreddit);
	}
}
