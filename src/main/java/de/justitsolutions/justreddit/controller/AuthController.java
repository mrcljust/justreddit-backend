package de.justitsolutions.justreddit.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.justitsolutions.justreddit.dto.AuthenticationResponse;
import de.justitsolutions.justreddit.dto.LoginRequest;
import de.justitsolutions.justreddit.dto.RefreshTokenRequest;
import de.justitsolutions.justreddit.dto.RegisterRequest;
import de.justitsolutions.justreddit.exception.JustRedditException;
import de.justitsolutions.justreddit.service.AuthService;
import de.justitsolutions.justreddit.service.RefreshTokenService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		try {
			authService.signup(registerRequest);
			return new ResponseEntity<>("User registration successful", HttpStatus.OK);
		}
		catch(JustRedditException ex) {
			return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token) {
		try {
			authService.verifyAccount(token);
			return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
		}
		catch(JustRedditException ex) {
			return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
		/*try {
			authService.login(loginRequest);
			return new ResponseEntity<>("Login successful", HttpStatus.OK);
		}
		catch(JustRedditException | UsernameNotFoundException ex) {
			return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}*/
	}
	
    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!!");
    }
}
