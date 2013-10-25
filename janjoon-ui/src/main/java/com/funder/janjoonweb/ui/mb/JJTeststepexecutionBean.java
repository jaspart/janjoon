package com.funder.janjoonweb.ui.mb;

import com.funder.janjoonweb.domain.JJBuild;
import com.funder.janjoonweb.domain.JJTeststepexecution;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJTeststepexecution.class, beanName = "jJTeststepexecutionBean")
public class JJTeststepexecutionBean {

	private JJBuild currentBuild;

	public JJBuild getCurrentBuild() {
		return currentBuild;
	}

	public void setCurrentBuild(JJBuild currentBuild) {
		this.currentBuild = currentBuild;
	}
}
