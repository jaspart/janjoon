package com.starit.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import org.hibernate.persister.entity.Loadable;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJTeststep;

@RooSerializable
@RooJsfManagedBean(entity = JJTeststep.class, beanName = "jJTeststepBean")
public class JJTeststepBean {

	private JJTeststep teststep;

	private List<JJTeststep> teststeps;

	private boolean rendered;

	public JJTeststep getTeststep() {
		return teststep;
	}

	public void setTeststep(JJTeststep teststep) {
		this.teststep = teststep;
	}

	public List<JJTeststep> getTeststeps() {
		return teststeps;
	}

	public void setTeststeps(List<JJTeststep> teststeps) {
		this.teststeps = teststeps;
	}

	public boolean getRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public void loadData() {
		System.out.println("load data in teststep");
		teststep = new JJTeststep();
		
		rendered = false;
	}

	public void newTeststep() {
		teststep = new JJTeststep();
		teststep.setCreationDate(new Date());
		teststep.setEnabled(true);
		teststep.setActionstep("Insert an action");
		teststep.setResultatstep("Insert a result");
		rendered = true;
		
		System.out.println("toto");
	}

}
