package de.justitsolutions.justreddit.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import de.justitsolutions.justreddit.exception.JustRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtProvider {
	
	private KeyStore keyStore;
	
	@PostConstruct
	public void init() {
		try {
			keyStore = KeyStore.getInstance("JKS");
			InputStream resourceAsStream = getClass().getResourceAsStream("/justreddit.jks");
			keyStore.load(resourceAsStream, "secret".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException ex) {
			throw new JustRedditException("Exception occured while loading keystore");
		}
	}
	public String generateToken(Authentication authentication) {
		User principal = (User)authentication.getPrincipal();
		return Jwts.builder()
				.setSubject(principal.getUsername())
				.signWith(getPrivateKey())
				.compact();
	}

	private PrivateKey getPrivateKey() {
		try {
			return (PrivateKey)keyStore.getKey("justreddit", "secret".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
			throw new JustRedditException("Exception occured while retrieving private key from keystore");
		}
	}
	
	public boolean validateToken(String jwt) {
		Jwts.parser()
		.setSigningKey(getPublicKey())
		.parseClaimsJws(jwt);
		
		return true;
	}
	
	private PublicKey getPublicKey() {
		try {
			return keyStore.getCertificate("justreddit").getPublicKey();
		} catch (KeyStoreException ex) {
			throw new JustRedditException("Exception occured while retrieving public key from keystore");
		}
	}
	
	public String getUsernameFromJwt(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(getPublicKey())
				.parseClaimsJws(token)
				.getBody();
		
		return claims.getSubject();
	}
}
