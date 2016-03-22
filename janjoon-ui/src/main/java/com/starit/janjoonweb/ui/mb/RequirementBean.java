package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJContact;
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
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.JJVersionService;
import com.starit.janjoonweb.ui.mb.util.CategorieRequirement;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.security.AuthorisationService;

@Scope("session")
@Component("requirementBean")
public class RequirementBean {

	public static final String REQUIREMENT_SUBSCRIPTION_RATE = "requirement_subscription_rate";
	public static final String REQUIREMENT_SUBSCRIPTION_CANCEL_RATE = "requirement_subscription_cancel_rate";

	@Autowired
	private JJRequirementService jJRequirementService;

	@Autowired
	private JJVersionService jJVersionService;

	@Autowired
	private JJChapterService jJChapterService;

	@Autowired
	private JJCategoryService jJCategoryService;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJTestcaseService jJTestcaseService;

	@Autowired
	private JJPermissionService jJPermissionService;

	@Autowired
	private JJMessageService jJMessageService;

	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	// private Integer rated;
	private TreeNode rootNode;
	private TreeNode selectedNode;
	private Integer rowCount;
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

	public void setjJVersionService(JJVersionService jJVersionService) {
		this.jJVersionService = jJVersionService;
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

	public void setjJPermissionService(
			JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public boolean isRated() {
		return (((LoginBean) LoginBean.findBean("loginBean")).getContact()
				.getRequirements().contains(requirement));
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

	public Integer getRowCount() {
		if (rootNode == null || rowCount == null)
			createTreeNode();
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public JJRequirement getRequirement() {
		return requirement;
	}

	public void setRequirement(JJRequirement requirement) {
		this.requirement = requirement;
	}

	public JJCategory getCategory() {

		if (category == null)
			category = jJPermissionService.getDefaultCategory(
					((LoginBean) LoginBean.findBean("loginBean")).getContact());
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
					true, LoginBean.getCompany());

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

	public void setCommunicationMessages(
			List<JJMessage> communicationMessages) {
		this.communicationMessages = communicationMessages;
	}

	public String getContainerType() {

		switch (categorieRequirements.size()) {

			case 1 :
				return "Container100";
			case 2 :
				return "Container55";
			case 3 :
				return "Container33";
			case 4 :
				return "Container25";
			case 5 :
				return "Container20";
			default :
				return "Container20";
		}

	}

	// private String activeIndex;

	public String getActiveIndex() {

		return ((LoginBean) LoginBean.findBean("loginBean")).isMobile()
				? "-1"
				: "0";

	}

	public void setActiveIndex(String index) {

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
		rowCount = null;
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
				"message_successfully_updated",
				MessageFactory.getMessage("label_requirement", "").getDetail(),
				"e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public void addTestCase(JJTestcaseBean jjTestcaseBean) {

		if (!testCaseName.isEmpty()) {
			JJTestcase b = new JJTestcase();
			b.setName(testCaseName);
			b.setEnabled(true);
			b.setRequirement(jJRequirementService
					.findJJRequirement(requirement.getId()));
			b.setDescription(
					"TestCase for Requirement " + requirement.getName());
			b.setOrdering(jJTestcaseService.getMaxOrdering(requirement));
			b.setAutomatic(false);
			jjTestcaseBean.saveJJTestcase(b);
			testCaseName = "";

			reqtestCases = jJTestcaseService.getJJtestCases(requirement);
			reqSelectedtestCases = reqtestCases;

			FacesMessage facesMessage = MessageFactory
					.getMessage("message_successfully_created", "TestCase");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
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
				show = jJPermissionService.isAuthorized(
						((LoginBean) LoginBean.findBean("loginBean"))
								.getContact(),
						requirement.getProject(), requirement.getProduct(),
						"Requirement");

			if (show && !jJProjectBean.getProjectList()
					.contains(requirement.getProject()))
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
					session.setAttribute("jJRiskBean", new JJRiskBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());
				} else if (!jJProjectBean.getProject()
						.equals(requirement.getProject())) {
					change = true;
					jJProjectBean.setProject(requirement.getProject());
					jJProductBean.setProduct(requirement.getProduct());
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(requirement.getVersioning());

					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJRiskBean", new JJRiskBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());
				} else if (requirement.getProduct() != null
						&& jJProductBean.getProduct() != null) {
					if (!requirement.getProduct()
							.equals(jJProductBean.getProduct())) {
						change = true;
						jJProductBean.setProduct(requirement.getProduct());
						jJVersionBean.getVersionList();
						jJVersionBean.setVersion(requirement.getVersioning());
						session.setAttribute("jJTaskBean", new JJTaskBean());
						session.setAttribute("jJStatusBean",
								new JJStatusBean());
						session.setAttribute("jJSprintBean",
								new JJSprintBean());
						session.setAttribute("jJRiskBean", new JJRiskBean());
					} else if (requirement.getVersioning() != null
							&& jJVersionBean.getVersion() != null) {
						if (!requirement.getVersioning()
								.equals(jJVersionBean.getVersion())) {
							change = true;
							jJVersionBean.getVersionList();
							jJVersionBean
									.setVersion(requirement.getVersioning());
							session.setAttribute("jJStatusBean",
									new JJStatusBean());
						}
					}
				} else if (jJProductBean.getProduct() != null
						&& requirement.getProduct() == null) {
					change = true;
					jJProductBean.setProduct(null);
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(null);
					session.setAttribute("jJTaskBean", new JJTaskBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJRiskBean", new JJRiskBean());
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
				// rated = (((LoginBean) LoginBean.findBean("loginBean"))
				// .getContact().getRequirements().contains(requirement)) ? 1
				// : 0;

				requirementTasks = jJTaskService.getImportTasks(null,
						requirement, null, true);
				categorieRequirements = CategorieRequirement
						.initCategorieRequirement(requirement,
								jJCategoryService.getCategories(null, false,
										true, true, LoginBean.getCompany()));

			} else {
				requirement = null;
				FacesMessage facesMessage = MessageFactory
						.getMessage("validator_page_access",
								MessageFactory
										.getMessage("label_requirement", "")
										.getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
				((LoginBean) LoginBean.findBean("loginBean"))
						.setFacesMessage(facesMessage);
			}

		} catch (NumberFormatException ex) {
		}

	}

	public void createTreeNode() {

		rootNode = new DefaultTreeNode("Root", null);
		rowCount = 0;

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
				LoginBean.getCompany(), project, category, true, true);

		for (JJChapter ch : chapters) {
			createTreeNode(ch, categoryNode, category, project, product,
					version);
		}

		List<JJRequirement> requirements = jJRequirementService
				.getRequirementsWithOutChapter(LoginBean.getCompany(), category,
						loginBean.getAuthorizedMap("Requirement", project,
								product),
						version, null, true, true);

		for (JJRequirement requirement : requirements) {
			rowCount++;
			new DefaultTreeNode("Requirement", JJRequirementBean.getRowState(
					requirement, jJRequirementService), categoryNode);
		}

	}

	public TreeNode createTreeNode(JJChapter chapter, TreeNode categoryNode,
			JJCategory cat, JJProject project, JJProduct product,
			JJVersion version) {

		TreeNode newNode = new DefaultTreeNode("chapter", chapter,
				categoryNode);
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		List<JJRequirement> requirements = jJRequirementService
				.getRequirements(LoginBean.getCompany(), cat,
						loginBean.getAuthorizedMap("Requirement", project,
								product),
						version, null, chapter, true, true, true, false, null);

		for (JJRequirement requirement : requirements) {
			rowCount++;
			new DefaultTreeNode("Requirement", JJRequirementBean
					.getRowState(requirement, jJRequirementService), newNode);
		}
		if (requirement != null && requirement.getChapter() != null
				&& requirement.getChapter().equals(chapter))
			newNode.setExpanded(true);
		return newNode;
	}

	public void onNodeSelect(NodeSelectEvent event) throws IOException {

		if (event.getTreeNode().getData() instanceof JJRequirement) {

			long id = ((JJRequirement) event.getTreeNode().getData()).getId();
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(FacesContext.getCurrentInstance()
							.getExternalContext().getRequestContextPath()
							+ "/pages/requirement.jsf?requirement=" + id
							+ "&faces-redirect=true");
		}

	}

	public void updateRequirementLinks(JJRequirementBean jJRequirementBean)
			throws IOException {

		JJCategory linkCategory = jJCategoryService.getCategory(categoryName,
				LoginBean.getCompany(), true);
		requirement = jJRequirementService
				.findJJRequirement(requirement.getId());

		List<JJRequirement> linkUpToUpdate = new ArrayList<JJRequirement>();

		if (requirement.getCategory().getStage() < linkCategory.getStage()) {
			List<JJRequirement> listReq = new ArrayList<JJRequirement>(
					requirement.getRequirementLinkUp());
			for (JJRequirement req : listReq) {
				if (req.getCategory().equals(linkCategory)) {
					requirement.getRequirementLinkUp().remove(req);
					if (!linkReq.contains(req))
						linkUpToUpdate.add(req);
				}
			}
			requirement.getRequirementLinkUp()
					.addAll(new HashSet<JJRequirement>(linkReq));
		} else {
			List<JJRequirement> listReq = new ArrayList<JJRequirement>(
					requirement.getRequirementLinkDown());
			for (JJRequirement req : listReq) {
				if (req.getCategory().equals(linkCategory)) {

					if (!linkReq.contains(req)) {
						req = jJRequirementService
								.findJJRequirement(req.getId());
						req.getRequirementLinkUp().remove(requirement);
						// jJRequirementBean.updateJJRequirement(req);
						JJRequirementBean.updateRowState(req,
								jJRequirementService, requirement);
						jJRequirementBean.updateDataTable(req,
								req.getCategory(),
								JJRequirementBean.UPDATE_OPERATION);
					}

				}
			}
			for (JJRequirement req : linkReq) {
				if (!listReq.contains(req)) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().add(requirement);
					// jJRequirementBean.updateJJRequirement(req);
					JJRequirementBean.updateRowState(req, jJRequirementService,
							requirement);
					jJRequirementBean.updateDataTable(req, req.getCategory(),
							JJRequirementBean.UPDATE_OPERATION);
				}
			}
		}

		rootNode = null;
		selectedNode = null;
		JJRequirementBean.updateRowState(requirement, jJRequirementService,
				requirement);
		jJRequirementBean.updateDataTable(requirement,
				requirement.getCategory(), JJRequirementBean.UPDATE_OPERATION);

		for (JJRequirement req : linkUpToUpdate) {
			JJRequirementBean.updateRowState(req, jJRequirementService,
					requirement);
			jJRequirementBean.updateDataTable(req, req.getCategory(),
					JJRequirementBean.UPDATE_OPERATION);
		}

		long id = requirement.getId();

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated",
				MessageFactory.getMessage("label_requirement", "").getDetail(),
				"e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		((LoginBean) LoginBean.findBean("loginBean"))
				.setFacesMessage(facesMessage);
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(FacesContext.getCurrentInstance().getExternalContext()
						.getRequestContextPath()
						+ "/pages/requirement.jsf?requirement=" + id
						+ "&faces-redirect=true");

	}

	public void loadData(CategorieRequirement cateRequirement) {
		linkReq = new ArrayList<JJRequirement>();
		this.categoryName = cateRequirement.getCategory().getName();
		linkReqList = new ArrayList<JJRequirement>();
		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		JJCategory selectedCategory = jJCategoryService
				.getCategory(categoryName, LoginBean.getCompany(), true);
		linkReqList = jJRequirementService.getRequirements(
				LoginBean.getCompany(), selectedCategory,
				loginBean.getAuthorizedMap("Requirement",
						requirement.getProject(), requirement.getProduct()),
				requirement.getVersioning(), null, null, false, true, true,
				false, null);
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

		if (node instanceof JJRequirement) {
			if (requirement != null
					&& ((JJRequirement) node).equals(requirement))
				return "opacity: 0.7;filter: alpha(opacity=70);";
			else
				return "";
		} else
			return "";
	}

	public List<JJChapter> completeChapterRequirement(String query) {
		List<JJChapter> suggestions = new ArrayList<JJChapter>();
		suggestions.add(null);
		for (JJChapter chapter : jJChapterService.getChapters(null,
				requirement.getProject(), requirement.getCategory(), true,
				null)) {
			String jJChapterStr = String.valueOf(chapter.getName());
			if (jJChapterStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(chapter);
			}
		}
		return suggestions;
	}

	public List<JJProject> completeProjectRequirement(String query) {

		List<JJProject> projectList = new ArrayList<JJProject>();
		projectList.add(null);
		projectList.addAll(((JJProjectBean) LoginBean.findBean("jJProjectBean"))
				.getProjectList());
		for (JJProject proj : ((JJProjectBean) LoginBean
				.findBean("jJProjectBean")).getProjectList()) {
			String name = String.valueOf(proj.getName());
			if (name.toLowerCase().startsWith(query.toLowerCase())) {
				projectList.add(proj);
			}
		}

		return projectList;
	}

	public List<JJCategory> completeCategoryRequirement(String query) {

		List<JJCategory> categoryList = new ArrayList<JJCategory>();
		categoryList.add(null);
		for (JJCategory cate : getCategories()) {
			String name = String.valueOf(cate.getName());
			if (name.toLowerCase().startsWith(query.toLowerCase())) {
				categoryList.add(cate);
			}
		}
		return categoryList;
	}

	public List<JJProduct> completeProductRequirement(String query) {

		List<JJProduct> productList = new ArrayList<JJProduct>();
		productList.add(null);
		for (JJProduct prod : ((JJProductBean) LoginBean
				.findBean("jJProductBean")).getProductList()) {
			String name = String.valueOf(prod.getName());
			if (name.toLowerCase().startsWith(query.toLowerCase())) {
				productList.add(prod);
			}
		}

		return productList;
	}

	public List<JJVersion> completeVersionRequirement(String query) {

		List<JJVersion> versionList = new ArrayList<JJVersion>();
		versionList.add(null);

		if (requirement.getProduct() != null)
			for (JJVersion ver : jJVersionService.getVersions(true, true,
					requirement.getProduct(), LoginBean.getCompany(), true)) {
				String name = String.valueOf(ver.getName());
				if (name.toLowerCase().startsWith(query.toLowerCase())) {
					versionList.add(ver);
				}
			}

		return versionList;
	}

	public void onrate() {

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
			contact.getRequirements().add(jJRequirementService
					.findJJRequirement(requirement.getId()));
			if (LoginBean.findBean("jJContactBean") == null) {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJContactBean", new JJContactBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory
					.getMessage(REQUIREMENT_SUBSCRIPTION_RATE);
			facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} else {

			((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
			if (((JJMessageBean) LoginBean.findBean("jJMessageBean")) != null) {
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setAlertMessages(null);
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setMainMessages(null);
			}
			contact.getRequirements().remove(jJRequirementService
					.findJJRequirement(requirement.getId()));
			if (LoginBean.findBean("jJContactBean") == null) {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJContactBean", new JJContactBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory
					.getMessage(REQUIREMENT_SUBSCRIPTION_CANCEL_RATE);
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);

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
			contact.getRequirements().remove(jJRequirementService
					.findJJRequirement(requirement.getId()));
			if (LoginBean.findBean("jJContactBean") == null) {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJContactBean", new JJContactBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory
					.getMessage(REQUIREMENT_SUBSCRIPTION_CANCEL_RATE);
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void save(JJRequirementBean jJRequirementBean) throws IOException {

		// updateDataTable pour le requirementBean
		JJCategory oldCategory = jJRequirementService
				.findJJRequirement(requirement.getId()).getCategory();
		boolean changeCategorie = !requirement.getCategory().getStage()
				.equals(oldCategory.getStage());

		boolean change = !requirement.getProject()
				.equals(LoginBean.getProject());

		if (!change && LoginBean.getProduct() != null) {
			change = !LoginBean.getProduct().equals(requirement.getProduct());
		}

		if (!change && LoginBean.getVersion() != null) {
			change = !LoginBean.getVersion()
					.equals(requirement.getVersioning());
		}

		if (change)
			jJRequirementBean.setTableDataModelList(null);

		List<JJRequirement> reqToUpdate = new ArrayList<JJRequirement>();

		if (changeCategorie) {
			List<JJRequirement> listReq = new ArrayList<JJRequirement>(
					requirement.getRequirementLinkUp());

			for (JJRequirement req : listReq) {
				if (requirement.getCategory().getStage() >= req.getCategory()
						.getStage()) {
					requirement.getRequirementLinkUp().remove(req);
					reqToUpdate.add(req);
				}
			}

			listReq = new ArrayList<JJRequirement>(
					requirement.getRequirementLinkDown());

			for (JJRequirement req : listReq) {
				if (requirement.getCategory().getStage() <= req.getCategory()
						.getStage()) {
					req = jJRequirementService.findJJRequirement(req.getId());
					req.getRequirementLinkUp().remove(requirement);
					req = jJRequirementBean.updateJJRequirement(req);

				}
			}
		}

		jJRequirementBean.setRequirementStatus(requirement.getStatus());
		jJRequirementBean.getRequirementOrder(requirement);

		jJRequirementBean.updateDataTable(requirement, oldCategory,
				JJRequirementBean.DELETE_OPERATION);

		jJRequirementBean.updateDataTable(requirement,
				requirement.getCategory(), JJRequirementBean.ADD_OPERATION);

		long id = requirement.getId();

		for (JJRequirement req : reqToUpdate) {
			jJRequirementBean.updateJJRequirement(req);
		}

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated",
				MessageFactory.getMessage("label_requirement", "").getDetail(),
				"e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		((LoginBean) LoginBean.findBean("loginBean"))
				.setFacesMessage(facesMessage);

		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(FacesContext.getCurrentInstance().getExternalContext()
						.getRequestContextPath()
						+ "/pages/requirement.jsf?requirement=" + id
						+ "&faces-redirect=true");
	}

}
