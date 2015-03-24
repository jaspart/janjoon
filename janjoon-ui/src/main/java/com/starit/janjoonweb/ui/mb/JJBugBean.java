package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJImportance;
import com.starit.janjoonweb.domain.JJImportanceService;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyBugDataModel;
import com.starit.janjoonweb.ui.mb.util.CategorieRequirement;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.security.AuthorisationService;

@RooSerializable
@RooJsfManagedBean(entity = JJBug.class, beanName = "jJBugBean")
public class JJBugBean {
	
	@Autowired
	private JJImportanceService jJImportanceService;
	
	@Autowired
	private JJPermissionService jJPermissionService;
	
	@Autowired
	private JJTaskService jjTaskService;
	
	
	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}
	

	public void setjJImportanceService(JJImportanceService jJImportanceService) {
		this.jJImportanceService = jJImportanceService;
	}
	public void setJjTaskService(JJTaskService jjTaskService) {
		this.jjTaskService = jjTaskService;
	}
	//Bug_page attributes
	private JJBug viewBug;
	private List<JJTask> viewBugTasks;
	
	
	
	private JJBug JJBug_;

	private JJProject bugProjectSelected;
	//private JJRequirement bugRequirementSelected;
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

//	public JJRequirement getBugRequirementSelected() {
//		return bugRequirementSelected;
//	}
//
//	public void setBugRequirementSelected(JJRequirement bugRequirementSelected) {
//		this.bugRequirementSelected = bugRequirementSelected;
//	}

//	public JJVersion getBugVersionSelected() {
//		return bugVersionSelected;
//	}
//
//	public void setBugVersionSelected(JJVersion bugVersionSelected) {
//		this.bugVersionSelected = bugVersionSelected;
//	}

	public JJBug getViewBug() {

		if (viewBug == null) {
			viewBug = new JJBug();
			viewBug.setProject(LoginBean.getProject());
			viewBug.setVersioning(LoginBean.getVersion());
			viewBug.setStatus(jJStatusService.getOneStatus("NEW", "BUG", true));			
			viewBugTasks =new ArrayList<JJTask>();
		}
		return viewBug;
	}

	public void setViewBug(JJBug bug) {
		this.viewBug = bug;
	}
	
	public List<JJTask> getViewBugTasks() {
		return viewBugTasks;
	}


	public void setViewBugTasks(List<JJTask> viewBugTasks) {
		this.viewBugTasks = viewBugTasks;
	}


	public JJBug getJJBug_() {
		if (JJBug_ == null) {
			JJBug_ = new JJBug();
			bugProjectSelected = project;
			JJBug_.setVersioning(LoginBean.getVersion());
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
			initJJBugTable(LoginBean.getProduct(), LoginBean.getVersion());
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
//		JJBug_.setVersioning(bugVersionSelected);
//
//		JJBug_.setVersioning(bugVersionSelected);

		//JJBug_.setRequirement(bugRequirementSelected);

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
		
		int i=0;
		if(((LoginBean) LoginBean.findBean("loginBean")).isRenderGantt())
			i=1;
		else
			i=0;

		context.execute("PF('projectTabView').select(" + i + ")");
		jJSprintBean.setUpdate(false);
		context.execute("PF('SprintTab').select("
				+ jJSprintBean.contains(jJSprintBean.getSprintUtil()
						.getSprint().getId()) + ")");
	}
	
	public void persistJJBug() {
		
		if (viewBug.getRequirement() != null) {

			viewBug.setCategory(viewBug.getRequirement().getCategory());
		}
		
		String message = "message_successfully_updated";		
		updateJJBug(viewBug);
		bugList=null;
		viewBug=jJBugService.findJJBug(viewBug.getId());		

		FacesMessage facesMessage = MessageFactory.getMessage(message, "Bug");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);		

	}

	public void persistBug() {

		//JJBug_.setRequirement(bugRequirementSelected);
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
		
		if(JJBug_.getVersioning() == null)
			JJBug_.setVersioning(JJBug_.getBuild().getVersion());

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
	
	public void persistviewBug() throws IOException
	{	

		if (viewBug.getRequirement() != null) {

			viewBug.setCategory(viewBug.getRequirement().getCategory());
		}

		if (viewBug.getId() == null) {
			viewBug.setEnabled(true);
		}

		if(viewBug.getVersioning() == null && viewBug.getBuild() != null)
			viewBug.setVersioning(viewBug.getBuild().getVersion());
		// if (viewBug.getRequirement() != null)
		// System.out.println(viewBug.getRequirement().getName());

		String message = "";
		if (viewBug.getId() != null) {
			updateJJBug(viewBug);
			message = "message_successfully_updated";
		} else {
			saveJJBug(viewBug);
			message = "message_successfully_created";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message, "Bug");		
		((LoginBean) LoginBean.findBean("loginBean"))
		.setFacesMessage(facesMessage);
		
		bugList =null;
		project=null;
		
		FacesContext
		.getCurrentInstance()
		.getExternalContext()
		.redirect(
				FacesContext.getCurrentInstance().getExternalContext()
						.getRequestContextPath()
						+ "/pages/bug.jsf?bug="
						+ viewBug.getId()
						+ "&faces-redirect=true");
		

	}
	
	public void bugInfo() throws IOException
	{		
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						FacesContext.getCurrentInstance().getExternalContext()
								.getRequestContextPath()
								+ "/pages/bug.jsf?bug="
								+ viewBug.getId() + "&faces-redirect=true");

		
	
	}
	
	public void checkBug(ComponentSystemEvent e) throws IOException {

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String value = request.getParameter("bug");
		try {
			long id = Long.parseLong(value);
			
			JJProductBean jJProductBean = ((JJProductBean) LoginBean
					.findBean("jJProductBean"));
			JJVersionBean jJVersionBean = ((JJVersionBean) LoginBean
					.findBean("jJVersionBean"));
			JJProjectBean jJProjectBean = ((JJProjectBean) LoginBean
					.findBean("jJProjectBean"));

			viewBug = jJBugService.findJJBug(id);
			
			JJProduct prod=null;		
			
			boolean show = viewBug != null ;
			
			if(show)
			{
				if(viewBug.getVersioning() != null)
					prod=viewBug.getVersioning().getProduct();
				else if(viewBug.getBuild() != null)
					prod=viewBug.getBuild().getVersion().getProduct();
			}
			
			if(show)
				show=viewBug.getEnabled();
			
			if(show)
			{		
				
				show=jJPermissionService.isAuthorized(((LoginBean) LoginBean
						.findBean("loginBean")).getContact(), viewBug.getProject(), prod, "Bug");
			}
				

			if (show
					&& viewBug.getProject() != null && !jJProjectBean.getProjectList().contains(
							viewBug.getProject()))
				show = false;

			if (show) {
				boolean change = false;
				HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
						.getSession(false);		

				if (jJProjectBean.getProject() == null) {
					change = true;
					jJProjectBean.setProject(viewBug.getProject());
					jJProductBean.setProduct(prod);
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(viewBug.getVersioning());				
									
					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());
				} else if (!jJProjectBean.getProject().equals(
						viewBug.getProject())) {
					change = true;
					jJProjectBean.setProject(viewBug.getProject());
					jJProductBean.setProduct(prod);
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(viewBug.getVersioning());	
								
					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());
				} else if (prod != null
						&& jJProductBean.getProduct() != null) {
					if (!prod.equals(
							jJProductBean.getProduct())) {
						change = true;
						jJProductBean.setProduct(prod);
						jJVersionBean.getVersionList();
						jJVersionBean.setVersion(viewBug.getVersioning());
						session.setAttribute("jJTaskBean", new JJTaskBean());
						session.setAttribute("jJSprintBean", new JJSprintBean());
					} else if (viewBug.getVersioning() != null
							&& jJVersionBean.getVersion() != null) {
						if (!viewBug.getVersioning().equals(
								jJVersionBean.getVersion())) {
							change = true;
							jJVersionBean.getVersionList();
							jJVersionBean.setVersion(viewBug
									.getVersioning());
						}
					}
				}

				if (change) {
//					FacesContext ctx = FacesContext.getCurrentInstance();
//					String viewId = ctx.getViewRoot().getViewId();					

					if (session.getAttribute("jJTestcaseBean") != null) {
						JJCategory cat = ((JJTestcaseBean) session
								.getAttribute("jJTestcaseBean")).getCategory();
						session.setAttribute("jJTestcaseBean", new JJTestcaseBean());
						((JJTestcaseBean) session.getAttribute("jJTestcaseBean"))
								.setCategory(cat);
					}

					// authorisationService = new AuthorisationService(session,contact);
					
					// messageListener(session);
					bugList=null;
					session.setAttribute("jJMessageBean", null);
					session.setAttribute("jJRequirementBean", null);
					((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
					((LoginBean) LoginBean.findBean("loginBean")).setAuthorisationService(new AuthorisationService(
							(HttpSession) FacesContext.getCurrentInstance()
									.getExternalContext().getSession(false),((LoginBean) LoginBean.findBean("loginBean"))
									.getContact()));						
				}
				
				viewBugTasks=jjTaskService.getImportTasks(viewBug, null, null, true);
			} else {
				viewBug = null;
				((LoginBean) LoginBean.findBean("loginBean"))
						.setFacesMessage(new FacesMessage(
								FacesMessage.SEVERITY_WARN,
								"This Bug is not among your company Specs",
								"Bug"));
			}

		} catch (NumberFormatException ex) {
		}

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
		//bugVersionSelected = null;
		//bugRequirementSelected = null;

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
		
		if(b.getRequirement() != null && b.getProject() != null && 
				!b.getRequirement().getProject().equals(b.getProject()))
			b.setRequirement(null);
		
		if(b.getRequirement() != null && b.getTeststep() != null && 
				!b.getTeststep().getTestcase().getRequirement().equals(b.getRequirement()))
			b.setTeststep(null);
		
		JJContact contact = (JJContact) ((HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false))
				.getAttribute("JJContact");
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJBugService.updateJJBug(b);
	}

	public void changeEvent(String field, JJBug b) {

		if (field.equalsIgnoreCase("project"))
			//bugRequirementSelected = null;
			b.setRequirement(null);
		else {
			if (field.equalsIgnoreCase("version"))
				b.setBuild(null);
		}
	}

	public List<JJRequirement> completeReqBug(String query) {

		List<JJRequirement> suggestions = new ArrayList<JJRequirement>();
		JJProduct prod = LoginBean.getProduct();
		JJProject proj = (JJProject) UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get("project");
		suggestions.add(null);

		for (JJRequirement req : jJRequirementService.getRequirements(
				(JJCompany) LoginBean.findBean("JJCompany"),
				proj, prod, null)) {
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
		
		FacesContext context = FacesContext.getCurrentInstance();
	    JJRequirement requirement = (JJRequirement) UIComponent.getCurrentComponent(context).getAttributes().get("requirement");
	    JJProject proj = (JJProject) UIComponent.getCurrentComponent(context).getAttributes().get("project");

		suggestions.add(null);
		for (JJTeststep testStep : jJTeststepService.getJJtestSteps(
				requirement, proj)) {
			String jJCriticityStr = String.valueOf(testStep.getName());
			if (jJCriticityStr.toLowerCase().contains(query.toLowerCase())) {
				suggestions.add(testStep);
			}
		}
		return suggestions;
	}

	public List<JJBuild> completeBuildBug(String query) {	

		FacesContext context = FacesContext.getCurrentInstance();
	    JJVersion versioning = (JJVersion) UIComponent.getCurrentComponent(context).getAttributes().get("version");
	    
		List<JJBuild> suggestions = new ArrayList<JJBuild>();

		suggestions.add(null);
		for (JJBuild req : jJBuildService.getBuilds(LoginBean.getProduct(), versioning,
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
				prod, (JJCompany) LoginBean.findBean("JJCompany"),true)) {
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
