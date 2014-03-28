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
import com.starit.janjoonweb.domain.JJTask;
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

	private JJTask task;

	private boolean disabled;
	private boolean disabledTeststep;
	private boolean disabledReset;

	private boolean initiateTask;
	private boolean disabledInitTask;
	private boolean disabledTask;

	public JJTestcase getTestcase() {
		return testcase;
	}

	public void setTestcase(JJTestcase testcase) {
		this.testcase = testcase;
	}

	public JJProject getProject() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProjectBean jJProjectBean = (JJProjectBean) session
				.getAttribute("jJProjectBean");
		this.project = jJProjectBean.getProject();
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public JJProduct getProduct() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProductBean jJProductBean = (JJProductBean) session
				.getAttribute("jJProductBean");
		this.product = jJProductBean.getProduct();
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

	public JJTask getTask() {
		return task;
	}

	public void setTask(JJTask task) {
		this.task = task;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean getDisabledTeststep() {
		return disabledTeststep;
	}

	public void setDisabledTeststep(boolean disabledTeststep) {
		this.disabledTeststep = disabledTeststep;
	}

	public boolean getDisabledReset() {
		return disabledReset;
	}

	public void setDisabledReset(boolean disabledReset) {
		this.disabledReset = disabledReset;
	}

	public boolean getInitiateTask() {
		return initiateTask;
	}

	public void setInitiateTask(boolean initiateTask) {
		this.initiateTask = initiateTask;
	}

	public boolean getDisabledInitTask() {
		return disabledInitTask;
	}

	public void setDisabledInitTask(boolean disabledInitTask) {
		this.disabledInitTask = disabledInitTask;
	}

	public boolean getDisabledTask() {
		return disabledTask;
	}

	public void setDisabledTask(boolean disabledTask) {
		this.disabledTask = disabledTask;
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

	public void newTestcase(JJTeststepBean jJTeststepBean) {
		System.out.println("I m in new testcase");
		message = "New Testcase";
		testcase = new JJTestcase();
		testcase.setEnabled(true);
		testcase.setCreationDate(new Date());
		testcase.setAutomatic(false);
		requirement = null;

		task = new JJTask();

		initiateTask = false;
		disabledInitTask = false;
		disabledTask = true;

		disabled = false;
		disabledTeststep = true;
		disabledReset = false;

		jJTeststepBean.newTeststep();
	}

	public void addMessage() {

		System.out
				.println("testcase.getAutomatic() " + testcase.getAutomatic());

	}

	public void loadTask() {

		disabledTask = !initiateTask;

		if (initiateTask) {
			task = new JJTask();
			task.setEnabled(true);
			task.setCreationDate(new Date());
			task.setWorkloadPlanned(8);

		} else {
			task = new JJTask();
		}

	}

	public void editTestcase(JJTeststepBean jJTeststepBean) {
		System.out.println("Edit testcase");
		message = "Edit Testcase";

		System.out.println("testcase.getName() " + testcase.getName());

		JJTestcase tc = jJTestcaseService.findJJTestcase(testcase.getId());
		requirement = tc.getRequirement();

		tc.getTasks();

		Set<JJTask> tasks = tc.getTasks();
		if (tasks.isEmpty()) {
			task = new JJTask();
		} else {
			for (JJTask temTask : tc.getTasks()) {
				task = temTask;
			}
		}

		disabledInitTask = true;
		disabledTask = true;

		disabled = false;
		disabledTeststep = false;
		disabledReset = true;

		jJTeststepBean.newTeststep();

		System.out.println("End testcase");
	}

	public void handleSelectRequirement() {

	}

	public void save() {
		System.out.println("Saving ...");
		String message = "";

		if (testcase.getId() != null) {

			testcase.setUpdatedDate(new Date());

			if (!requirement.equals(testcase.getRequirement())) {

				testcase.setRequirement(requirement);
				requirement.getTestcases().add(testcase);
			}

			jJTestcaseService.updateJJTestcase(testcase);

			message = "message_successfully_updated";
		}

		else {

			manageTestcaseOrder(requirement);

			testcase.setRequirement(requirement);
			requirement.getTestcases().add(testcase);

			if (initiateTask) {
				task.setName(testcase.getName());
				task.setDescription("This Task: " + task.getName());
				testcase.getTasks().add(task);
				task.setTestcase(testcase);
			}

			jJTestcaseService.saveJJTestcase(testcase);

			disabled = true;
			disabledTeststep = false;
			disabledInitTask = true;
			disabledTask = true;

			System.out.println("end saving");

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
		task = null;

		createTestcaseTree();

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTeststepBean jJTeststepBean = (JJTeststepBean) session
				.getAttribute("jJTeststepBean");
		jJTeststepBean.setTeststep(null);
		jJTeststepBean.setTeststeps(null);
	}

	private void manageTestcaseOrder(JJRequirement requirement) {

		SortedMap<Integer, JJTestcase> elements = new TreeMap<Integer, JJTestcase>();

		List<JJTestcase> testcases = jJTestcaseService.getTestcases(null,
				requirement.getChapter(), false, false);

		for (JJTestcase testcase : testcases) {
			elements.put(testcase.getOrdering(), testcase);
		}

		if (elements.isEmpty()) {

			testcase.setOrdering(0);
		} else {
			testcase.setOrdering(elements.lastKey() + 1);
		}

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
					.getParentsChapter(project, category, true, true);

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
			System.out.println("Chapter selected");

			long id = Long.parseLong(getSubString(selectedNode, 1, "-"));

			chapter = jJChapterService.findJJChapter(id);

			rendredTestCaseRecaps = true;
		}

		else if (code.equalsIgnoreCase("TC")) {
			System.out.println("Testcase selected");
			rendredTestCaseRecaps = false;

		}

		// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		// "Selected " + event.getTreeNode().toString(), event
		// .getTreeNode().toString());
		//
		// FacesContext.getCurrentInstance().addMessage(null, message);
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

		SortedMap<Integer, JJTestcase> testcaseElements = new TreeMap<Integer, JJTestcase>();

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
					testcaseElements.put(testcase.getOrdering(), testcase);
				}

			}
		}

		for (Map.Entry<Integer, JJTestcase> testcaseEntry : testcaseElements
				.entrySet()) {
			JJTestcase testcase = testcaseEntry.getValue();
			TreeNode newNode3 = new DefaultTreeNode("TC-" + testcase.getId()
					+ "- " + testcase.getName(), newNode);
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
					project, category, onlyActif, true);

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
