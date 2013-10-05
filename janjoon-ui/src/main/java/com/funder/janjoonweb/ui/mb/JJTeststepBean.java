package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.RowEditEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJTeststep;

@RooSerializable
@RooJsfManagedBean(entity = JJTeststep.class, beanName = "jJTeststepBean")
public class JJTeststepBean {

	private JJTeststep jJTeststep = new JJTeststep();

	private JJTeststep deletedjJTeststep;

	private JJTeststep insertedjJTeststep;

	private List<JJTeststep> testStepList = new ArrayList<JJTeststep>();

	public JJTeststep getjJTeststep() {
		return jJTeststep;
	}

	public void setjJTeststep(JJTeststep jJTeststep) {
		this.jJTeststep = jJTeststep;
	}

	public JJTeststep getDeletedjJTeststep() {
		return deletedjJTeststep;
	}

	public void setDeletedjJTeststep(JJTeststep deletedjJTeststep) {
		this.deletedjJTeststep = deletedjJTeststep;
	}

	public JJTeststep getInsertedjJTeststep() {
		return insertedjJTeststep;
	}

	public void setInsertedjJTeststep(JJTeststep insertedjJTeststep) {
		this.insertedjJTeststep = insertedjJTeststep;
	}

	public List<JJTeststep> getTestStepList() {
		return testStepList;
	}

	public void setTestStepList(List<JJTeststep> testStepList) {
		this.testStepList = testStepList;
	}

	public void initTestStepParameter() {

		jJTeststep = new JJTeststep();
		testStepList.removeAll(testStepList);
		deletedjJTeststep = null;
		insertedjJTeststep = null;

	}

	public void addTestStep(ActionEvent actionEvent) {

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(jJTeststep.getActionstep() + " Added !"));
		jJTeststep.setName(jJTeststep.getActionstep());
		jJTeststep.setDescription("TOTO");
		jJTeststep.setCreationDate(new Date());
		jJTeststep.setOrdering(testStepList.size() + 1);
		jJTeststep.setEnabled(true);
		testStepList.add(jJTeststep);

		jJTeststep = new JJTeststep();

	}

	public void deleteTestStep() {

		if (deletedjJTeststep != null) {

			int index = testStepList.indexOf(deletedjJTeststep);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(deletedjJTeststep.getActionstep()
							+ " Deleted !"));
			testStepList.remove(index);

			for (int i = index; i < testStepList.size(); i++) {
				testStepList.get(i).setOrdering(i + 1);
			}

		}
	}

	public void insertTestStep() {

		if (insertedjJTeststep != null) {

			int index = testStepList.indexOf(insertedjJTeststep);

			List<JJTeststep> tmpList = new ArrayList<JJTeststep>();

			for (int i = index; i < testStepList.size(); i++) {
				tmpList.add(testStepList.get(i));
			}

			testStepList.removeAll(tmpList);

			for (JJTeststep jjTeststep : tmpList) {
				jjTeststep.setOrdering(jjTeststep.getOrdering() + 1);
			}

			jJTeststep = new JJTeststep();
			jJTeststep.setName("TOTO");
			jJTeststep.setDescription("TOTO");
			jJTeststep.setCreationDate(new Date());
			jJTeststep.setOrdering(index + 1);
			jJTeststep.setEnabled(true);
			jJTeststep.setActionstep("Insert a value");
			jJTeststep.setResultat("Insert a value");

			testStepList.add(jJTeststep);
			testStepList.addAll(tmpList);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Test Step Inserted !"));

			jJTeststep = new JJTeststep();
		}

	}

	public void editTestStep(RowEditEvent event) {

		FacesMessage msg = new FacesMessage("Test Step Edited",
				((JJTeststep) event.getObject()).getActionstep());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void cancelEditTestStep(RowEditEvent event) {

		FacesMessage msg = new FacesMessage("Test Step edition Cancelled",
				((JJTeststep) event.getObject()).getActionstep());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
