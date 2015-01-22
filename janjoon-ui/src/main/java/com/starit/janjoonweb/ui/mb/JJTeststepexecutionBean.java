package com.starit.janjoonweb.ui.mb;

import java.util.*;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJTeststepexecution;
import com.starit.janjoonweb.domain.JJTestcase;
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

	private boolean disabledTestcase;

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

	public boolean getDisabledTestcase() {
		return disabledTestcase;
	}

	public void setDisabledTestcase(boolean disabledTestcase) {
		this.disabledTestcase = disabledTestcase;
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

				List<JJBug> bugs = jJBugService.getBugs(((LoginBean) LoginBean.findBean("loginBean")).getContact().getCompany()
						,jJProjectBean.getProject(),teststepexecution.getTeststep(),
						jJBuildBean.getBuild(), true, true);
				if (bugs.isEmpty()) {
					newBug();
				} else {
					bug = bugs.get(0);
				}
			}
		}

		changeTestcaseStatus();

		if (activeIndex != elements.size() - 1) {
			disabledTestcase = true;
		} else {
			disabledTestcase = false;
		}

	}

	private void newTeststepexecutions(JJTestcaseexecution testcaseexecution) {

		JJTestcaseexecution tce = jJTestcaseexecutionService
				.findJJTestcaseexecution(testcaseexecution.getId());

		List<JJTeststep> teststeps = jJTeststepService.getTeststeps(
				tce.getTestcase(), true, true);

		for (JJTeststep teststep : teststeps) {

			JJTeststepexecution teststepexecution = new JJTeststepexecution();

			teststepexecution.setName(teststep.getName());
			teststepexecution.setDescription(teststep.getDescription());
			teststepexecution.setEnabled(true);

			teststepexecution.setTeststep(teststep);
			teststepexecution.setPassed(null);

			teststepexecution.setTestcaseexecution(tce);
			tce.getTeststepexecutions().add(teststepexecution);

			saveJJTeststepexecution(teststepexecution);

		}
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

		JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");

		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");

		bug.setBuild(jJBuildBean.getBuild());
		bug.setProject(jJProjectBean.getProject());
		bug.setVersioning(jJVersionBean.getVersion());
		bug.setCategory(jJTestcaseBean.getCategory());
	}

	public void handleStatus() {

		teststepexecution.setPassed(status);
		updateJJTeststepexecution(teststepexecution);
		teststepexecution = jJTeststepexecutionService
				.findJJTeststepexecution(teststepexecution.getId());

		elements.put(activeIndex, teststepexecution);

		if (teststepexecution.getPassed()) {

			nextTab();			
			onTabChange();

		} else {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);

			JJBugBean jJBugBean = (JJBugBean) session.getAttribute("jJBugBean");

			bug.setTeststep(teststepexecution.getTeststep());

			jJBugBean.setJJBug_(bug);
			jJBugBean.setBugRequirementSelected(teststepexecution.getTeststep()
					.getTestcase().getRequirement());
			jJBugBean.setBugVersionSelected(teststepexecution.getTeststep().getTestcase().getBuild()
					.getVersion());
			jJBugBean.setBugProjectSelected(((JJProjectBean) session
					.getAttribute("jJProjectBean")).getProject());

			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('bugTestDialogWidget').show()");
			disabledTestcase=false;
		}

		if (activeIndex == elements.size()) {
			changeTestcaseStatus();
		}
		if(!disabledTestcase)
			RequestContext.getCurrentInstance().execute("remoteButton()");	
	}

	public void changeTestcaseStatus() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseexecutionBean jJTestcaseexecutionBean = (JJTestcaseexecutionBean) session
				.getAttribute("jJTestcaseexecutionBean");

		List<JJTeststepexecution> teststepexecutions = jJTeststepexecutionService
				.getTeststepexecutions(
						jJTestcaseexecutionBean.getTestcaseexecution(), true);

		for (JJTeststepexecution teststepexecution : teststepexecutions) {

			if (teststepexecution.getPassed() != null) {
				if (teststepexecution.getPassed()) {
					jJTestcaseexecutionBean.setStatus(true);
				} else {
					jJTestcaseexecutionBean.setStatus(false);
					break;
				}

			} else {
				jJTestcaseexecutionBean.setStatus(null);
			}

		}

	}

	public void onTabChange() {

		if (activeIndex < elements.size()) {
			disabledTestcase = true;
			teststepexecution = elements.get(activeIndex);

			status = teststepexecution.getPassed();

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

					List<JJBug> bugs = jJBugService.getBugs(((LoginBean) LoginBean.findBean("loginBean")).getContact().getCompany(),
							jJProjectBean.getProject(),
							teststepexecution.getTeststep(),
							jJBuildBean.getBuild(), true, true);

					if (bugs.isEmpty()) {
						newBug();
					} else {
						bug = bugs.get(0);
					}
				}

			} else {
				newBug();
			}
		} else {
			disabledTestcase = false;
			changeTestcaseStatus();
		}

		
	}

	public void nextTab() {
		
		activeIndex=teststepexecutions.size();
		int i=0;
		while(i<teststepexecutions.size())
		{
			if(teststepexecutions.get(i).getPassed() == null)
			{
				activeIndex=i;
				i=teststepexecutions.size();
			}
			i++;
		}
	}

	public void validateTab() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseexecutionBean jJTestcaseexecutionBean = (JJTestcaseexecutionBean) session
				.getAttribute("jJTestcaseexecutionBean");
		jJTestcaseexecutionBean.save();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('runTestcaseDialogWidget').hide()");
		closeDialog();
	}
	
	public void saveJJTeststepexecution(JJTeststepexecution b)
	{
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setCreatedBy(contact);
		b.setCreationDate(new Date());
		jJTeststepexecutionService.saveJJTeststepexecution(b);
	}
	
	public void updateJJTeststepexecution(JJTeststepexecution b)
	{
		JJContact contact=((LoginBean) LoginBean.findBean("loginBean")).getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJTeststepexecutionService.updateJJTeststepexecution(b);
	}

	public void closeDialog() {

		teststepexecution = null;
		teststepexecutions = null;
		elements = null;
		bug = null;

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTestcaseexecutionBean jJTestcaseexecutionBean = (JJTestcaseexecutionBean) session
				.getAttribute("jJTestcaseexecutionBean");

		jJTestcaseexecutionBean.setTestcaseexecution(null);

		JJTestcaseBean jJTestcaseBean = (JJTestcaseBean) session
				.getAttribute("jJTestcaseBean");
		JJTestcase jjTestcase = jJTestcaseBean.getTestcase();
		jJTestcaseBean.createTestcaseTree();
		jJTestcaseBean.setTestcase(jjTestcase);
		jJTestcaseBean.setRendredEmptySelection(false);

	}

}
