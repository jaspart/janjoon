package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJImportance;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyBugDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJBug.class, beanName = "jJBugBean")
public class JJBugBean {

	private JJBug bug;
	private JJBug JJBug_;

	private JJProject bugProjectSelected;
	private JJRequirement bugRequirementSelected;
	private JJVersion bugVersionSelected;

	private JJProject project;
	private LazyBugDataModel bugList;
	private List<JJBug> selectedBugList;
	private SelectItem[] criticityOptions;
	private SelectItem[] importanceOptions;
	private SelectItem[] statusOptions;

	public JJProject getBugProjectSelected() {
		return bugProjectSelected;
	}

	public void setBugProjectSelected(JJProject bugProjectSelected) {
		this.bugProjectSelected = bugProjectSelected;
	}

	public JJRequirement getBugRequirementSelected() {
		return bugRequirementSelected;
	}

	public void setBugRequirementSelected(JJRequirement bugRequirementSelected) {
		this.bugRequirementSelected = bugRequirementSelected;
	}

	public JJVersion getBugVersionSelected() {
		return bugVersionSelected;
	}

	public void setBugVersionSelected(JJVersion bugVersionSelected) {
		this.bugVersionSelected = bugVersionSelected;
	}

	public JJBug getBug() {

		return bug;
	}

	public void setBug(JJBug bug) {
		this.bug = bug;
	}

	public JJBug getJJBug_() {
		if (JJBug_ == null) {
			JJBug_ = new JJBug();
			bugProjectSelected = project;
			bugVersionSelected = LoginBean.getVersion();
			JJBug_.setStatus(jJStatusService.getOneStatus("NEW", "BUG", true));
		}
		return JJBug_;
	}

	public void setJJBug_(JJBug JJBug_) {
		this.JJBug_ = JJBug_;
	}

	public JJProject getProject() {

		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public LazyBugDataModel getBugList() {
		if (bugList == null) {

			bugList = new LazyBugDataModel(jJBugService, project,
					LoginBean.getProduct(), LoginBean.getVersion());
		}

		return bugList;
	}

	public void setBugList(LazyBugDataModel bugList) {

		this.bugList = bugList;
	}

	public List<JJBug> getSelectedBugList() {
		return selectedBugList;
	}

	public void setSelectedBugList(List<JJBug> selectedBugList) {
		this.selectedBugList = selectedBugList;
	}

	public SelectItem[] getcriticityOptions() {
		return criticityOptions;
	}

	public SelectItem[] getImportanceOptions() {

		return importanceOptions;
	}

	public SelectItem[] getStatusOptions() {
		return statusOptions;
	}

	public void initJJBug(ComponentSystemEvent e) {

		if (LoginBean.getProject() == null) {

			if (bugList == null) {
				project = null;
				bugList = new LazyBugDataModel(jJBugService, project,
						LoginBean.getProduct(), LoginBean.getVersion());
				initJJBugTable(LoginBean.getProduct(), LoginBean.getVersion());
			} else if (project != null) {

				project = null;
				bugList = new LazyBugDataModel(jJBugService, project,
						LoginBean.getProduct(), LoginBean.getVersion());
				initJJBugTable(LoginBean.getProduct(), LoginBean.getVersion());
			} else {
				if (bugList.getVersion() != null) {
					if (!bugList.getVersion().equals(LoginBean.getVersion())) {
						bugList = new LazyBugDataModel(jJBugService, project,
								LoginBean.getProduct(), LoginBean.getVersion());
						initJJBugTable(LoginBean.getProduct(),
								LoginBean.getVersion());
					}
				} else if (LoginBean.getVersion() != null) {
					bugList = new LazyBugDataModel(jJBugService, project,
							LoginBean.getProduct(), LoginBean.getVersion());
					initJJBugTable(LoginBean.getProduct(),
							LoginBean.getVersion());
				} else {
					if (bugList.getProduct() != null) {
						if (!bugList.getProduct()
								.equals(LoginBean.getProduct())) {
							bugList = new LazyBugDataModel(jJBugService,
									project, LoginBean.getProduct(),
									LoginBean.getVersion());
							initJJBugTable(LoginBean.getProduct(),
									LoginBean.getVersion());
						}
					} else if (LoginBean.getProduct() != null) {
						bugList = new LazyBugDataModel(jJBugService, project,
								LoginBean.getProduct(), LoginBean.getVersion());
						initJJBugTable(LoginBean.getProduct(),
								LoginBean.getVersion());
					}

				}

			}

		} else if (project != null) {

			if (!project.equals(LoginBean.getProject())) {
				project = LoginBean.getProject();
				bugList = new LazyBugDataModel(jJBugService, project,
						LoginBean.getProduct(), LoginBean.getVersion());
				initJJBugTable(LoginBean.getProduct(), LoginBean.getVersion());
			} else {

				if (bugList.getVersion() != null) {
					if (!bugList.getVersion().equals(LoginBean.getVersion())) {
						bugList = new LazyBugDataModel(jJBugService, project,
								LoginBean.getProduct(), LoginBean.getVersion());
						initJJBugTable(LoginBean.getProduct(),
								LoginBean.getVersion());
					}
				} else if (LoginBean.getVersion() != null) {
					bugList = new LazyBugDataModel(jJBugService, project,
							LoginBean.getProduct(), LoginBean.getVersion());
					initJJBugTable(LoginBean.getProduct(),
							LoginBean.getVersion());
				} else {
					if (bugList.getProduct() != null) {
						if (!bugList.getProduct()
								.equals(LoginBean.getProduct())) {
							bugList = new LazyBugDataModel(jJBugService,
									project, LoginBean.getProduct(),
									LoginBean.getVersion());
							initJJBugTable(LoginBean.getProduct(),
									LoginBean.getVersion());
						}
					} else if (LoginBean.getProduct() != null) {
						bugList = new LazyBugDataModel(jJBugService, project,
								LoginBean.getProduct(), LoginBean.getVersion());
						initJJBugTable(LoginBean.getProduct(),
								LoginBean.getVersion());
					}

				}

			}
		} else {
			project = LoginBean.getProject();
			bugList = new LazyBugDataModel(jJBugService, project,
					LoginBean.getProduct(), LoginBean.getVersion());
			initJJBugTable(LoginBean.getProduct(), LoginBean.getVersion());
		}

	}

	public void deleteMultiple() {
		for (JJBug b : selectedBugList) {
			b.setEnabled(false);
			updateJJBug(b);
		}
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", "Bug");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
	}

	public void deleteSingle() {

		getJJBug_().setEnabled(false);
		updateJJBug(getJJBug_());
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", "Bug");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
	}

	public void initJJBugTable(JJProduct jjProduct, JJVersion jjVersion) {

		List<JJCriticity> criticities = new ArrayList<JJCriticity>();
		List<JJStatus> status = new ArrayList<JJStatus>();
		List<JJImportance> importances = new ArrayList<JJImportance>();

		List<JJBug> data = jJBugService.getBugs(
				(JJCompany) LoginBean.findBean("JJCompany"), project,
				jjProduct, jjVersion);
		for (JJBug b : data) {

			if (b.getCriticity() != null
					&& !listContaines(criticities, b.getCriticity()))
				criticities.add(b.getCriticity());

			if (b.getStatus() != null && !listContaines(status, b.getStatus()))
				status.add(b.getStatus());

			if (b.getImportance() != null
					&& !listContaines(importances, b.getImportance()))
				importances.add(b.getImportance());

		}
		criticityOptions = createFilterOptions(criticities);
		importanceOptions = createFilterOptions(importances);
		statusOptions = createFilterOptions(status);

	}

	private SelectItem[] createFilterOptions(Object objet) {

		List<Object> data = (List<Object>) objet;

		SelectItem[] options = new SelectItem[data.size() + 1];

		options[0] = new SelectItem("", "Select");
		for (int i = 0; i < data.size(); i++) {

			if (data.get(i) instanceof JJCriticity) {
				JJCriticity criticity = (JJCriticity) data.get(i);
				options[i + 1] = new SelectItem(criticity.getName(),
						criticity.getName());
			} else if (data.get(i) instanceof JJStatus) {
				JJStatus status = (JJStatus) data.get(i);
				options[i + 1] = new SelectItem(status.getName(),
						status.getName());

			} else if (data.get(i) instanceof JJImportance) {
				JJImportance importance = (JJImportance) data.get(i);
				options[i + 1] = new SelectItem(importance.getName(),
						importance.getName());
			}

		}
		return options;

	}

	public boolean listContaines(Object objet, Object find) {

		if (find instanceof JJStatus) {
			List<JJStatus> list = (List<JJStatus>) objet;
			int i = 0;
			JJStatus status = (JJStatus) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(status));
				i++;
			}

			return contain;
		} else if (find instanceof JJImportance) {
			List<JJImportance> list = (List<JJImportance>) objet;
			int i = 0;
			JJImportance importance = (JJImportance) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(importance));
				i++;
			}

			return contain;
		} else if (find instanceof JJCriticity) {
			List<JJCriticity> list = (List<JJCriticity>) objet;
			int i = 0;
			JJCriticity criticity = (JJCriticity) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(criticity));
				i++;
			}

			return contain;
		} else
			return false;

	}

	public void createJJBug(JJTeststep jJTeststep) {

		JJBug bug = new JJBug();
		bug.setName("BUG NAME");
		bug.setEnabled(true);
		bug.setDescription("Insert a comment");
		bug.setProject(project);
		bug.setTeststep(jJTeststep);

		saveJJBug(bug);
		reset();

	}

	public void reset() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		session.setAttribute("jJBugBean", new JJBugBean());
		// JJBug_ = null;
		// bugProjectSelected = null;
		// bugRequirementSelected = null;
		// bugList = null;
		// selectedBugList = null;
		// filteredJJBug=null;
		//
		// criticityOptions = null;
		// importanceOptions = null;
		// statusOptions = null;
		//
		// setSelectedBugs(null);
		// setSelectedTasks(null);
		// setSelectedMessages(null);
		// setCreateDialogVisible(false);
	}

	public void saveBug() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJTeststepexecutionBean jJTeststepexecutionBean = (JJTeststepexecutionBean) session
				.getAttribute("jJTeststepexecutionBean");
		JJBug_.setVersioning(bugVersionSelected);

		JJBug_.setVersioning(bugVersionSelected);

		JJBug_.setRequirement(bugRequirementSelected);

		JJBug_.setProject(bugProjectSelected);

		if (JJBug_.getId() == null) {

			JJTeststep teststep = jJTeststepService
					.findJJTeststep(jJTeststepexecutionBean
							.getTeststepexecution().getTeststep().getId());

			JJBug_.setTeststep(teststep);
			teststep.getBugs().add(JJBug_);

			saveJJBug(JJBug_);
		} else {

			updateJJBug(JJBug_);
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('bugTestDialogWidget').hide()");

		if (jJTeststepexecutionBean.getDisabledTestcase()) {
			jJTeststepexecutionBean.nextTab();
			jJTeststepexecutionBean.onTabChange();
		} else {
			jJTeststepexecutionBean.changeTestcaseStatus();
		}

	}

	public void persistBugTask() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJSprintBean jJSprintBean = (JJSprintBean) session
				.getAttribute("jJSprintBean");
		String message = "";
		updateJJBug(JJBug_);
		message = "message_successfully_updated";

		FacesMessage facesMessage = MessageFactory.getMessage(message, "Bug");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('projectTabView').select(" + 1 + ")");
		jJSprintBean.setUpdate(false);
		context.execute("PF('SprintTab').select("
				+ jJSprintBean.contains(jJSprintBean.getSprintUtil()
						.getSprint().getId()) + ")");
	}

	public void persistBug() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		if (LoginBean.getVersion() != null)
			JJBug_.setVersioning(((JJVersionBean) session
					.getAttribute("jJVersionBean")).getVersion());
		else
			JJBug_.setVersioning(bugVersionSelected);

		JJBug_.setRequirement(bugRequirementSelected);
		if (project == null) {
			JJBug_.setProject(bugProjectSelected);

		} else
			JJBug_.setProject(project);

		if (JJBug_.getRequirement() != null) {

			JJBug_.setCategory(JJBug_.getRequirement().getCategory());
		}

		if (JJBug_.getId() == null) {
			JJBug_.setEnabled(true);
		}

		// if (JJBug_.getRequirement() != null)
		// System.out.println(JJBug_.getRequirement().getName());

		String message = "";
		if (JJBug_.getId() != null) {
			updateJJBug(JJBug_);
			message = "message_successfully_updated";
		} else {
			saveJJBug(JJBug_);
			message = "message_successfully_created";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message, "Bug");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		System.err.println(facesMessage.getDetail());
		if (message != "message_successfully_updated")
			RequestContext.getCurrentInstance().execute(
					"PF('createDialogWidget').hide()");
		else
			RequestContext.getCurrentInstance().execute(
					"PF('editDialogWidget').hide()");

	}

	public String findStyleColor(JJBug b) {
		if (b.getStatus() != null)

			return b.getStatus().getName();
		else
			return "";

	}

	public void handleDialogClose() {

		JJBug_ = null;
		bugProjectSelected = null;
		bugVersionSelected = null;
		bugRequirementSelected = null;

	}

	public void saveJJBug(JJBug b) {
		JJContact contact = (JJContact) ((HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false))
				.getAttribute("JJContact");
		b.setCreatedBy(contact);
		b.setCreationDate(new Date());
		jJBugService.saveJJBug(b);
	}

	public void updateJJBug(JJBug b) {
		JJContact contact = (JJContact) ((HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false))
				.getAttribute("JJContact");
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJBugService.updateJJBug(b);
	}

	public void changeEvent(SelectEvent e) {
		bugRequirementSelected = null;
	}

	public List<JJRequirement> completeReqBug(String query) {

		List<JJRequirement> suggestions = new ArrayList<JJRequirement>();
		JJProduct prod = LoginBean.getProduct();
		suggestions.add(null);

		for (JJRequirement req : jJRequirementService.getRequirements(
				(JJCompany) LoginBean.findBean("JJCompany"),
				bugProjectSelected, prod, null)) {
			String jJCriticityStr = String.valueOf(req.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(req);
			}
		}
		return suggestions;
	}

	public List<JJImportance> completeImportance(String query) {
		List<JJImportance> suggestions = new ArrayList<JJImportance>();
		suggestions.add(null);
		for (JJImportance jJImportance : jJImportanceService.getBugImportance()) {
			String jJImportanceStr = String.valueOf(jJImportance.getName());
			if (jJImportanceStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJImportance);
			}
		}
		return suggestions;
	}

	public List<JJTeststep> completeTestStepsBug(String query) {
		List<JJTeststep> suggestions = new ArrayList<JJTeststep>();

		suggestions.add(null);
		for (JJTeststep testStep : jJTeststepService.getJJtestSteps(
				bugRequirementSelected, bugProjectSelected)) {
			String jJCriticityStr = String.valueOf(testStep.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(testStep);
			}
		}
		return suggestions;
	}

	public List<JJBuild> completeBuildBug(String query) {
		JJProduct prod = LoginBean.getProduct();

		List<JJBuild> suggestions = new ArrayList<JJBuild>();

		suggestions.add(null);
		for (JJBuild req : jJBuildService.getBuilds(prod, bugVersionSelected,
				true)) {
			String jJCriticityStr = String.valueOf(req.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(req);
			}
		}
		return suggestions;
	}

	public List<JJVersion> completeVersioningBug(String query) {		
		JJProduct prod = LoginBean.getProduct();
		List<JJVersion> suggestions = new ArrayList<JJVersion>();
		suggestions.add(null);

		for (JJVersion req : jJVersionService.getVersions(true, prod != null,
				prod, (JJCompany) LoginBean.findBean("JJCompany"))) {
			String jJCriticityStr = String.valueOf(req.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(req);
			}
		}
		return suggestions;
	}

	public List<JJSprint> completeSprintBug(String query) {
		List<JJSprint> suggestions = new ArrayList<JJSprint>();
		suggestions.add(null);

		for (JJSprint req : jJSprintService
				.getSprints(bugProjectSelected, true)) {
			String jJCriticityStr = String.valueOf(req.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(req);
			}
		}
		return suggestions;
	}

	public List<JJStatus> completeStatusBug(String query) {
		List<JJStatus> suggestions = new ArrayList<JJStatus>();
		suggestions.add(null);
		for (JJStatus jJStatus : jJStatusService.getStatus("Bug", true, null,
				true)) {
			String jJCriticityStr = String.valueOf(jJStatus.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJStatus);
			}
		}
		return suggestions;
	}

	public List<JJCriticity> completeCriticityBug(String query) {
		List<JJCriticity> suggestions = new ArrayList<JJCriticity>();
		suggestions.add(null);
		for (JJCriticity jJCriticity : jJCriticityService.getCriticities("Bug",
				true)) {
			String jJCriticityStr = String.valueOf(jJCriticity.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJCriticity);
			}
		}
		return suggestions;
	}

}
