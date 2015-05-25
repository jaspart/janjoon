package com.starit.janjoonweb.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;

@Component
@Path("/authentifier")
public class AuthentificatinWS {
      
	
	@Autowired
	private JJContactService jJContactService;
	@Autowired
	private BCryptPasswordEncoder encoder;

	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}
	
	public void setEncoder(BCryptPasswordEncoder encoder) {
		this.encoder = encoder;
	}

	@POST
	@Path("verification")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String Authentifier(MultivaluedMap<String, String> personParams){
		
		String login =personParams.getFirst("mail");
		String password=personParams.getFirst("password");
		if(login !=null  && password != null){
			JJContact contact =jJContactService.getContactByEmail(login, true);
			if(contact == null){
				return "User not found";
			}else if (!encoder.matches(password, contact.getPassword())){
				return "Wrong Password";
			}
		}else
		   return "erreur";
		   
	return "success";
	}
}
