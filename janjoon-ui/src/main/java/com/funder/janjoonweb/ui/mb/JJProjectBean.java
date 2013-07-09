package com.funder.janjoonweb.ui.mb;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.ui.mb.converter.JJProjectConverter;

@RooSerializable
@RooJsfManagedBean(entity = JJProject.class, beanName = "jJProjectBean")
public class JJProjectBean {

	private JJProject myJJProject;
	private JJProjectConverter projectConverter = new JJProjectConverter();
	private List<JJProject> myJJProjectList;

	public JJProject getMyJJProject() {
		System.out.println("Getter is invoked");
		return myJJProject;
	}

	public void setMyJJProject(JJProject myJJProject) {
		System.out.println("Setter is invoked");
		this.myJJProject = myJJProject;
	}

	public JJProjectConverter getProjectConverter() {
		return projectConverter;
	}

	public void setProjectConverter(JJProjectConverter projectConverter) {
		this.projectConverter = projectConverter;
	}

	public List<JJProject> getMyJJProjectList() {
		myJJProjectList = jJProjectService.findAllJJProjects();
		return myJJProjectList;
	}

	public void setMyJJProjectList(List<JJProject> myJJProjectList) {
		this.myJJProjectList = myJJProjectList;
	}


	public void handleSelect() {

		if (myJJProject == null)
			System.out.println("NULL");
		else {
			System.out.println(myJJProject.getId());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Project selected: "+myJJProject.getName(), "Selection info");

			FacesContext.getCurrentInstance().addMessage(null, message);
			//RequestContext.getCurrentInstance().update("toto");
		}

	}

}