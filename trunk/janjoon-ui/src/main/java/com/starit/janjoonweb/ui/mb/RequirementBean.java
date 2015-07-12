package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.RateEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJImportance;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJMessageService;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.domain.JJTestcaseexecutionService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.CategorieRequirement;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.RequirementUtil;
import com.starit.janjoonweb.ui.security.AuthorisationService;

@Scope("session")
@Component("requirementBean")
public class RequirementBean {

	public static final String REQUIREMENT_SUBSCRIPTION_RATE = "requirement_subscription_rate";
	public static final String REQUIREMENT_SUBSCRIPTION_CANCEL_RATE = "requirement_subscription_cancel_rate";

	@Autowired
	private JJRequirementService jJRequirementService;

	@Autowired
	private JJChapterService jJChapterService;

	@Autowired
	private JJCategoryService jJCategoryService;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJTestcaseService jJTestcaseService;

	@Autowired
	private JJTestcaseexecutionService jJTestcaseexecutionService;

	@Autowired
	private JJPermissionService jJPermissionService;

	@Autowired
	private JJMessageService jJMessageService;

	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	private Integer rated;
	private TreeNode rootNode;
	private TreeNode selectedNode;
	private JJCategory category;
	private List<JJTask> requirementTasks;
	private List<JJCategory> categories;
	private List<JJTestcase> reqtestCases;
	private List<JJTestcase> reqSelectedtestCases;
	private String testCaseName;
	private List<CategorieRequirement> categorieRequirements;
	private List<JJRequirement> linkReqList;
	private List<JJRequirement> linkReq;
	private JJRequirement requirement;
	private boolean reqDialogReqListrender;
	private String categoryName;
	private List<JJMessage> communicationMessages;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setjJChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	public JJCategoryService getjJCategoryService() {
		return jJCategoryService;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
		this.jJTestcaseService = jJTestcaseService;
	}

	public void setjJTestcaseexecutionService(
			JJTestcaseexecutionService jJTestcaseexecutionService) {
		this.jJTestcaseexecutionService = jJTestcaseexecutionService;
	}

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public Integer getRated() {
		return rated;
	}

	public void setRated(Integer rated) {
		this.rated = rated;
	}

	public TreeNode getRootNode() {
		if (rootNode == null)
			createTreeNode();
		return rootNode;
	}

	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public JJRequirement getRequirement() {
		return requirement;
	}

	public void setRequirement(JJRequirement requirement) {
		this.requirement = requirement;
	}

	public JJCategory getCategory() {

		if (category == null)
			category = jJPermissionService
					.getDefaultCategory(((LoginBean) LoginBean
							.findBean("loginBean")).getContact());
		return category;
	}

	public void setCategory(JJCategory category) {
		this.category = category;
	}

	public List<JJTestcase> getReqtestCases() {
		return reqtestCases;
	}

	public void setReqtestCases(List<JJTestcase> reqtestCases) {
		this.reqtestCases = reqtestCases;
	}

	public List<JJTestcase> getReqSelectedtestCases() {
		return reqSelectedtestCases;
	}

	public void setReqSelectedtestCases(List<JJTestcase> reqSelectedtestCases) {
		this.reqSelectedtestCases = reqSelectedtestCases;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public List<CategorieRequirement> getCategorieRequirements() {
		return categorieRequirements;
	}

	public void setCategorieRequirements(
			List<CategorieRequirement> categorieRequirements) {
		this.categorieRequirements = categorieRequirements;
	}

	public List<JJTask> getRequirementTasks() {
		return requirementTasks;
	}

	public void setRequirementTasks(List<JJTask> requirementTasks) {
		this.requirementTasks = requirementTasks;
	}

	public List<JJCategory> getCategories() {

		if (categories == null)
			categories = jJCategoryService.getCategories(null, false, true,
					true);

		return categories;
	}

	public void setCategories(List<JJCategory> categories) {
		this.categories = categories;
	}

	public List<JJRequirement> getLinkReqList() {
		return linkReqList;
	}

	public void setLinkReqList(List<JJRequirement> linkReqList) {
		this.linkReqList = linkReqList;
	}

	public List<JJRequirement> getLinkReq() {
		return linkReq;
	}

	public void setLinkReq(List<JJRequirement> linkReq) {
		this.linkReq = linkReq;
	}

	public List<JJMessage> getCommunicationMessages() {

		if (requirement != null) {
			communicationMessages = jJMessageService
					.getCommMessages(requirement);
			return communicationMessages;
		} else
			return new ArrayList<JJMessage>();
	}

	public void setCommunicationMessages(List<JJMessage> communicationMessages) {
		this.communicationMessages = communicationMessages;
	}

	public boolean isReqDialogReqListrender() {

		if (FacesContext.getCurrentInstance().getViewRoot().getViewId()
				.contains("specifications"))
			reqDialogReqListrender = true;
		else
			reqDialogReqListrender = false;

		return reqDialogReqListrender;
	}

	public RequirementBean() {

	}

	public void categorySelectionChanged(final AjaxBehaviorEvent event) {

		rootNode = null;
		selectedNode = null;
	}

	public void updateReqTestCases(JJTestcaseBean jjTestcaseBean) {

		for (JJTestcase test : reqtestCases) {
			if (!reqSelectedtestCases.contains(test)) {
				test.setEnabled(false);
				jjTestcaseBean.updateJJTestcase(test);
			}

		}
		reqtestCases = jJTestcaseService.getJJtestCases(requirement);
		reqSelectedtestCases = reqtestCases;
		testCaseName = null;
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "Requirement");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public void addTestCase(JJTestcaseBean jjTestcaseBean) {

		if (!testCaseName.isEmpty()) {
			JJTestcase b = new JJTestcase();
			b.setName(testCaseName);
			b.setEnabled(true);
			b.setRequirement(jJRequirementService.findJJRequirement(requirement
					.getId()));
			b.setDescription("TestCase for Requirement "
					+ requirement.getName());
			b.setOrdering(jJTestcaseService.getMaxOrdering(requirement));
			b.setAutomatic(false);
			jjTestcaseBean.saveJJTestcase(b);
			testCaseName = "";

			reqtestCases = jJTestcaseService.getJJtestCases(requirement);
			reqSelectedtestCases = reqtestCases;

			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_created", "TestCase");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"TestCase Name is required", ""));
		}

	}

	public void checkRequirement(ComponentSystemEvent e) throws IOException {

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String value = request.getParameter("requirement");
		try {
			long id = Long.parseLong(value);
			categoryName = null;
			requirementTasks = new ArrayList<JJTask>();
			JJProductBean jJProductBean = ((JJProductBean) LoginBean
					.findBean("jJProductBean"));
			JJVersionBean jJVersionBean = ((JJVersionBean) LoginBean
					.findBean("jJVersionBean"));
			JJProjectBean jJProjectBean = ((JJProjectBean) LoginBean
					.findBean("jJProjectBean"));

			requirement = jJRequirementService.findJJRequirement(id);
			boolean show = requirement != null;
			if (show)
				show = requirement.getEnabled();

			if (show)
				show = jJPermissionService.isAuthorized(((LoginBean) LoginBean
						.findBean("loginBean")).getContact(), requirement
						.getProject(), requirement.getProduct(), "Requirement");

			if (show
					&& !jJProjectBean.getProjectList().contains(
							requirement.getProject()))
				show = false;

			if (show) {
				boolean change = false;
				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);

				if (jJProjectBean.getProject() == null) {
					change = true;
					jJProjectBean.setProject(requirement.getProject());
					jJProductBean.setProduct(requirement.getProduct());
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(requirement.getVersioning());

					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());
				} else if (!jJProjectBean.getProject().equals(
						requirement.getProject())) {
					change = true;
					jJProjectBean.setProject(requirement.getProject());
					jJProductBean.setProduct(requirement.getProduct());
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(requirement.getVersioning());

					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());
				} else if (requirement.getProduct() != null
						&& jJProductBean.getProduct() != null) {
					if (!requirement.getProduct().equals(
							jJProductBean.getProduct())) {
						change = true;
						jJProductBean.setProduct(requirement.getProduct());
						jJVersionBean.getVersionList();
						jJVersionBean.setVersion(requirement.getVersioning());
						session.setAttribute("jJTaskBean", new JJTaskBean());
						session.setAttribute("jJStatusBean", new JJStatusBean());
						session.setAttribute("jJSprintBean", new JJSprintBean());
					} else if (requirement.getVersioning() != null
							&& jJVersionBean.getVersion() != null) {
						if (!requirement.getVersioning().equals(
								jJVersionBean.getVersion())) {
							change = true;
							jJVersionBean.getVersionList();
							jJVersionBean.setVersion(requirement
									.getVersioning());
							session.setAttribute("jJStatusBean", new JJStatusBean());
						}
					}
				}

				if (change) {
					((LoginBean) LoginBean.findBean("loginBean"))
							.changeEvent(null);
					((LoginBean) LoginBean.findBean("loginBean"))
							.setAuthorisationService(new AuthorisationService(
									(HttpSession) FacesContext
											.getCurrentInstance()
											.getExternalContext()
											.getSession(false),
									((LoginBean) LoginBean
											.findBean("loginBean"))
											.getContact()));
					rootNode = null;
				}

				if (!getCategory().equals(requirement.getCategory())) {
					category = requirement.getCategory();
					rootNode = null;
				}

				reqtestCases = jJTestcaseService.getJJtestCases(requirement);
				reqSelectedtestCases = reqtestCases;
				testCaseName = "";
				rated = (((LoginBean) LoginBean.findBean("loginBean"))
						.getContact().getRequirements().contains(requirement)) ? 1
						: 0;

				requirementTasks = jJTaskService.getImportTasks(null,
						requirement, null, true);
				categorieRequirements = CategorieRequirement
						.initCategorieRequirement(requirement,
								jJCategoryService.getCategories(null, false,
										true, true));

			} else {
				requirement = null;
				FacesMessage facesMessage = MessageFactory.getMessage(
						"validator_page_access", "requirement");
				facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
				((LoginBean) LoginBean.findBean("loginBean"))
						.setFacesMessage(facesMessage);
			}

		} catch (NumberFormatException ex) {
		}

	}

	public void createTreeNode() {

		rootNode = new DefaultTreeNode("Root", null);

		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		JJProject project = LoginBean.getProject();
		JJProduct product = LoginBean.getProduct();
		JJVersion version = LoginBean.getVersion();

		TreeNode projectNode = new DefaultTreeNode("projet", project, rootNode);

		projectNode.setExpanded(true);

		if (category == null)
			getCategory();

		TreeNode categoryNode = new DefaultTreeNode("category", category,
				projectNode);

		categoryNode.setExpanded(true);

		List<JJChapter> chapters = jJChapterService.getParentsChapter(
				((LoginBean) LoginBean.findBean("loginBean")).getContact()
						.getCompany(), project, category, true, true);

		for (JJChapter ch : chapters) {
			createTreeNode(ch, categoryNode, category, project, product,
					version);
		}

		List<JJRequirement> requirements = jJRequirementService
				.getRequirementsWithOutChapter(((LoginBean) LoginBean
						.findBean("loginBean")).getContact().getCompany(),
						category, loginBean.getAuthorizedMap("Requirement",
								project, product), version, null, true, true);

		for (JJRequirement requirement : requirements) {
			new DefaultTreeNode("Requirement", new RequirementUtil(requirement,
					jJCategoryService, jJRequirementService, jJTaskService,
					jJTestcaseService, jJTestcaseexecutionService),
					categoryNode);
		}

	}

	public TreeNode createTreeNode(JJChapter chapter, TreeNode categoryNode,
			JJCategory cat, JJProject project, JJProduct product,
			JJVersion version) {

		TreeNode newNode = new DefaultTreeNode("chapter", chapter, categoryNode);
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		List<JJRequirement> requirements = jJRequirementService
				.getRequirements(((LoginBean) LoginBean.findBean("loginBean"))
						.getContact().getCompany(), cat, loginBean
						.getAuthorizedMap("Requirement", project, product),
						version, null, chapter, true, true, true, false, null);

		for (JJRequirement requirement : requirements) {
			new DefaultTreeNode("Requirement", new RequirementUtil(requirement,
					jJCategoryService, jJRequirementService, jJTaskService,
					jJTestcaseService, jJTestcaseexecutionService), newNode);
		}
		if (requirement != null && requirement.getChapter() != null
				&& requirement.getChapter().equals(chapter))
			newNode.setExpanded(true);
		return newNode;
	}

	public void onNodeSelect(NodeSelectEvent event) throws IOException {

		if (event.getTreeNode().getData() instanceof RequirementUtil) {

			long id = ((RequirementUtil) event.getTreeNode().getData())
					.getRequirement().getId();
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							FacesContext.getCurrentInstance()
									.getExternalContext()
									.getRequestContextPath()
									+ "/pages/requirement.jsf?requirement="
									+ id + "&faces-redirect=true");
		}

	}

	public void updateRequirementLinks(JJRequirementBean jJRequirementBean)
			throws IOException {

		JJCategory linkCategory = jJCategoryService.getCategory(categoryName,
				true);
		requirement = jJRequirementService.findJJRequirement(requirement
				.getId());

		if (requirement.getCategory().getStage() < linkCategory.getStage()) {
			List<JJRequirement> listReq = new ArrayList<JJRequirement>(
					requirement.getRequirementLinkUp());
			for (JJRequirement req : listReq) {
				if (req.getCategory().equals(linkCategory)) {
					requirement.getRequirementLinkUp().remove(req);
				}
			}
			requirement.getRequirementLinkUp().addAll(
					new HashSet<JJRequirement>(linkReq));
		} else {
			List<JJRequirement> listReq = new ArrayList<JJRequirement>(
					requirement.getRequirementLinkDown());
			for (JJRequirement req : listReq) {
				if (req.getCategory().equals(linkCategory)) {

					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().remove(requirement);
					JJContact contact = ((LoginBean) LoginBean
							.findBean("loginBean")).getContact();
					req.setUpdatedBy(contact);
					req.setUpdatedDate(new Date());
					jJRequirementService.updateJJRequirement(req);

				}
			}
			for (JJRequirement req : linkReq) {
				req = jJRequirementService.findJJRequirement(req.getId());
				req.getRequirementLinkUp().add(requirement);
				JJContact contact = ((LoginBean) LoginBean
						.findBean("loginBean")).getContact();
				req.setUpdatedBy(contact);
				req.setUpdatedDate(new Date());
				jJRequirementService.updateJJRequirement(req);
			}
		}

		rootNode = null;
		selectedNode = null;
		jJRequirementService.saveJJRequirement(requirement);
		jJRequirementBean.updateDataTable(requirement,
				JJRequirementBean.UPDATE_OPERATION, false);
		long id = requirement.getId();

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "Requirement");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		((LoginBean) LoginBean.findBean("loginBean"))
				.setFacesMessage(facesMessage);
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						FacesContext.getCurrentInstance().getExternalContext()
								.getRequestContextPath()
								+ "/pages/requirement.jsf?requirement="
								+ id
								+ "&faces-redirect=true");

	}

	public void loadData(CategorieRequirement cateRequirement) {
		linkReq = new ArrayList<JJRequirement>();
		this.categoryName = cateRequirement.getCategory().getName();
		linkReqList = new ArrayList<JJRequirement>();
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		JJCategory selectedCategory = jJCategoryService.getCategory(
				categoryName, true);
		linkReqList = jJRequirementService.getRequirements(
				((LoginBean) LoginBean.findBean("loginBean")).getContact()
						.getCompany(), selectedCategory, loginBean
						.getAuthorizedMap("Requirement",
								requirement.getProject(),
								requirement.getProduct()), requirement
						.getVersioning(), null, null, false, true, true, false,
				null);
		linkReq = cateRequirement.getRequirements();

	}

	public void closeLinkDialog() {
		linkReq = null;
		linkReqList = null;
		this.categoryName = null;
	}

	// public void closeDialog(boolean value,JJRequirementBean
	// jJRequirementBean) throws IOException
	// {
	// if (jJRequirementBean.getRequirement() != null) {
	// if (jJRequirementBean.getRequirement().getId() != null) {
	// long id = jJRequirementBean.getRequirement().getId();
	// jJRequirementBean.closeDialog(value);
	//
	// if (jJRequirementService.findJJRequirement(id) != null) {
	// FacesContext
	// .getCurrentInstance()
	// .getExternalContext()
	// .redirect(
	// FacesContext.getCurrentInstance()
	// .getExternalContext()
	// .getRequestContextPath()public static final String
	// SPECIFICATION_WARNING_LINKUP = "specification_warning_linkUp";
	public static final String SPECIFICATION_WARNING_LINKDOWN = "specification_warning_linkDown";

	// + "/pages/requirement.jsf?requirement="
	// + id + "&faces-redirect=true");
	// }
	//
	// } else
	// jJRequirementBean.closeDialog(value);
	// } else
	// jJRequirementBean.closeDialog(value);
	//
	// }

	public String underligne(Object node) {

		if (node instanceof RequirementUtil) {
			if (requirement != null
					&& ((RequirementUtil) node).getRequirement().equals(
							requirement))
				return "text-decoration: underline;";
			else
				return "";
		} else
			return "";
	}

	public List<JJChapter> completeChapterRequirement(String query) {
		List<JJChapter> suggestions = new ArrayList<JJChapter>();
		suggestions.add(null);
		for (JJChapter chapter : jJChapterService.getChapters(null,
				LoginBean.getProject(), requirement.getCategory(), true, null)) {
			String jJChapterStr = String.valueOf(chapter.getName());
			if (jJChapterStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(chapter);
			}
		}
		return suggestions;
	}

	public void onrate(RateEvent rateEvent) {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if (!contact.getRequirements().contains(requirement)) {

			((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
			if (((JJMessageBean) LoginBean.findBean("jJMessageBean")) != null) {
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setAlertMessages(null);
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setMainMessages(null);
			}
			contact.getRequirements()
					.add(jJRequirementService.findJJRequirement(requirement
							.getId()));
			if (LoginBean.findBean("jJContactBean") == null) {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJProjectBean", new JJProjectBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory.getMessage(
					REQUIREMENT_SUBSCRIPTION_RATE, "Requirement");
			facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void oncancel() {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if (contact.getRequirements().contains(requirement)) {

			((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
			if (((JJMessageBean) LoginBean.findBean("jJMessageBean")) != null) {
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setAlertMessages(null);
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setMainMessages(null);
			}
			contact.getRequirements()
					.remove(jJRequirementService.findJJRequirement(requirement
							.getId()));
			if (LoginBean.findBean("jJContactBean") == null) {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJProjectBean", new JJProjectBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory.getMessage(
					REQUIREMENT_SUBSCRIPTION_CANCEL_RATE, "Requirement");
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void save(JJRequirementBean jJRequirementBean) throws IOException {

		jJRequirementBean.setRequirementStatus(requirement.getStatus());
		jJRequirementBean.getRequirementOrder(requirement);
		// jJRequirementBean.updateJJRequirement(requirement);
		jJRequirementBean.updateDataTable(requirement,
				JJRequirementBean.UPDATE_OPERATION, false);
		;
		long id = requirement.getId();

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "Requirement");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		((LoginBean) LoginBean.findBean("loginBean"))
				.setFacesMessage(facesMessage);
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						FacesContext.getCurrentInstance().getExternalContext()
								.getRequestContextPath()
								+ "/pages/requirement.jsf?requirement="
								+ id
								+ "&faces-redirect=true");
	}

}
