package com.starit.janjoonweb.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.service.entity.Contact;

@Component("contactWS")
@Path("/contact")
public class ContactWS {
   
	@Autowired
	private JJContactService jJContacService;

	public void setjJContacService(JJContactService jJContacService) {
		this.jJContacService = jJContacService;
	}
	@GET
	@Path("/listecontact")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContact(){
		return Contact.getListContactFromJJContact(jJContacService.getContacts(true));
	}
}
