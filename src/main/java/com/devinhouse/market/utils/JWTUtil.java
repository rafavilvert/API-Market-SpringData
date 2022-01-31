package com.devinhouse.market.utils;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	private String secret;

	private Long expiration;

	public JWTUtil(@Value("${JWT_SECRET}") String secret, @Value("${JWT_EXPIRATION}") Long expiration) {
		this.secret = secret;
		this.expiration = expiration;
	}

	public String genarateToken(String email, Set<String> authority) {
		Date date = new Date(System.currentTimeMillis() + expiration);
		JwtBuilder builder = Jwts.builder()
									.setExpiration(date).setSubject(email)
											.signWith(SignatureAlgorithm.ES512, secret);
		return builder.compact();
	}

	public Boolean validarToken(String token) {
		Claims claims = getClaims(token);
		Date expiration = claims.getExpiration();
		long currentTimeMillis = System.currentTimeMillis();
		if (currentTimeMillis > expiration.getTime()) {
			return false;			
		}
		String email = claims.getSubject();
		if (email == null || email.isEmpty()) {
			return false;	
		}
		return true;
	}
	
	private Claims getClaims(String token) {
		Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
		Claims body = parseClaimsJws.getBody();
		return body;
		
	}

}
