package com.funder.janjoonweb.ui.mb;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJRequirement;

@RooSerializable
@RooJsfManagedBean(entity = JJRequirement.class, beanName = "jJRequirementBean")
public class JJRequirementBean {
	
	private JJRequirement myJJRequirement;

	public JJRequirement getMyJJRequirement() {
		System.out.println("get invoked");
		return myJJRequirement;
		
	}

	public void setMyJJRequirement(JJRequirement myJJRequirement) {
		this.myJJRequirement = myJJRequirement;
	}
	
	public void createNewJJRequirement(){
	
		myJJRequirement = new JJRequirement();
		System.out.println("bean created");
	}
	
	public void  myPersist(){
		
		jJRequirementService.saveJJRequirement(myJJRequirement);
		
		System.out.println("save done");
		findAllJJRequirements();
	}
	
	
	
	
}
