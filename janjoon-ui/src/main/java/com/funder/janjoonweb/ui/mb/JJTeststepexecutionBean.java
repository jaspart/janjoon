package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJBuild;
import com.funder.janjoonweb.domain.JJTeststep;
import com.funder.janjoonweb.domain.JJTeststepexecution;

@RooSerializable
@RooJsfManagedBean(entity = JJTeststepexecution.class, beanName = "jJTeststepexecutionBean")
public class JJTeststepexecutionBean {

	private JJBuild currentBuild;
	private List<JJTeststepexecution> jJTeststepexecutionList;

	public JJBuild getCurrentBuild() {
		return currentBuild;
	}

	public void setCurrentBuild(JJBuild currentBuild) {
		this.currentBuild = currentBuild;
	}

	public List<JJTeststepexecution> getjJTeststepexecutionList() {
		return jJTeststepexecutionList;
	}

	public void setjJTeststepexecutionList(
			List<JJTeststepexecution> jJTeststepexecutionList) {
		this.jJTeststepexecutionList = jJTeststepexecutionList;
	}

	public void initParameter() {
		jJTeststepexecutionList = new ArrayList<JJTeststepexecution>();
	}

	public void createJJTeststepexecution(JJTeststep jJTeststep) {
		JJTeststepexecution jJTeststepexecution = new JJTeststepexecution();
		jJTeststepexecution.setBuild(currentBuild);
		jJTeststepexecution.setPassed(true);
		jJTeststepexecution.setName(jJTeststep.getName());
		jJTeststepexecution.setDescription("TEST STEP EXECUTION");
		jJTeststepexecution.setCreationDate(new Date());
		jJTeststepexecution.setTeststep(jJTeststep);
		jJTeststepexecution.setEnabled(true);

		jJTeststepexecutionService.saveJJTeststepexecution(jJTeststepexecution);

		jJTeststepexecutionList.add(jJTeststepexecutionService
				.findJJTeststepexecution(jJTeststepexecution.getId()));

	}
	
	public void insertJJTeststepexecution(JJTeststepexecution jJTeststepexecution){
		jJTeststepexecutionList.add(jJTeststepexecution);
	}
}
