package com.starit.janjoonweb.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.service.entity.Status;

@Component
@Path("/status")
public class StatusWS {
	
	@Autowired
	private JJStatusService jJStatusService;

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}
	
	@GET
	@Path("/listeStatus")
	@javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
	public Response getStatusName(){
		
		return Status.getListContactFromJJStatut(jJStatusService.getStatus("task", true, null, true));
		
	}
	

}
