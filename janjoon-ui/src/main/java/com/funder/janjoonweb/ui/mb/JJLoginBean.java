package com.funder.janjoonweb.ui.mb;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJContactService;
import com.funder.janjoonweb.domain.JJContactServiceImpl;



@Scope("session")
@Component("jJLoginBean")
public class JJLoginBean
{	
    private AuthenticationManager authenticationManager;
    private String username;
    private String password;
    private JJContact contact;
    private boolean enable=false;

    public  boolean isEnable() {
			
    	return enable;
	}

	public void setEnable(boolean enable) {
			this.enable = enable;
	}

	@Autowired
    public JJLoginBean(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public String login() {
    	
        String s="";
    	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContext sContext = SecurityContextHolder.getContext();
            sContext.setAuthentication(authentication);
            enable=true;
            s= "success";
        } catch (AuthenticationException loginError) {
            FacesContext fContext = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage("Invalid username/password. Reason " + loginError.getMessage());
            fContext.addMessage("login",  message);
            enable=false;
            s= "fail";
        }
       
        return s;
    }

    public String getUsername() {
        enable=!(username==null||username.equals(""));
    	return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public JJContact getContact() {
		return contact;
	}

	public void setContact(JJContact contact) {
		this.contact = contact;
	}

	//authentificationProvider implementation 
	
}
