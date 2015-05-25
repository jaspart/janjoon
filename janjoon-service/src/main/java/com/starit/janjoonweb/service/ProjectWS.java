package com.starit.janjoonweb.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJCompanyService;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJProjectService;
import com.starit.janjoonweb.service.entity.Project;

@Component
@Path("/projets")
public class ProjectWS {
    
	@Autowired
	private JJProjectService jJProjectService;
	@Autowired
	private JJContactService jJContactService;
	@Autowired
	private JJCompanyService jJCompanyService;

	public void setjJProjectService(JJProjectService jJProjectService) {
		this.jJProjectService = jJProjectService;
	}
	
	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	public void setjJCompanyService(JJCompanyService jJCompanyService) {
		this.jJCompanyService = jJCompanyService;
	}

	@GET
	@Path("/listeprojets")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Project> getProjects(){
		return Project.getListProjectFromJJProject(jJProjectService.getAdminListProjects());
		
	}
	

}
