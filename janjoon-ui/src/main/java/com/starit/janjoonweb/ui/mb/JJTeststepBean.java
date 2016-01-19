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
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJTeststep.class, beanName = "jJTeststepBean")
public class JJTeststepBean {

	private JJTeststep teststep;

	private List<JJTeststep> teststeps;

	private boolean actionTeststep;

	@Autowired
	private JJStatusService jJStatusService;

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public JJTeststep getTeststep() {
		return teststep;
	}

	public void setTeststep(JJTeststep teststep) {
		this.teststep = teststep;
	}

	public List<JJTeststep> getTeststeps() {

		teststeps = new ArrayList<JJTeststep>();

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		JJTestcase testcase = jJTestcaseBean.getTestcase();

		if (testcase != null && testcase.getId() != null) {

			teststeps = jJTeststepService.getTeststeps(testcase, true, true);
		}

		return teststeps;
	}

	public void setTeststeps(List<JJTeststep> teststeps) {
		this.teststeps = teststeps;
	}

	public boolean getActionTeststep() {
		return actionTeststep;
	}

	public void setActionTeststep(boolean actionTeststep) {
		this.actionTeststep = actionTeststep;
	}

	public boolean isBugFixed(JJBug bug) {

		return bug.getStatus() != null
				&& bug.getStatus().getName().equalsIgnoreCase("fixed");

	}

	public void onbugCheckStatus(JJBug bug, JJBugBean jJBugBean) {

		bug.setStatus(jJStatusService.getOneStatus("FIXED", "Bug", true));
		jJBugBean.updateJJBug(bug);

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "Bug", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void newTeststep() {

		teststep = new JJTeststep();
		teststep.setCreationDate(new Date());
		teststep.setEnabled(true);

	}

	public void save() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		JJTestcase tc = jJTestcaseService.findJJTestcase(jJTestcaseBean
				.getTestcase().getId());

		if (teststep.getId() == null) {

			manageTeststepOrder(tc);

			teststep.setTestcase(tc);
			// tc.getTeststeps().add(teststep);

			teststep.setName("Teststep for " + tc.getName());

			teststep.setDescription("This is " + teststep.getActionstep() + " "
					+ teststep.getResultstep() + " description");

			saveJJTeststep(teststep);
			newTeststep();

			actionTeststep = true;
		}

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_created", "Teststep", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void insertTestStep() {

		JJTeststep ts = jJTeststepService.findJJTeststep(teststep.getId());

		int ordering = ts.getOrdering();

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

		// teststeps = new HashSet<JJTeststep>();

		for (Map.Entry<Integer, JJTeststep> entry : subElements.entrySet()) {

			JJTeststep tmpTeststep = entry.getValue();

			JJTeststep teststep = jJTeststepService.findJJTeststep(tmpTeststep
					.getId());

			int order = teststep.getOrdering();

			teststep.setOrdering(order + 1);

			// System.out.println("teststep.getName() " + teststep.getName());
			// System.out.println("teststep.getOrdering() "
			// + teststep.getOrdering());

			teststeps.add(teststep);

			updateJJTeststep(teststep);
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

		saveJJTeststep(ts);

		actionTeststep = true;

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Test Step Inserted !"));
	}

	public void initCkeditor(RowEditEvent event) {

		DataTable table = (DataTable) event.getSource();
		int activeRow = table.getRowIndex();
		int lenth = table.getRowCount();

		for (int i = 0; i < lenth; i++)
			if (i != activeRow)
				RequestContext.getCurrentInstance().execute(
						"jQuery('.ui-datatable-data tr').find('span.ui-icon-close').eq("
								+ i
								+ ").each(function(){jQuery(this).click()});");

		RequestContext.getCurrentInstance().execute(
				"PF('testTabView').select(0)");
		RequestContext.getCurrentInstance().execute(
				"PF('testTabView').select(1)");
	}

	public void editTestStep(RowEditEvent event) {

		JJTeststep ts = jJTeststepService.findJJTeststep(((JJTeststep) event
				.getObject()).getId());

		ts.setName(ts.getActionstep() + " " + ts.getResultstep());

		ts.setDescription("This is " + ts.getActionstep() + " "
				+ ts.getResultstep() + " description");

		updateJJTeststep(ts);

		newTeststep();

		actionTeststep = true;

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "Teststep", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public void cancelEditTestStep(RowEditEvent event) {

		// FacesMessage facesMessage = MessageFactory.getMessage("Cancel Edit",
		// "Teststep");
		// FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		newTeststep();

	}

	public void saveJJTeststep(JJTeststep b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJTeststepService.saveJJTeststep(b);
	}

	public void updateJJTeststep(JJTeststep b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJTeststepService.updateJJTeststep(b);
	}

	public void deleteTestStep() {

		JJTeststep ts = jJTeststepService.findJJTeststep(teststep.getId());
		ts.setEnabled(false);
		jJTeststepService.deleteJJTeststep(ts);

		newTeststep();

		actionTeststep = true;

	}

	private void manageTeststepOrder(JJTestcase testcase) {

		SortedMap<Integer, JJTeststep> elements = new TreeMap<Integer, JJTeststep>();

		// JJTestcase tc = jJTestcaseService.findJJTestcase(testcase.getId());

		Set<JJTeststep> teststeps = testcase.getTeststeps();

		// System.out.println("teststeps size " + teststeps.size());

		for (JJTeststep teststep : teststeps) {

			JJTeststep ts = jJTeststepService.findJJTeststep(teststep.getId());

			elements.put(ts.getOrdering(), ts);
		}

		if (elements.isEmpty()) {
			teststep.setOrdering(0);
		} else {
			teststep.setOrdering(elements.lastKey() + 1);
		}

		// System.out.println("teststep.getOrdering() " +
		// teststep.getOrdering());

	}

}
