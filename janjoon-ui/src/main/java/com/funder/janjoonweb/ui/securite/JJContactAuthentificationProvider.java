package com.funder.janjoonweb.ui.securite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.ui.mb.JJContactBean;


//@Component("contactAuthentificationProvider")
public class JJContactAuthentificationProvider implements
		AuthenticationProvider {	
	
	JJContactBean jJContactBean;	
	

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		
		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        
        JJContact contact=jJContactBean.getContactByEmail(username);   
        
        if (contact == null) {
            throw new BadCredentialsException("Username not found.");
        }
 
        if (!password.equals(contact.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }         
        
        return new UsernamePasswordAuthenticationToken(username, password);
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}


}
