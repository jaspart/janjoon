package com.funder.janjoonweb.ui.mb;
import com.funder.janjoonweb.domain.JJBuild;
import com.funder.janjoonweb.domain.JJTestcaseexecution;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJTestcaseexecution.class, beanName = "jJTestcaseexecutionBean")
public class JJTestcaseexecutionBean {
	
	private JJBuild currentBuild;
	

	public JJBuild getCurrentBuild() {
		return currentBuild;
	}

	public void setCurrentBuild(JJBuild currentBuild) {
		this.currentBuild = currentBuild;
	}
}
