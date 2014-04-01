package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJTeststepexecution;

@RooSerializable
@RooJsfManagedBean(entity = JJTeststepexecution.class, beanName = "jJTeststepexecutionBean")
public class JJTeststepexecutionBean {

	private List<JJTeststepexecution> teststepexecutions;
	private List<TeststepexecutionStructure> teststepexecutionStructures;

	private List<JJBug> bugs;

	private int activeIndex;

	public List<JJTeststepexecution> getTeststepexecutions() {
		return teststepexecutions;
	}

	public void setTeststepexecutions(
			List<JJTeststepexecution> teststepexecutions) {
		this.teststepexecutions = teststepexecutions;
	}

	public List<TeststepexecutionStructure> getTeststepexecutionStructures() {

		teststepexecutionStructures = new ArrayList<TeststepexecutionStructure>();

		for (JJTeststepexecution teststepexecution : teststepexecutions) {
			TeststepexecutionStructure teststepexecutionStructure = new TeststepexecutionStructure(
					teststepexecution, null, false);
			teststepexecutionStructures.add(teststepexecutionStructure);
		}

		return teststepexecutionStructures;
	}

	public void setTeststepexecutionStructures(
			List<TeststepexecutionStructure> teststepexecutionStructures) {
		this.teststepexecutionStructures = teststepexecutionStructures;
	}

	public List<JJBug> getBugs() {
		return bugs;
	}

	public void setBugs(List<JJBug> bugs) {
		this.bugs = bugs;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public void newTabView(List<JJTeststep> teststeps) {

		teststepexecutions = new ArrayList<JJTeststepexecution>();

		for (JJTeststep teststep : teststeps) {

			JJTeststepexecution teststepexecution = new JJTeststepexecution();

			teststepexecution.setName(teststep.getName());
			teststepexecution.setDescription(teststep.getDescription());
			teststepexecution.setCreationDate(new Date());
			teststepexecution.setEnabled(true);

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJBuildBean jJBuildBean = (JJBuildBean) session
					.getAttribute("jJBuildBean");

			JJBuild build = jJBuildBean.getBuild();
			teststepexecution.setBuild(build);

			teststepexecution.setTeststep(teststep);
			teststepexecution.setPassed(null);
		}

		jJTeststepexecutionService.saveteststepexecutions(teststepexecutions);
	}

	public class TeststepexecutionStructure {

		private JJTeststepexecution teststepexecution;
		private JJBug bug;
		private boolean rendered;

		public TeststepexecutionStructure(
				JJTeststepexecution teststepexecution, JJBug bug,
				boolean rendered) {
			super();
			this.teststepexecution = teststepexecution;
			this.bug = bug;
			this.rendered = rendered;
		}

		public JJTeststepexecution getTeststepexecution() {
			return teststepexecution;
		}

		public void setTeststepexecution(JJTeststepexecution teststepexecution) {
			this.teststepexecution = teststepexecution;
		}

		public JJBug getBug() {
			return bug;
		}

		public void setBug(JJBug bug) {
			this.bug = bug;
		}

		public boolean getRendered() {
			return rendered;
		}

		public void setRendered(boolean rendered) {
			this.rendered = rendered;
		}

	}

}
