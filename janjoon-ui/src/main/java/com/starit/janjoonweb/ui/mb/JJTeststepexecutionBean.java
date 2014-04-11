package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJTeststepexecution;

@RooSerializable
@RooJsfManagedBean(entity = JJTeststepexecution.class, beanName = "jJTeststepexecutionBean")
public class JJTeststepexecutionBean {

	@Autowired
	JJBugService jJBugService;

	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	private JJTeststepexecution teststepexecution;

	private List<JJTeststepexecution> teststepexecutions;

	private JJBug bug;

	private int activeIndex;

	private boolean disabled;

	private boolean disabledTestcase;

	private boolean disabledNext;

	private Boolean status;

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

	public boolean getDisabledTestcase() {
		return disabledTestcase;
	}

	public void setDisabledTestcase(boolean disabledTestcase) {
		this.disabledTestcase = disabledTestcase;
	}

	public boolean getDisabledNext() {
		return disabledNext;
	}

	public void setDisabledNext(boolean disabledNext) {
		this.disabledNext = disabledNext;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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

		this.teststepexecutions = new ArrayList<JJTeststepexecution>();

		for (JJTeststepexecution teststepexecution : teststepexecutions) {
			elements.put(teststepexecution.getTeststep().getOrdering(),
					teststepexecution);
		}

		for (Map.Entry<Integer, JJTeststepexecution> teststepexecutionEntry : elements
				.entrySet()) {
			this.teststepexecutions.add(teststepexecutionEntry.getValue());
		}

		activeIndex = -1;

		for (int i = 0; i < this.teststepexecutions.size(); i++) {
			if (teststepexecutions.get(i).getPassed() == null) {
				activeIndex = i;
				break;
			}
		}

		elements = new TreeMap<Integer, JJTeststepexecution>();

		// Mapping between index in the Tab and teststep
		for (int i = 0; i < this.teststepexecutions.size(); i++) {
			elements.put(i, teststepexecutions.get(i));
		}

		if (activeIndex != -1) {
			status = null;
			disabled = true;
			newBug();
			teststepexecution = elements.get(activeIndex);
		} else {
			activeIndex = elements.size() - 1;

			teststepexecution = elements.get(activeIndex);

			status = teststepexecution.getPassed();

			if (status) {
				newBug();
			} else {

				JJBuildBean jJBuildBean = (JJBuildBean) session
						.getAttribute("jJBuildBean");

				JJProjectBean jJProjectBean = (JJProjectBean) session
						.getAttribute("jJProjectBean");

				List<JJBug> bugs = jJBugService.getBugs(
						jJProjectBean.getProject(),
						teststepexecution.getTeststep(),
						jJBuildBean.getBuild(), true, true);

				bug = bugs.get(0);
			}

			disabled = status;

		}

		if (activeIndex != elements.size() - 1) {
			disabledTestcase = true;
		} else {
			disabledTestcase = false;
		}

		disabledNext = !disabledTestcase;

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
		bug.setName("Insert a bug name");
		bug.setDescription("Insert a bug description");
		bug.setEnabled(true);

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJBuildBean jJBuildBean = (JJBuildBean) session
				.getAttribute("jJBuildBean");

		JJProjectBean jJProjectBean = (JJProjectBean) session
				.getAttribute("jJProjectBean");

		bug.setBuild(jJBuildBean.getBuild());
		bug.setProject(jJProjectBean.getProject());

	}

	public void toto() {
		System.out.println("dfgdfgfg");
		System.out.println(status);
		teststepexecution.setPassed(status);
		System.out.println(teststepexecution.getPassed());
		disabled = teststepexecution.getPassed();
	}

	public void onTabChange(TabChangeEvent event) {
		System.out.println("Active Tab: " + event.getTab().getTitle());
		System.out.println("onTabChange tabIndex " + activeIndex);

		if (activeIndex != elements.size() - 1) {
			disabledTestcase = true;
		} else {
			disabledTestcase = false;
		}
		disabledNext = !disabledTestcase;

		teststepexecution = elements.get(activeIndex);

		status = teststepexecution.getPassed();

		System.out.println("name " + teststepexecution.getName());
		System.out.println("status " + status);

		if (status != null) {

			if (status) {
				newBug();
			} else {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJBuildBean jJBuildBean = (JJBuildBean) session
						.getAttribute("jJBuildBean");

				JJProjectBean jJProjectBean = (JJProjectBean) session
						.getAttribute("jJProjectBean");

				List<JJBug> bugs = jJBugService.getBugs(
						jJProjectBean.getProject(),
						teststepexecution.getTeststep(),
						jJBuildBean.getBuild(), true, true);

				bug = bugs.get(0);

				System.out.println("bug.getName() " + bug.getName());
			}

			disabled = status;

		} else {

			disabled = true;
			newBug();
		}

		System.out.println("tabIndex " + activeIndex);
	}

	public void nextTab() {

		System.out.println("begin next");

		// Traiter le teststep précedent

		System.out.println("old activeIndex " + activeIndex);

		teststepexecution = elements.get(activeIndex);
		status = teststepexecution.getPassed();
		
		
		Boolean status1 = teststepexecution.getPassed();

		JJTeststepexecution tse = jJTeststepexecutionService
				.findJJTeststepexecution(teststepexecution.getId());
		tse.setPassed(status1);
		tse.setUpdatedDate(new Date());
		
		

		System.out.println("yy");
		jJTeststepexecutionService.updateJJTeststepexecution(tse);
		System.out.println("ooo");

		System.out.println("name " + tse.getName());
		System.out.println("status " + status);

		if (status1 != null && status1 == false) {
			if (bug.getId() == null) {

				System.out.println("bug.getId() == null");

				bug.setCreationDate(new Date());

				JJTeststep teststep = jJTeststepService
						.findJJTeststep(tse.getTeststep().getId());

				bug.setTeststep(teststep);
				teststep.getBugs().add(bug);
				jJBugService.saveJJBug(bug);

			} else {
				System.out.println("bug.getId() != null");

				bug.setUpdatedDate(new Date());
				jJBugService.updateJJBug(bug);
			}
		}

		System.out.println("Heeeere");

		activeIndex++;

		System.out.println("new activeIndex " + activeIndex);

		// Charger le teststep courant

		teststepexecution = elements.get(activeIndex);

		status = teststepexecution.getPassed();

		System.out.println("name++ " + teststepexecution.getName());
		System.out.println("status++ " + status);

		if (status != null) {

			if (status) {
				newBug();
			} else {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJBuildBean jJBuildBean = (JJBuildBean) session
						.getAttribute("jJBuildBean");

				JJProjectBean jJProjectBean = (JJProjectBean) session
						.getAttribute("jJProjectBean");

				List<JJBug> bugs = jJBugService.getBugs(
						jJProjectBean.getProject(),
						teststepexecution.getTeststep(),
						jJBuildBean.getBuild(), true, true);

				bug = bugs.get(0);

				System.out.println("bug.getName() " + bug.getName());
			}

			disabled = status;

		} else {

			disabled = true;
			newBug();
		}

		if (activeIndex != elements.size() - 1) {
			disabledTestcase = true;
		} else {
			disabledTestcase = false;
		}
		disabledNext = !disabledTestcase;

		System.out.println("Quit next");

	}

	public void quitTab() {
		System.out.println("In quit Tab");

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseexecutionBean jJTestcaseexecutionBean = (JJTestcaseexecutionBean) session
				.getAttribute("jJTestcaseexecutionBean");

		jJTestcaseexecutionBean.save();

		teststepexecution = elements.get(elements.lastKey());
		Boolean status = teststepexecution.getPassed();

		JJTeststepexecution tse = jJTeststepexecutionService
				.findJJTeststepexecution(teststepexecution.getId());
		tse.setPassed(status);
		tse.setUpdatedDate(new Date());
		jJTeststepexecutionService.updateJJTeststepexecution(tse);

		if (status != null && status == false) {
			if (bug.getId() == null) {
				bug.setCreationDate(new Date());

				JJTeststep teststep = jJTeststepService.findJJTeststep(tse
						.getTeststep().getId());

				bug.setTeststep(teststep);
				teststep.getBugs().add(bug);
				jJBugService.saveJJBug(bug);

			} else {
				bug.setUpdatedDate(new Date());
				jJBugService.updateJJBug(bug);
			}
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("runTestcaseDialogWidget.hide()");
		closeDialog();
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
