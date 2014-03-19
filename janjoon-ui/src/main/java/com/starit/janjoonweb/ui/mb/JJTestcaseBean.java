package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTestcaseexecutionService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJTestcase.class, beanName = "jJTestcaseBean")
public class JJTestcaseBean {

	@Autowired
	JJCategoryService jJCategoryService;

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	@Autowired
	JJChapterService jJChapterService;

	public void setjJChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

	@Autowired
	JJTestcaseexecutionService jJTestcaseexecutionService;

	public void setjJTestcaseexecutionService(
			JJTestcaseexecutionService jJTestcaseexecutionService) {
		this.jJTestcaseexecutionService = jJTestcaseexecutionService;
	}

	private JJTestcase testcase;

	private JJProject project;
	private JJProduct product;
	private JJVersion version;

	private JJChapter chapter;

	private TreeNode rootNode;
	private TreeNode selectedNode;

	private List<TestCaseRecap> testCaseRecaps;
	private boolean rendredTestCaseRecaps;

	private String message;

	private JJRequirement requirement;
	private List<JJRequirement> requirements;

	public JJTestcase getTestcase() {
		return testcase;
	}

	public void setTestcase(JJTestcase testcase) {
		this.testcase = testcase;
	}

	public JJProject getProject() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProjectBean projbean = (JJProjectBean) session
				.getAttribute("jJProjectBean");
		this.project = projbean.getProject();
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public JJProduct getProduct() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProductBean prodbean = (JJProductBean) session
				.getAttribute("jJProductBean");
		this.product = prodbean.getProduct();
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public JJVersion getVersion() {
		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
	}

	public JJChapter getChapter() {
		return chapter;
	}

	public void setChapter(JJChapter chapter) {
		this.chapter = chapter;
	}

	public TreeNode getRootNode() {
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

	public List<TestCaseRecap> getTestCaseRecaps() {

		testCaseRecaps = new ArrayList<TestCaseRecap>();
		List<JJTestcase> testcases = jJTestcaseService.getTestcases(null,
				chapter, true, true);

		for (JJTestcase testcase : testcases) {
			TestCaseRecap testCaseRecap = new TestCaseRecap(testcase);
			testCaseRecaps.add(testCaseRecap);
		}

		return testCaseRecaps;
	}

	public void setTestCaseRecaps(List<TestCaseRecap> testCaseRecaps) {
		this.testCaseRecaps = testCaseRecaps;
	}

	public boolean getRendredTestCaseRecaps() {
		return rendredTestCaseRecaps;
	}

	public void setRendredTestCaseRecaps(boolean rendredTestCaseRecaps) {
		this.rendredTestCaseRecaps = rendredTestCaseRecaps;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public JJRequirement getRequirement() {
		return requirement;
	}

	public void setRequirement(JJRequirement requirement) {
		this.requirement = requirement;
	}

	public List<JJRequirement> getRequirements() {

		requirements = jJRequirementService.getRequirements(null, null, null,
				null, null, chapter, true, true, true);
		return requirements;
	}

	public void setRequirements(List<JJRequirement> requirements) {

		this.requirements = requirements;
	}

	public void loadData() {

		System.out.println("load data in test");

		this.getProject();
		this.getProduct();
		this.getVersion();

		chapter = null;
		testcase = null;
		rendredTestCaseRecaps = false;
		requirement = null;

		createTestcaseTree();

	}

	public void newTestcase() {
		System.out.println("I m in new testcase");
		message = "New Testcase";
		testcase = new JJTestcase();
		testcase.setEnabled(true);
		testcase.setCreationDate(new Date());
		requirement = null;
	}

	public void save() {
		System.out.println("Saving ...");
		String message = "";
		if (testcase.getId() == null) {
			testcase.setRequirement(requirement);
			requirement.getTestcases().add(testcase);
			jJTestcaseService.saveJJTestcase(testcase);

			newTestcase();
			message = "message_successfully_created";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJTestcase");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public void closeDialog() {
		System.out.println("close Dialog");
		testcase = null;
		requirement = null;
		createTestcaseTree();
	}

	private void createTestcaseTree() {

		rootNode = new DefaultTreeNode("Root", null);

		TreeNode projectNode = new DefaultTreeNode("P-" + project.getId()
				+ "- " + project.getName(), rootNode);

		projectNode.setExpanded(true);

		List<JJCategory> categories = jJCategoryService.getCategories(null,
				false, true, true);
		for (JJCategory category : categories) {

			TreeNode categoryNode = new DefaultTreeNode("C-" + category.getId()
					+ "- " + category.getName(), projectNode);

			categoryNode.setExpanded(true);

			List<JJChapter> parentChapters = jJChapterService
					.getParentsChapter(project, product, category, true, true);

			for (JJChapter chapter : parentChapters) {
				TreeNode node = createTree(chapter, categoryNode, project,
						product, category);
			}
		}

	}

	public void onNodeSelect(NodeSelectEvent event) {

		String selectedNode = event.getTreeNode().toString();

		String code = getSubString(selectedNode, 0, "-");

		if (code.equalsIgnoreCase("P")) {
			System.out.println("Project selected");
			rendredTestCaseRecaps = false;
		} else if (code.equalsIgnoreCase("C")) {

			System.out.println("Category selected");
			rendredTestCaseRecaps = false;

		} else if (code.equalsIgnoreCase("CH")) {
			System.out.println("Chapitre selected");

			long id = Long.parseLong(getSubString(selectedNode, 1, "-"));

			chapter = jJChapterService.findJJChapter(id);

			rendredTestCaseRecaps = true;
		}

		else if (code.equalsIgnoreCase("TC")) {
			System.out.println("Testcase selected");
			rendredTestCaseRecaps = false;

		}

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Selected " + event.getTreeNode().toString(), event
						.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	private String getSubString(String s, int index, String c) {
		String[] temp = s.split(c);
		return temp[index];
	}

	// Recursive function to create tree
	private TreeNode createTree(JJChapter chapterParent, TreeNode rootNode,
			JJProject project, JJProduct product, JJCategory category) {

		TreeNode newNode = new DefaultTreeNode("CH-" + chapterParent.getId()
				+ "- " + chapterParent.getName(), rootNode);

		SortedMap<Integer, Object> elements = getSortedElements(chapterParent,
				project, product, category, true);
		for (Map.Entry<Integer, Object> entry : elements.entrySet()) {
			String className = entry.getValue().getClass().getSimpleName();

			if (className.equalsIgnoreCase("JJChapter")) {

				JJChapter chapter = (JJChapter) entry.getValue();
				TreeNode newNode2 = createTree(chapter, newNode, project,
						product, category);

			} else if (className.equalsIgnoreCase("JJRequirement")) {

				JJRequirement requirement = (JJRequirement) entry.getValue();
				List<JJTestcase> testcases = jJTestcaseService.getTestcases(
						requirement, null, true, true);
				for (JJTestcase testcase : testcases) {
					TreeNode newNode3 = new DefaultTreeNode("TC-"
							+ testcase.getId() + "- " + testcase.getName(),
							newNode);
				}

			}

		}

		newNode.setExpanded(true);

		return newNode;
	}

	public SortedMap<Integer, Object> getSortedElements(JJChapter parent,
			JJProject project, JJProduct product, JJCategory category,
			boolean onlyActif) {

		SortedMap<Integer, Object> elements = new TreeMap<Integer, Object>();

		if (parent != null) {

			List<JJChapter> chapters = jJChapterService
					.getChildrenOfParentChapter(parent, onlyActif, true);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering(), chapter);
			}

			List<JJRequirement> requirements = jJRequirementService
					.getRequirementChildrenWithChapterSortedByOrder(parent,
							onlyActif);

			for (JJRequirement requirement : requirements) {
				elements.put(requirement.getOrdering(), requirement);

			}
		} else {

			List<JJChapter> chapters = jJChapterService.getParentsChapter(
					project, product, category, onlyActif, true);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering(), chapter);
			}
		}

		return elements;

	}

	public class TestCaseRecap {

		private JJTestcase testcase;

		private String status;

		private boolean rendered;

		public TestCaseRecap(JJTestcase testcase) {
			super();
			this.testcase = testcase;

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJBuildBean jJBuildBean = (JJBuildBean) session
					.getAttribute("jJBuildBean");

			JJBuild build = jJBuildBean.getBuild();

			if (build != null) {
				rendered = false;

				JJTestcaseexecution testcaseexecution = jJTestcaseexecutionService
						.getTestcaseexecution(testcase, build, true);

				if (testcaseexecution != null) {
					if (testcaseexecution.getPassed()) {
						status = "SUCCESS";
					} else {
						status = "FAILED";
					}
				} else {
					status = "Not Run";
				}

			} else {
				rendered = true;
				status = "Select a Build";
			}

		}

		public JJTestcase getTestcase() {
			return testcase;
		}

		public void setTestcase(JJTestcase testcase) {
			this.testcase = testcase;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public boolean getRendered() {
			return rendered;
		}

		public void setRendered(boolean rendered) {
			this.rendered = rendered;
		}
	}

}
