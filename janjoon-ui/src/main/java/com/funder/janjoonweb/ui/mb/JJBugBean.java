package com.funder.janjoonweb.ui.mb;

import java.util.Date;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJBug;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJTestcase;

@RooSerializable
@RooJsfManagedBean(entity = JJBug.class, beanName = "jJBugBean")
public class JJBugBean {

	private JJBug jJBug;
	private JJProject currentProject;

	public JJBug getjJBug() {
		return jJBug;
	}

	public void setjJBug(JJBug jJBug) {
		this.jJBug = jJBug;
	}

	public JJProject getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(JJProject currentProject) {
		this.currentProject = currentProject;
	}

	public void createJJBug(JJTestcase jJTestcase) {
		JJBug bug = new JJBug();
		bug.setName("BUG NAME");
		bug.setCreationDate(new Date());
		bug.setEnabled(true);
		bug.setDescription("Insert a comment");
		bug.setProject(currentProject);
		bug.setTestcase(jJTestcase);

		jJBugService.saveJJBug(bug);
		jJBug = jJBugService.findJJBug(bug.getId());

	}
}
