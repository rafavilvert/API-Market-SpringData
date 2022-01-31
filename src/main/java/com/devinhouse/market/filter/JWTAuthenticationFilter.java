package com.devinhouse.market.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.devinhouse.market.model.transport.CredentialsDTO;
import com.devinhouse.market.model.transport.CustomerDetail;
import com.devinhouse.market.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JWTUtil jwtUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;

	}

	public Authentication attempAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			ServletInputStream inputStream = request.getInputStream();
			CredentialsDTO credentialsDTO = new ObjectMapper().readValue(inputStream, CredentialsDTO.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentialsDTO.getEmail(),
					credentialsDTO.getPassword(), new ArrayList<>()));
		} catch (Exception e) {
			throw new BadCredentialsException(e.getMessage());
		}

	}

	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		CustomerDetail customer = (CustomerDetail) authResult.getPrincipal();
		String genarateToken = jwtUtil.genarateToken(customer.getUsername(), new HashSet<String>());
		
		response.addHeader("Authorization", "Bearer " + genarateToken);
		

	}

}
