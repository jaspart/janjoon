package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJTeststep;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJTeststep.class, beanName = "jJTeststepBean")
public class JJTeststepBean {

	private JJTeststep jJTeststep = new JJTeststep();

	private JJTeststep tmpjJTeststep;

	private JJTeststep insertedjJTeststep;

	private JJTeststep selectedJJTeststep;

	private List<JJTeststep> testStepList = new ArrayList<JJTeststep>();

	public JJTeststep getjJTeststep() {
		return jJTeststep;
	}

	public void setjJTeststep(JJTeststep jJTeststep) {
		this.jJTeststep = jJTeststep;
	}

	public JJTeststep getTmpjJTeststep() {
		return tmpjJTeststep;
	}

	public void setTmpjJTeststep(JJTeststep tmpjJTeststep) {
		this.tmpjJTeststep = tmpjJTeststep;
	}

	public JJTeststep getInsertedjJTeststep() {
		return insertedjJTeststep;
	}

	public void setInsertedjJTeststep(JJTeststep insertedjJTeststep) {
		this.insertedjJTeststep = insertedjJTeststep;
	}

	public JJTeststep getSelectedJJTeststep() {
		return selectedJJTeststep;
	}

	public void setSelectedJJTeststep(JJTeststep selectedJJTeststep) {
		this.selectedJJTeststep = selectedJJTeststep;
	}

	public List<JJTeststep> getTestStepList() {
		return testStepList;
	}

	public void setTestStepList(List<JJTeststep> testStepList) {
		this.testStepList = testStepList;
	}

	public void addTestStep(ActionEvent actionEvent) {

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Added " + jJTeststep.getActionstep() + "!"));
		jJTeststep.setName(jJTeststep.getActionstep());
		jJTeststep.setDescription("TOTO");
		jJTeststep.setCreationDate(new Date());
		jJTeststep.setOrdering(testStepList.size() + 1);
		jJTeststep.setEnabled(true);
		testStepList.add(jJTeststep);

		jJTeststep = new JJTeststep();

	}

	public void deleteTestStep() {

		if (tmpjJTeststep != null) {

			int index = testStepList.indexOf(tmpjJTeststep);
			System.out.println("Deleted Index " + index);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage("Deleted " + tmpjJTeststep.getActionstep()
							+ "!"));
			testStepList.remove(index);

			for (int i = index; i < testStepList.size(); i++) {
				testStepList.get(i).setOrdering(i + 1);
			}

		}
	}

	public void insertTestStep() {

		if (insertedjJTeststep != null) {

			System.out.println("insertedjJTeststep.getOrdering() "
					+ insertedjJTeststep.getOrdering());
			int index = testStepList.indexOf(insertedjJTeststep);
			System.out.println("Insert Index " + index);

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

			testStepList.add(jJTeststep);
			testStepList.addAll(tmpList);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Inserted a test Step !"));

			jJTeststep = new JJTeststep();
		}

	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		
		System.out.println("Heeeeere");

//		if (newValue != null && !newValue.equals(oldValue)) {
//			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
//					"Cell Changed", "Old: " + oldValue + ", New:" + newValue);
//			FacesContext.getCurrentInstance().addMessage(null, msg);
//		}
	}

	public void onRowSelect(SelectEvent event) {
		JJTeststep testStep = (JJTeststep) event.getObject();

		// FacesMessage msg = new FacesMessage("JJTeststep Selected "
		// + req.getName(), req.getName());
		// FacesContext.getCurrentInstance().addMessage(null, msg);

	}

}
