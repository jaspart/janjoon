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

	public void createJJBug(JJTeststep jJTeststep) {
		JJBug bug = new JJBug();
		bug.setName("BUG NAME");
		bug.setCreationDate(new Date());
		bug.setEnabled(true);
		bug.setDescription("Insert a comment");
		bug.setProject(currentProject);
		bug.setTeststep(jJTeststep);

		jJBugService.saveJJBug(bug);
		jJBug = jJBugService.findJJBug(bug.getId());

	}
}
