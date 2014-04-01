package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.RowEditEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJTeststep.class, beanName = "jJTeststepBean")
public class JJTeststepBean {

	private JJTeststep teststep;

	private List<JJTeststep> teststeps;

	private List<JJTeststep> teststepList;

	public JJTeststep getTeststep() {
		return teststep;
	}

	public void setTeststep(JJTeststep teststep) {
		this.teststep = teststep;
	}

	public List<JJTeststep> getTeststeps() {

		// System.out.println("in get list");

		teststeps = new ArrayList<JJTeststep>();

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		JJTestcase testcase = jJTestcaseBean.getTestcase();

		if (testcase.getId() != null) {
			teststeps = jJTeststepService.getTeststeps(testcase, true, true);
		}

		// System.out.println("teststeps.size() " + teststeps.size());

		return teststeps;
	}

	public void setTeststeps(List<JJTeststep> teststeps) {
		this.teststeps = teststeps;
	}

	public List<JJTeststep> getTeststepList() {
		System.out.println("in get list");

		teststepList = new ArrayList<JJTeststep>();

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		JJTestcase testcase = jJTestcaseBean.getTestcase();

		if (testcase.getId() != null) {
			teststepList = jJTeststepService.getTeststeps(testcase, true, true);
		}

		System.out.println("teststeps.size() " + teststepList.size());

		return teststepList;
	}

	public void setTeststepList(List<JJTeststep> teststepList) {
		this.teststepList = teststepList;
	}

	public void newTeststep() {
		System.out.println("New test Step");
		teststep = new JJTeststep();
		teststep.setCreationDate(new Date());
		teststep.setEnabled(true);
		System.out.println("End New test Step");
	}

	public void save() {

		System.out.println("Saving ...");

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		JJTestcase tc = jJTestcaseService.findJJTestcase(jJTestcaseBean
				.getTestcase().getId());

		if (teststep.getId() == null) {

			manageTeststepOrder(tc);

			teststep.setTestcase(tc);
			tc.getTeststeps().add(teststep);

			teststep.setName(teststep.getActionstep() + " "
					+ teststep.getResultstep());

			teststep.setDescription("This is " + teststep.getActionstep() + " "
					+ teststep.getResultstep() + " description");

			jJTeststepService.saveJJTeststep(teststep);

			System.out.println("end saving");

			newTeststep();

		}

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_created", "JJTeststep");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void insertTestStep() {

		System.out.println("Insert a test Step");
		//
		// System.out.println("teststep.getId() " + teststep.getId());

		JJTeststep ts = jJTeststepService.findJJTeststep(teststep.getId());

		int ordering = ts.getOrdering();

		// System.out.println("ordering " + ordering);

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		JJTestcase testcase = jJTestcaseService.findJJTestcase(jJTestcaseBean
				.getTestcase().getId());

		Set<JJTeststep> teststeps = testcase.getTeststeps();

		SortedMap<Integer, JJTeststep> elements = new TreeMap<Integer, JJTeststep>();
		for (JJTeststep teststep : teststeps) {
			elements.put(teststep.getOrdering(), teststep);
		}

		// for (Map.Entry<Integer, JJTeststep> entry : elements.entrySet()) {
		// System.out.println("entry.getValue().getId() "
		// + entry.getValue().getId());
		// }

		// System.out.println("##################################");

		SortedMap<Integer, JJTeststep> subElements = new TreeMap<Integer, JJTeststep>();
		subElements = elements.tailMap(ordering);

		// for (Map.Entry<Integer, JJTeststep> entry : subElements.entrySet()) {
		// System.out.println("entry.getValue().getId() "
		// + entry.getValue().getId());
		// }

		System.out.println("begin boucle");

		// teststeps = new HashSet<JJTeststep>();

		for (Map.Entry<Integer, JJTeststep> entry : subElements.entrySet()) {

			JJTeststep tmpTeststep = entry.getValue();

			JJTeststep teststep = jJTeststepService.findJJTeststep(tmpTeststep
					.getId());

			int order = teststep.getOrdering();

			teststep.setOrdering(order + 1);
			teststep.setUpdatedDate(new Date());

			// System.out.println("teststep.getName() " + teststep.getName());
			// System.out.println("teststep.getOrdering() "
			// + teststep.getOrdering());

			teststeps.add(teststep);

			jJTeststepService.updateJJTeststep(teststep);
			// System.out.println("toto");
		}
		// System.out.println("end boucle");

		newTeststep();

		// jJTeststepService.updateTeststeps(teststeps);

		ts = new JJTeststep();
		ts.setCreationDate(new Date());
		ts.setEnabled(true);
		ts.setOrdering(ordering);

		ts.setTestcase(testcase);
		testcase.getTeststeps().add(ts);

		ts.setActionstep(" ");
		ts.setResultstep(" ");

		ts.setName(ts.getActionstep() + " " + ts.getResultstep());

		ts.setDescription("This is " + ts.getActionstep() + " "
				+ ts.getResultstep() + " ts");

		jJTeststepService.saveJJTeststep(ts);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Test Step Inserted !"));

		System.out.println("End Insert a test Step");

	}

	public void editTestStep(RowEditEvent event) {

		System.out.println("Edit Test step");

		JJTeststep ts = jJTeststepService.findJJTeststep(((JJTeststep) event
				.getObject()).getId());

		ts.setUpdatedDate(new Date());
		ts.setName(ts.getActionstep() + " " + ts.getResultstep());

		ts.setDescription("This is " + ts.getActionstep() + " "
				+ ts.getResultstep() + " description");

		jJTeststepService.updateJJTeststep(ts);

		newTeststep();

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "JJTeststep");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		System.out.println("End Edit Test step");
	}

	public void cancelEditTestStep(RowEditEvent event) {

		FacesMessage facesMessage = MessageFactory.getMessage("Cancel Edit",
				"JJTeststep");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		newTeststep();

	}

	public void deleteTestStep() {
		System.out.println("Delete test Step");

		System.out.println("teststep.getId() " + teststep.getId());

		JJTeststep ts = jJTeststepService.findJJTeststep(teststep.getId());

		ts.setUpdatedDate(new Date());
		ts.setEnabled(false);

		jJTeststepService.deleteJJTeststep(ts);

		newTeststep();

		System.out.println("End Delete test Step");

	}

	private void manageTeststepOrder(JJTestcase testcase) {

		System.out.println("Manage Order");

		SortedMap<Integer, JJTeststep> elements = new TreeMap<Integer, JJTeststep>();

		// JJTestcase tc = jJTestcaseService.findJJTestcase(testcase.getId());

		Set<JJTeststep> teststeps = testcase.getTeststeps();

		// System.out.println("teststeps size " + teststeps.size());

		for (JJTeststep teststep : teststeps) {

			JJTeststep ts = jJTeststepService.findJJTeststep(teststep.getId());

			elements.put(ts.getOrdering(), ts);
		}

		if (elements.isEmpty()) {
			System.out.println("is empty");
			teststep.setOrdering(0);
		} else {
			System.out.println("not empty");
			teststep.setOrdering(elements.lastKey() + 1);
		}

		// System.out.println("teststep.getOrdering() " +
		// teststep.getOrdering());

		System.out.println("end Order");

	}

}
