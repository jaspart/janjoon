package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJTeststepexecution;

@RooSerializable
@RooJsfManagedBean(entity = JJTeststepexecution.class, beanName = "jJTeststepexecutionBean")
public class JJTeststepexecutionBean {

	private JJTeststepexecution teststepexecution;

	private List<JJTeststepexecution> teststepexecutions;

	private JJBug bug;

	private int activeIndex;
	
	private boolean disabled;

	private SortedMap<Integer, JJTeststepexecution> elements;

	public JJTeststepexecution getTeststepexecution() {
		return teststepexecution;
	}

	public void setTeststepexecution(JJTeststepexecution teststepexecution) {
		this.teststepexecution = teststepexecution;
	}

	public List<JJTeststepexecution> getTeststepexecutions() {
		return teststepexecutions;
	}

	public void setTeststepexecutions(
			List<JJTeststepexecution> teststepexecutions) {
		this.teststepexecutions = teststepexecutions;
	}

	public JJBug getBug() {
		return bug;
	}

	public void setBug(JJBug bug) {
		this.bug = bug;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}


	public void loadTeststepexecution() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseexecutionBean jJTestcaseexecutionBean = (JJTestcaseexecutionBean) session
				.getAttribute("jJTestcaseexecutionBean");

		JJTestcaseexecution testcaseexecution = jJTestcaseexecutionBean
				.getTestcaseexecution();

		List<JJTeststepexecution> teststepexecutions = jJTeststepexecutionService
				.getTeststepexecutions(testcaseexecution, true);

		elements = new TreeMap<Integer, JJTeststepexecution>();

		if (teststepexecutions.isEmpty()) {

			newTeststepexecutions(testcaseexecution);

		}

		teststepexecutions = jJTeststepexecutionService.getTeststepexecutions(
				testcaseexecution, true);
		System.out.println("Herere");

		this.teststepexecutions = new ArrayList<JJTeststepexecution>();

		for (JJTeststepexecution teststepexecution : teststepexecutions) {
			elements.put(teststepexecution.getTeststep().getOrdering(),
					teststepexecution);
		}

		for (Map.Entry<Integer, JJTeststepexecution> teststepexecutionEntry : elements
				.entrySet()) {
			this.teststepexecutions.add(teststepexecutionEntry.getValue());
		}

		System.out.println("bye");

		System.out.println("teststepexecutions 36 : "
				+ this.teststepexecutions.size());

		for (int i = 0; i < this.teststepexecutions.size(); i++) {
			if (teststepexecutions.get(i).getPassed() == null) {
				activeIndex = i;
				break;
			}
		}

		System.out.println("activeIndex " + activeIndex);

		elements = new TreeMap<Integer, JJTeststepexecution>();

		System.out.println("VOVO");
		// Mapping between index in the Tab and teststep
		for (int i = 0; i < this.teststepexecutions.size(); i++) {

			System.out.println("????????????????????");
			System.out.println(teststepexecutions.get(i).getId());
			System.out.println("????????????????????");

			elements.put(i, teststepexecutions.get(i));
		}

		newBug();

		teststepexecution = elements.get(activeIndex);

		disabled = true;
	}

	private void newTeststepexecutions(JJTestcaseexecution testcaseexecution) {

		System.out.println("In new teststepexec");

		JJTestcaseexecution tce = jJTestcaseexecutionService
				.findJJTestcaseexecution(testcaseexecution.getId());

		List<JJTeststep> teststeps = jJTeststepService.getTeststeps(
				tce.getTestcase(), true, true);

		for (JJTeststep teststep : teststeps) {

			JJTeststepexecution teststepexecution = new JJTeststepexecution();

			teststepexecution.setName(teststep.getName());
			teststepexecution.setDescription(teststep.getDescription());
			teststepexecution.setCreationDate(new Date());
			teststepexecution.setEnabled(true);

			teststepexecution.setBuild(tce.getBuild());

			teststepexecution.setTeststep(teststep);
			teststepexecution.setPassed(null);

			teststepexecution.setTestcaseexecution(tce);
			tce.getTeststepexecutions().add(teststepexecution);

			jJTeststepexecutionService
					.saveJJTeststepexecution(teststepexecution);

		}

		System.out.println("End In new teststepexec");
	}

	private void newBug() {
		bug = new JJBug();
		bug.setName(" ");
		bug.setDescription(" ");
		bug.setEnabled(true);
	}

	public void toto() {
		System.out.println("dfgdfgfg");
		System.out.println(teststepexecution.getPassed());
		disabled = teststepexecution.getPassed();
	}
	
	public void closeDialog() {

		System.out.println("in close dialog");

		teststepexecution = null;
		teststepexecutions = null;
		elements = null;
		bug = null;

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseexecutionBean jJTestcaseexecutionBean = (JJTestcaseexecutionBean) session
				.getAttribute("jJTestcaseexecutionBean");

		jJTestcaseexecutionBean.setTestcaseexecution(null);

		System.out.println("end close dialog");
	}

}
