package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJTeststep;

@RooSerializable
@RooJsfManagedBean(entity = JJBug.class, beanName = "jJBugBean")
public class JJBugBean {

	private JJBug bug;
	private JJProject project;

	public JJBug getBug() {
		return bug;
	}

	public void setBug(JJBug bug) {
		this.bug = bug;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public void createJJBug(JJTeststep jJTeststep) {
		JJBug bug = new JJBug();
		bug.setName("BUG NAME");
		bug.setCreationDate(new Date());
		bug.setEnabled(true);
		bug.setDescription("Insert a comment");
		bug.setProject(project);
		bug.setTeststep(jJTeststep);

		jJBugService.saveJJBug(bug);
		bug = jJBugService.findJJBug(bug.getId());

	}
}
