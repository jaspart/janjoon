package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJStatusService;
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

	public void cancelEditTestStep(final RowEditEvent event) {

		// FacesMessage facesMessage = MessageFactory.getMessage("Cancel Edit",
		// "Teststep");
		// FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		newTeststep();

	}

	public void deleteTestStep() {

		final JJTeststep ts = jJTeststepService
				.findJJTeststep(teststep.getId());
		ts.setEnabled(false);
		jJTeststepService.deleteJJTeststep(ts);

		newTeststep();

		actionTeststep = true;

	}

	public void editTestStep(final RowEditEvent event) {

		final JJTeststep ts = jJTeststepService
				.findJJTeststep(((JJTeststep) event.getObject()).getId());

		ts.setName(ts.getActionstep() + " " + ts.getResultstep());

		ts.setDescription("This is " + ts.getActionstep() + " "
				+ ts.getResultstep() + " description");

		updateJJTeststep(ts);

		newTeststep();

		actionTeststep = true;

		final FacesMessage facesMessage = MessageFactory
				.getMessage("message_successfully_updated", "Teststep", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public boolean getActionTeststep() {
		return actionTeststep;
	}

	public JJTeststep getTeststep() {
		return teststep;
	}

	public List<JJTeststep> getTeststeps() {

		teststeps = new ArrayList<JJTeststep>();

		final HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);
		final JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		final JJTestcase testcase = jJTestcaseBean.getTestcase();

		if (testcase != null && testcase.getId() != null) {

			teststeps = jJTeststepService.getTeststeps(testcase, true, true);
		}

		return teststeps;
	}

	public void initCkeditor(final RowEditEvent event) {

		if (!((LoginBean) LoginBean.findBean("loginBean")).isMobile()) {
			final DataTable table = (DataTable) event.getSource();
			final int activeRow = table.getRowIndex();
			final int lenth = table.getRowCount();

			for (int i = 0; i < lenth; i++) {
				if (i != activeRow) {
					RequestContext.getCurrentInstance().execute(
							"jQuery('.ui-datatable-data tr').find('span.ui-icon-close').eq("
									+ i
									+ ").each(function(){jQuery(this).click()});");
				}
			}

			RequestContext.getCurrentInstance()
					.execute("PF('testTabView').select(0)");
			RequestContext.getCurrentInstance()
					.execute("PF('testTabView').select(1)");
		}
	}

	public void insertTestStep() {

		JJTeststep ts = jJTeststepService.findJJTeststep(teststep.getId());

		final int ordering = ts.getOrdering();

		final HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);
		final JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		final JJTestcase testcase = jJTestcaseService
				.findJJTestcase(jJTestcaseBean.getTestcase().getId());

		final Set<JJTeststep> teststeps = testcase.getTeststeps();

		final SortedMap<Integer, JJTeststep> elements = new TreeMap<Integer, JJTeststep>();
		for (final JJTeststep teststep : teststeps) {
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

		for (final Map.Entry<Integer, JJTeststep> entry : subElements
				.entrySet()) {

			final JJTeststep tmpTeststep = entry.getValue();

			final JJTeststep teststep = jJTeststepService
					.findJJTeststep(tmpTeststep.getId());

			final int order = teststep.getOrdering();

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

	public boolean isBugFixed(final JJBug bug) {

		return bug.getStatus() != null
				&& bug.getStatus().getName().equalsIgnoreCase("fixed");

	}

	private void manageTeststepOrder(final JJTestcase testcase) {

		final SortedMap<Integer, JJTeststep> elements = new TreeMap<Integer, JJTeststep>();

		// JJTestcase tc = jJTestcaseService.findJJTestcase(testcase.getId());

		final Set<JJTeststep> teststeps = testcase.getTeststeps();

		// System.out.println("teststeps size " + teststeps.size());

		for (final JJTeststep teststep : teststeps) {

			final JJTeststep ts = jJTeststepService
					.findJJTeststep(teststep.getId());

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

	public void newTeststep() {

		teststep = new JJTeststep();
		teststep.setCreationDate(new Date());
		teststep.setEnabled(true);

	}

	public void onbugCheckStatus(final JJBug bug, final JJBugBean jJBugBean) {

		bug.setStatus(jJStatusService.getOneStatus("FIXED", "Bug", true));
		jJBugBean.updateJJBug(bug);

		final FacesMessage facesMessage = MessageFactory
				.getMessage("message_successfully_updated", "Bug", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void save() {

		final HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);
		final JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		final JJTestcase tc = jJTestcaseService
				.findJJTestcase(jJTestcaseBean.getTestcase().getId());

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

		final FacesMessage facesMessage = MessageFactory
				.getMessage("message_successfully_created", "Teststep", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void saveJJTeststep(final JJTeststep b) {
		b.setCreationDate(new Date());
		final JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJTeststepService.saveJJTeststep(b);
	}

	public void setActionTeststep(final boolean actionTeststep) {
		this.actionTeststep = actionTeststep;
	}

	public void setjJStatusService(final JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public void setTeststep(final JJTeststep teststep) {
		this.teststep = teststep;
	}

	public void setTeststeps(final List<JJTeststep> teststeps) {
		this.teststeps = teststeps;
	}

	public void updateJJTeststep(final JJTeststep b) {
		final JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJTeststepService.updateJJTeststep(b);
	}

	public HtmlPanelGrid populateCreatePanel() {
		return null;
	}

	public HtmlPanelGrid populateEditPanel() {
		return null;
	}

	public HtmlPanelGrid populateViewPanel() {
		return null;
	}

}
