package com.starit.janjoonweb.ui.security;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;

@Component("contactAuthentificationProvider")
public class JJContactAuthentificationProvider
		implements
			AuthenticationProvider,
			Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	JJContactService jJContactService;

	@Autowired
	BCryptPasswordEncoder encoder;

	public void setEncoder(BCryptPasswordEncoder encoder) {
		this.encoder = encoder;
	}

	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		JJContact contact = jJContactService.getContactByEmail(username, true);

		if (contact == null) {
			throw new BadCredentialsException("Username not found.");
		}

		if (!encoder.matches(password.trim(), contact.getPassword())) {
			throw new BadCredentialsException("Wrong password.");
		}

		return new UsernamePasswordAuthenticationToken(username, password,
				null);

	}

	@Override
	public boolean supports(Class<?> authentication) {

		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
