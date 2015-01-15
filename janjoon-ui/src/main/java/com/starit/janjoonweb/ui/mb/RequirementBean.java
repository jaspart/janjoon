package com.starit.janjoonweb.ui.mb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;

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
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.RequirementUtil;

@Scope("session")
@Component("requirementBean")
public class RequirementBean {

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

	private TreeNode rootNode;
	private TreeNode selectedNode;
	private JJCategory category;
	private List<JJTask> requirementTasks;
	private List<JJCategory> categories;
	private List<JJTestcase> reqtestCases;
	private List<JJTestcase> reqSelectedtestCases;
	private String testCaseName;
	private List<JJRequirement> technicalRequirements;
	private List<JJRequirement> businessRequirements;
	private List<JJRequirement> securityRequirements;
	private List<JJRequirement> functionalRequirements;
	private List<JJRequirement> architucturalRequirements;
	private List<JJRequirement> linkReqList;
	private List<JJRequirement> linkReq;
	private JJRequirement requirement;
	private boolean reqDialogReqListrender;
	private String categoryName;

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

	public List<JJRequirement> getTechnicalRequirements() {
		return technicalRequirements;
	}

	public void setTechnicalRequirements(
			List<JJRequirement> technicalRequirements) {
		this.technicalRequirements = technicalRequirements;
	}

	public List<JJRequirement> getBusinessRequirements() {
		return businessRequirements;
	}

	public void setBusinessRequirements(List<JJRequirement> businessRequirements) {
		this.businessRequirements = businessRequirements;
	}

	public List<JJRequirement> getSecurityRequirements() {
		return securityRequirements;
	}

	public void setSecurityRequirements(List<JJRequirement> securityRequirements) {
		this.securityRequirements = securityRequirements;
	}

	public List<JJRequirement> getFunctionalRequirements() {
		return functionalRequirements;
	}

	public void setFonctionalRequirements(
			List<JJRequirement> functionalRequirements) {
		this.functionalRequirements = functionalRequirements;
	}

	public List<JJRequirement> getArchitucturalRequirements() {
		return architucturalRequirements;
	}

	public void setArchitucturalRequirements(
			List<JJRequirement> architucturalRequirements) {
		this.architucturalRequirements = architucturalRequirements;
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
		testCaseName=null;
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "Requirement");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}
	
	public void addTestCase(JJTestcaseBean jjTestcaseBean) {
		
		if(!testCaseName.isEmpty())
		{
			JJTestcase b = new JJTestcase();
			b.setName(testCaseName);
			b.setEnabled(true);
			b.setRequirement(jJRequirementService.findJJRequirement(requirement
					.getId()));
			b.setDescription("TestCase for Requirement " + requirement.getName());
			b.setOrdering(reqtestCases.size());
			b.setAutomatic(false);
			jjTestcaseBean.saveJJTestcase(b);
			testCaseName = "";

			reqtestCases = jJTestcaseService.getJJtestCases(requirement);
			reqSelectedtestCases = reqtestCases;

			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_created", "TestCase");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}else
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TestCase Name is required", ""));
		}
		

	}

	public void checkRequirement(ComponentSystemEvent e) throws IOException {

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String value = request.getParameter("requirement");
		try {
			long id = Long.parseLong(value);
			categoryName = null;
			requirementTasks=new ArrayList<JJTask>();
			
			requirement = jJRequirementService.findJJRequirement(id);
			reqtestCases = jJTestcaseService.getJJtestCases(requirement);
			reqSelectedtestCases=reqtestCases;
			testCaseName="";
			requirementTasks=jJTaskService.getImportTasks(null, requirement, null, true);
			technicalRequirements = new ArrayList<JJRequirement>();
			businessRequirements = new ArrayList<JJRequirement>();
			securityRequirements = new ArrayList<JJRequirement>();
			functionalRequirements = new ArrayList<JJRequirement>();
			architucturalRequirements = new ArrayList<JJRequirement>();

			for (JJRequirement req : requirement.getRequirementLinkDown()) {

				if (req.getCategory().getName().equalsIgnoreCase("business")
						&& req.getEnabled())
					businessRequirements.add(req);
				else if (req.getCategory().getName()
						.equalsIgnoreCase("functional")
						&& req.getEnabled())
					functionalRequirements.add(req);
				else if (req.getCategory().getName()
						.equalsIgnoreCase("security")
						&& req.getEnabled())
					securityRequirements.add(req);
				else if (req.getCategory().getName()
						.equalsIgnoreCase("architecture")
						&& req.getEnabled())
					architucturalRequirements.add(req);
				else if (req.getCategory().getName()
						.equalsIgnoreCase("technical")
						&& req.getEnabled())
					technicalRequirements.add(req);
			}

			for (JJRequirement req : requirement.getRequirementLinkUp()) {

				if (req.getCategory().getName().equalsIgnoreCase("business")
						&& req.getEnabled())
					businessRequirements.add(req);
				else if (req.getCategory().getName()
						.equalsIgnoreCase("functional")
						&& req.getEnabled())
					functionalRequirements.add(req);
				else if (req.getCategory().getName()
						.equalsIgnoreCase("security")
						&& req.getEnabled())
					securityRequirements.add(req);
				else if (req.getCategory().getName()
						.equalsIgnoreCase("architecture")
						&& req.getEnabled())
					architucturalRequirements.add(req);
				else if (req.getCategory().getName()
						.equalsIgnoreCase("technical")
						&& req.getEnabled())
					technicalRequirements.add(req);
			}

			if (requirement.getCategory().getName()
					.equalsIgnoreCase("business"))
				businessRequirements = null;
			else if (requirement.getCategory().getName()
					.equalsIgnoreCase("functional"))
				functionalRequirements = null;
			else if (requirement.getCategory().getName()
					.equalsIgnoreCase("security"))
				securityRequirements = null;
			else if (requirement.getCategory().getName()
					.equalsIgnoreCase("architecture"))
				architucturalRequirements = null;
			else if (requirement.getCategory().getName()
					.equalsIgnoreCase("technical"))
				technicalRequirements = null;

		} catch (NumberFormatException ex) {
			System.err.println("NumberFormatException");
		}

	}

	public void createTreeNode() {

		rootNode = new DefaultTreeNode("Root", null);

		JJProject project = ((JJProjectBean) LoginBean
				.findBean("jJProjectBean")).getProject();
		JJProduct product = ((JJProductBean) LoginBean
				.findBean("jJProductBean")).getProduct();
		JJVersion version = ((JJVersionBean) LoginBean
				.findBean("jJVersionBean")).getVersion();

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
						category, project, product, version, null, true, true);

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

		List<JJRequirement> requirements = jJRequirementService
				.getRequirements(((LoginBean) LoginBean.findBean("loginBean"))
						.getContact().getCompany(), cat, project, product,
						version, null, chapter, true, true, true);

		for (JJRequirement requirement : requirements) {
			new DefaultTreeNode("Requirement", new RequirementUtil(requirement,
					jJCategoryService, jJRequirementService, jJTaskService,
					jJTestcaseService, jJTestcaseexecutionService), newNode);
		}
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

	public void updateRequirementLinks() throws IOException {

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
		long id = requirement.getId();
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

	public void loadData(String catName, List<JJRequirement> list) {
		linkReq = new ArrayList<JJRequirement>();
		this.categoryName = catName;
		linkReqList = new ArrayList<JJRequirement>();
		JJCategory selectedCategory = jJCategoryService.getCategory(
				categoryName, true);
		linkReqList = jJRequirementService.getRequirements(
				((LoginBean) LoginBean.findBean("loginBean")).getContact()
						.getCompany(), selectedCategory, requirement
						.getProject(), requirement.getProduct(), requirement
						.getVersioning(), null, null, false, true, true);
		linkReq = list;

	}

	public void closeLinkDialog() {
		linkReq = null;
		linkReqList = null;
		this.categoryName = null;
	}

	public void save() throws IOException {
		jJRequirementService.saveJJRequirement(requirement);
		long id = requirement.getId();
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
