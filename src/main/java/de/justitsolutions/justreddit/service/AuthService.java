package de.justitsolutions.justreddit.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.justitsolutions.justreddit.dto.AuthenticationResponse;
import de.justitsolutions.justreddit.dto.LoginRequest;
import de.justitsolutions.justreddit.dto.RegisterRequest;
import de.justitsolutions.justreddit.exception.JustRedditException;
import de.justitsolutions.justreddit.model.NotificationEmail;
import de.justitsolutions.justreddit.model.User;
import de.justitsolutions.justreddit.model.VerificationToken;
import de.justitsolutions.justreddit.repository.UserRepository;
import de.justitsolutions.justreddit.repository.VerificationTokenRepository;
import de.justitsolutions.justreddit.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j //logger
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	
	@Transactional
	public void signup(RegisterRequest registerRequest) {
		if(userRepository.existsByEmail(registerRequest.getEmail())) {
			log.error("Email already exists");
			throw new JustRedditException("Email already exists");
	    }
		
		if(userRepository.existsByUsername(registerRequest.getUsername())) {
			log.error("Username already exists");
			throw new JustRedditException("Username already exists");
	    }
		
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setActivated(false);
		user.setCreationDate(Instant.now());
		userRepository.save(user);
		
		String token = generateVerificationToken(user);
		NotificationEmail tokenEmail = new NotificationEmail(
				"Bitte aktivieren Sie ihr JustReddit Konto", registerRequest.getEmail(),
				"Vielen Dank für Ihre Registrierung bei JustReddit, " + registerRequest.getUsername() + "! <br/>"
				+ "Bitte klicken Sie auf den untenstehenden Link, um Ihr Konto bei JustReddit zu aktivieren: <br/>"
				+ "<a href=\"http://localhost:8080/api/auth/accountVerification/" + token + "\">Konto aktivieren</a> <br/> <br/> <br/>"
				+ "Falls Sie den obenstehenden Link nicht sehen oder anklicken können, geben Sie bitte folgende Adresse in Ihren Webbrowser ein: <br/>"
				+ "http://localhost:8080/api/auth/accountVerification/" + token);
		//Ist Async für schnellere REST Response Time
		mailService.sendMail(tokenEmail);
		log.info("User registration successful");
	}

	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationTokenRepository.save(verificationToken);
		return token;
	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new JustRedditException("Invalid token"));
		fetchUserAndEnable(verificationToken.get());
		log.info("User activated successfully");
	}

	@Transactional //editiert Daten in DB
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		//hier nicht mit optional arbeiten
		User user = userRepository.findByUsername(username).orElseThrow(() -> new JustRedditException("User corresponding to token not found"));
		if(user.isActivated()) {
			log.error("Token already used/User already activated");
			throw new JustRedditException("Token already used/User already activated");
		}
		user.setActivated(true);
		user.setActivationDate(Instant.now());
		userRepository.save(user);
	}
	
	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		return new AuthenticationResponse(token, loginRequest.getUsername());
	}
	

	@Transactional(readOnly = true)
	public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found - " + principal.getUsername()));
    }
}
