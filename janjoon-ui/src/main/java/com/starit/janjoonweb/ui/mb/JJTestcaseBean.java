package com.starit.janjoonweb.ui.mb;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTestcaseexecutionService;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJTeststepService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJTestcase.class, beanName = "jJTestcaseBean")
public class JJTestcaseBean {

	@Autowired
	public JJConfigurationService jJConfigurationService;

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

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

	@Autowired
	JJTeststepService jJTeststepService;

	public void setjJTeststepService(JJTeststepService jJTeststepService) {
		this.jJTeststepService = jJTeststepService;
	}

	@Autowired
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	private JJTestcase testcase;

	private JJProject project;
	private JJProduct product;
	private JJVersion version;

	private JJChapter chapter;

	private JJCategory category;

	private TreeNode rootNode;
	private TreeNode selectedNode;

	private List<TestCaseRecap> testCaseRecaps;
	private boolean rendredTestCaseRecaps;
	private boolean rendredTestCaseHistorical;
	private boolean rendredEmptySelection;

	private String message;

	private JJRequirement requirement;
	private List<JJRequirement> requirements;

	private JJTask task;

	private boolean disabledTestcaseMode;
	private boolean disabledTeststepMode;

	private boolean initiateTask;
	private boolean disabledInitTask;
	private boolean disabledTask;
	private boolean disabledExport;
	private boolean testcaseState;

	private String namefile;

	private List<JJCategory> categoryList;

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

	public JJCategory getCategory() {
		return category;
	}

	public void setCategory(JJCategory category) {
		this.category = category;
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
				chapter, true, true, false);

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

	public boolean getRendredTestCaseHistorical() {
		return rendredTestCaseHistorical;
	}

	public void setRendredTestCaseHistorical(boolean rendredTestCaseHistorical) {
		this.rendredTestCaseHistorical = rendredTestCaseHistorical;
	}

	public boolean getRendredEmptySelection() {
		return rendredEmptySelection;
	}

	public void setRendredEmptySelection(boolean rendredEmptySelection) {
		this.rendredEmptySelection = rendredEmptySelection;
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

	public boolean getDisabledTestcaseMode() {
		return disabledTestcaseMode;
	}

	public void setDisabledTestcaseMode(boolean disabledTestcaseMode) {
		this.disabledTestcaseMode = disabledTestcaseMode;
	}

	public boolean getDisabledTeststepMode() {
		return disabledTeststepMode;
	}

	public void setDisabledTeststepMode(boolean disabledTeststepMode) {
		this.disabledTeststepMode = disabledTeststepMode;
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

	public boolean getDisabledExport() {
		return disabledExport;
	}

	public void setDisabledExport(boolean disabledExport) {
		this.disabledExport = disabledExport;
	}

	public String getNamefile() {
		return namefile;
	}

	public void setNamefile(String namefile) {
		this.namefile = namefile;
	}

	public List<JJCategory> getCategoryList() {
		categoryList = jJCategoryService.getCategories(null, false, true, true);
		return categoryList;
	}

	public void setCategoryList(List<JJCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public void loadData() {

		this.getProject();
		this.getProduct();
		this.getVersion();

		chapter = null;
		testcase = null;
		rendredTestCaseRecaps = false;
		rendredTestCaseHistorical = false;
		requirement = null;

		disabledExport = true;

		namefile = null;

		if (category != null) {
			namefile = category.getName().trim();
			disabledExport = false;
		} else {

			namefile = null;
			disabledExport = true;

		}

		createTestcaseTree();

	}

	public void newTestcase(JJTeststepBean jJTeststepBean) {
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

		disabledTestcaseMode = false;
		disabledTeststepMode = true;
		testcaseState = true;

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJBuildBean jJBuildBean = (JJBuildBean) session
				.getAttribute("jJBuildBean");

		jJBuildBean.setBuild(null);

		jJTeststepBean.newTeststep();
		jJTeststepBean.setActionTeststep(false);
	}

	public void addMessage() {

	}

	public void loadTask() {

		disabledTask = !initiateTask;

		if (initiateTask) {
			task = new JJTask();
			task.setEnabled(true);
			task.setCreationDate(new Date());
			task.setStartDatePlanned(new Date());
			task.setWorkloadPlanned(8);
			task.setEndDatePlanned(new Date(task.getStartDatePlanned()
					.getTime()
					+ task.getWorkloadPlanned().longValue()
					* 3600000));

		} else {
			task = new JJTask();
		}

	}

	public void editTestcase(JJTeststepBean jJTeststepBean) {

		message = "Edit Testcase";

		JJTestcase tc = jJTestcaseService.findJJTestcase(testcase.getId());
		requirement = tc.getRequirement();

		chapter = requirement.getChapter();

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

		disabledTestcaseMode = false;
		disabledTeststepMode = false;
		testcaseState = false;

		jJTeststepBean.newTeststep();
		jJTeststepBean.setActionTeststep(false);
	}

	public void runTestcase(JJTestcaseexecutionBean jJTestcaseexecutionBean,
			JJTeststepexecutionBean jJTeststepexecutionBean) {

		jJTestcaseexecutionBean.loadTestcaseexecution(jJTeststepexecutionBean);

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJBuildBean jJBuildBean = (JJBuildBean) session
				.getAttribute("jJBuildBean");

		if (jJBuildBean == null) {
			jJBuildBean = new JJBuildBean();
		}

		JJBuild build = jJBuildBean.getBuild();

		List<JJTask> tasks = jJTaskService.getTasks(null, null, null, null,
				null, null, testcase, build, true, false, true, null);
		if (tasks.isEmpty()) {
			tasks = jJTaskService.getTasks(null, null, null, null, null, null,
					testcase, null, true, false, false, null);
			if (!tasks.isEmpty()) {

				JJTask task1 = tasks.get(0);
				JJTask task = new JJTask();

				task.setName(testcase.getName() + "_"
						+ build.getName().trim().toUpperCase());
				task.setCreationDate(new Date());
				task.setDescription("This is task " + task.getName());
				task.setEnabled(true);

				task.setStartDateReal(new Date());
				task.setEndDateReal(null);
				task.setWorkloadReal(null);

				task.setTestcase(testcase);
				task.setBuild(build);

				task.setAssignedTo(task1.getAssignedTo());

				task.setStartDateReal(new Date());

				task.setStartDateRevised(task1.getStartDateRevised());
				task.setEndDateRevised(task1.getEndDateRevised());
				task.setWorkloadRevised(task1.getWorkloadRevised());

				task.setStartDatePlanned(task1.getStartDatePlanned());
				task.setEndDatePlanned(task1.getEndDatePlanned());
				task.setWorkloadPlanned(task1.getWorkloadPlanned());

				// task.setCompleted(task1.getCompleted());
				// task.setConsumed(task1.getConsumed());

				jJTaskService.saveJJTask(task);

			}

		} else {
			JJTask task = tasks.get(0);
			task.setName(testcase.getName() + "_"
					+ build.getName().trim().toUpperCase());
			task.setStartDateReal(new Date());
			task.setEndDateReal(null);
			task.setWorkloadReal(null);
			task.setUpdatedDate(new Date());
			jJTaskService.updateJJTask(task);
		}

	}

	public void handleSelectRequirement() {

	}

	public void save() {

		if (testcase.getId() == null) {

			manageTestcaseOrder(requirement);

			testcase.setRequirement(requirement);
			requirement.getTestcases().add(testcase);

			if (initiateTask) {
				task.setName(testcase.getName());
				task.setDescription("This is Task: " + task.getName());
				testcase.getTasks().add(task);
				task.setTestcase(testcase);
			}

			jJTestcaseService.saveJJTestcase(testcase);

			disabledInitTask = true;
			disabledTask = true;

			disabledTestcaseMode = true;
			disabledTeststepMode = false;

			FacesContext.getCurrentInstance().addMessage(
					null,
					MessageFactory.getMessage("message_successfully_created",
							"JJTestcase"));

		}

	}

	public void saveAndclose(JJTeststepBean jJTeststepBean) {

		RequestContext context = RequestContext.getCurrentInstance();

		if (testcaseState) {

			if (getTestcaseDialogConfiguration()) {
				context.execute("testcaseDialogWidget.hide()");
			} else {
				newTestcase(jJTeststepBean);
			}

		} else {

			JJTestcase tc;

			if (jJTeststepBean.getActionTeststep()) {
				String name = testcase.getName();
				String description = testcase.getDescription();
				Boolean automatic = testcase.getAutomatic();

				tc = jJTestcaseService.findJJTestcase(testcase.getId());
				tc.setName(name);
				tc.setDescription(description);
				tc.setAutomatic(automatic);

			} else {
				tc = testcase;
			}

			tc.setUpdatedDate(new Date());

			if (!requirement.equals(tc.getRequirement())) {

				tc.setRequirement(requirement);

			}

			jJTestcaseService.updateJJTestcase(tc);

			requirement.getTestcases().add(tc);

			FacesContext.getCurrentInstance().addMessage(
					null,
					MessageFactory.getMessage("message_successfully_updated",
							"JJTestcase"));

			context.execute("testcaseDialogWidget.hide()");

		}

	}

	public void closeDialog() {

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
				requirement.getChapter(), false, false, false);

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

		if (category != null) {

			TreeNode categoryNode = new DefaultTreeNode("C-" + category.getId()
					+ "- " + category.getName(), projectNode);

			categoryNode.setExpanded(true);

			List<JJChapter> parentChapters = jJChapterService
					.getParentsChapter(project, category, true, true);

			for (JJChapter chapter : parentChapters) {
				TreeNode node = createTree(chapter, categoryNode, category);
			}

		}
		rendredEmptySelection = true;

	}

	public void onNodeSelect(NodeSelectEvent event) {

		String selectedNode = event.getTreeNode().toString();

		String code = getSubString(selectedNode, 0, "-");

		if (code.equalsIgnoreCase("P")) {

			rendredTestCaseRecaps = false;
			rendredTestCaseHistorical = false;
			rendredEmptySelection = false;

		} else if (code.equalsIgnoreCase("C")) {

			rendredTestCaseRecaps = false;
			rendredTestCaseHistorical = false;
			rendredEmptySelection = false;

		} else if (code.equalsIgnoreCase("CH")) {

			long id = Long.parseLong(getSubString(selectedNode, 1, "-"));

			chapter = jJChapterService.findJJChapter(id);
			testcase = null;

			rendredTestCaseRecaps = true;
			rendredTestCaseHistorical = false;
			rendredEmptySelection = false;
		}

		else if (code.equalsIgnoreCase("TC")) {

			long id = Long.parseLong(getSubString(selectedNode, 1, "-"));

			testcase = jJTestcaseService.findJJTestcase(id);
			chapter = null;

			rendredTestCaseRecaps = false;
			rendredTestCaseHistorical = true;
			rendredEmptySelection = false;

		}
	}

	private String getSubString(String s, int index, String c) {
		String[] temp = s.split(c);
		return temp[index];
	}

	// Recursive function to create tree
	private TreeNode createTree(JJChapter chapterParent, TreeNode rootNode,
			JJCategory category) {

		TreeNode newNode = new DefaultTreeNode("CH-" + chapterParent.getId()
				+ "- " + chapterParent.getName(), rootNode);

		SortedMap<Integer, Object> elements = getSortedElements(chapterParent,
				project, product, category, true);

		SortedMap<Integer, JJTestcase> testcaseElements = new TreeMap<Integer, JJTestcase>();

		for (Map.Entry<Integer, Object> entry : elements.entrySet()) {
			String className = entry.getValue().getClass().getSimpleName();

			if (className.equalsIgnoreCase("JJChapter")) {

				JJChapter chapter = (JJChapter) entry.getValue();
				TreeNode newNode2 = createTree(chapter, newNode, category);

			} else if (className.equalsIgnoreCase("JJRequirement")) {

				JJRequirement requirement = (JJRequirement) entry.getValue();
				List<JJTestcase> testcases = jJTestcaseService.getTestcases(
						requirement, null, true, true, false);
				for (JJTestcase testcase : testcases) {
					testcaseElements.put(testcase.getOrdering(), testcase);
				}
			}
		}

		for (Map.Entry<Integer, JJTestcase> testcaseEntry : testcaseElements
				.entrySet()) {
			JJTestcase testcase = testcaseEntry.getValue();
			String type = getType(testcase);
			TreeNode newNode3 = new DefaultTreeNode(type, "TC-"
					+ testcase.getId() + "- " + testcase.getName(), newNode);

		}

		newNode.setExpanded(true);

		return newNode;
	}

	private String getType(JJTestcase testcase) {

		JJTestcase tc = jJTestcaseService.findJJTestcase(testcase.getId());

		boolean hasTS = false;

		for (JJTeststep teststep : tc.getTeststeps()) {
			if (teststep.getEnabled()) {
				hasTS = true;
				break;
			}
		}

		if (hasTS) {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJBuildBean jJBuildBean = (JJBuildBean) session
					.getAttribute("jJBuildBean");

			if (jJBuildBean == null) {
				jJBuildBean = new JJBuildBean();
			}

			JJBuild build = jJBuildBean.getBuild();

			List<JJTestcaseexecution> testcaseexecutions = jJTestcaseexecutionService
					.getTestcaseexecutions(testcase, build, true, true, false);

			if (build != null) {
				if (testcaseexecutions.isEmpty()) {

					testcaseexecutions = jJTestcaseexecutionService
							.getTestcaseexecutions(testcase, null, true, true,
									false);
					if (testcaseexecutions.isEmpty()) {
						return "TNE";
					} else {
						JJTestcaseexecution testcaseexecution = testcaseexecutions
								.get(0);
						if (testcaseexecution.getPassed() != null) {

							if (testcaseexecution.getPassed()) {
								return "TSWB";
							} else {
								return "TFWB";
							}

						} else {
							return "TNFWB";
						}
					}

				} else {

					JJTestcaseexecution testcaseexecution = testcaseexecutions
							.get(0);
					if (testcaseexecution.getPassed() != null) {

						if (testcaseexecution.getPassed()) {
							return "TS";
						} else {
							return "TF";
						}

					} else {
						return "TNF";
					}

				}
			} else {

				if (testcaseexecutions.isEmpty()) {
					return "TNE";
				} else {

					JJTestcaseexecution testcaseexecution = testcaseexecutions
							.get(0);
					if (testcaseexecution.getPassed() != null) {

						if (testcaseexecution.getPassed()) {
							return "TSWB";
						} else {
							return "TFWB";
						}

					} else {
						return "TNFWB";
					}

				}
			}
		} else {
			return "TWT";
		}

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

	public void preProcessPDF(Object document) throws IOException,
			BadElementException, DocumentException {

		Document pdf = (Document) document;
		pdf.open();
		pdf.setPageSize(PageSize.A4);

		Font fontTitle = new Font(Font.TIMES_ROMAN, 30, Font.BOLD);
		fontTitle.setColor(new Color(0x24, 0x14, 0x14));

		Font fontChapter = new Font(Font.HELVETICA, 15, Font.BOLD);
		fontChapter.setColor(new Color(0x4E, 0x4E, 0x4E));

		Font fontTestcase = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
		fontTestcase.setColor(new Color(0x5A, 0x5A, 0x5A));

		Font fontTeststep = new Font(Font.COURIER, 8, Font.BOLD);
		fontTeststep.setColor(new Color(0x82, 0x82, 0x82));

		StyleSheet style = new StyleSheet();
		style.loadTagStyle("body", "font", "Times New Roman");

		Phrase phrase = new Phrase(20, new Chunk("\n" + category.getName()
				+ "\n" + project.getName() + "\n" + "\n" + "\n", fontChapter));

		Paragraph paragraph = new Paragraph();
		paragraph.add(phrase);

		List<JJChapter> parentChapters = jJChapterService.getParentsChapter(
				project, category, true, true);

		for (JJChapter chapter : parentChapters) {
			createTreeDocument(chapter, category, paragraph, fontTeststep,
					fontChapter, fontTestcase, style);
		}

		paragraph.add(phrase);

		pdf.add(paragraph);

	}

	private void createTreeDocument(JJChapter chapterParent,
			JJCategory category, Paragraph paragraph, Font fontTeststep,
			Font fontChapter, Font fontTestcase, StyleSheet style)
			throws IOException {

		paragraph.add(new Chunk("\n" + chapterParent.getName() + "\n",
				fontChapter));

		SortedMap<Integer, Object> elements = getSortedElements(chapterParent,
				project, product, category, true);

		SortedMap<Integer, JJTestcase> testcaseElements = new TreeMap<Integer, JJTestcase>();

		for (Map.Entry<Integer, Object> entry : elements.entrySet()) {
			String className = entry.getValue().getClass().getSimpleName();

			if (className.equalsIgnoreCase("JJChapter")) {

				JJChapter chapter = (JJChapter) entry.getValue();
				createTreeDocument(chapter, category, paragraph, fontTeststep,
						fontChapter, fontTestcase, style);

			} else if (className.equalsIgnoreCase("JJRequirement")) {

				JJRequirement requirement = (JJRequirement) entry.getValue();
				List<JJTestcase> testcases = jJTestcaseService.getTestcases(
						requirement, null, true, true, false);
				for (JJTestcase testcase : testcases) {
					testcaseElements.put(testcase.getOrdering(), testcase);
				}

			}
		}

		for (Map.Entry<Integer, JJTestcase> testcaseEntry : testcaseElements
				.entrySet()) {

			JJTestcase testcase = testcaseEntry.getValue();

			paragraph.add(new Chunk(testcase.getName() + "\n", fontTestcase));

			List<JJTeststep> teststeps = jJTeststepService.getTeststeps(
					testcase, true, true);

			for (JJTeststep teststep : teststeps) {
				paragraph.add(new Chunk(teststep.getActionstep() + "\t"
						+ teststep.getResultstep() + "\n", fontTeststep));
			}
		}

	}

	public List<JJTestcase> getTestcases() {
		List<JJTestcase> testcases = new ArrayList<JJTestcase>();

		testcases = jJTestcaseService.getTestcases(null, chapter, true, false,
				true);

		return testcases;
	}

	public class TestCaseRecap {

		private JJTestcase testcase;

		private String status;

		private boolean disabled;

		public TestCaseRecap(JJTestcase testcase) {
			super();
			this.testcase = testcase;

			List<JJTeststep> teststeps = jJTeststepService.getTeststeps(
					testcase, true, true);

			if (!teststeps.isEmpty()) {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJBuildBean jJBuildBean = (JJBuildBean) session
						.getAttribute("jJBuildBean");

				JJBuild build = jJBuildBean.getBuild();

				if (build != null) {
					disabled = false;

					List<JJTestcaseexecution> testcaseexecutions = jJTestcaseexecutionService
							.getTestcaseexecutions(testcase, build, true,
									false, true);

					if (!testcaseexecutions.isEmpty()) {
						JJTestcaseexecution testcaseexecution = testcaseexecutions
								.get(0);
						if (testcaseexecution.getPassed() != null) {

							if (testcaseexecution.getPassed()) {
								status = "SUCCESS";
							} else {
								status = "FAILED";
							}
						} else {
							status = "Non Fini";
						}
					} else {
						status = "NOT RUN";
					}

				} else {
					disabled = true;
					status = "Select a Build";
				}
			} else {
				disabled = true;
				status = "This testcase doesn't have teststeps";
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

		public boolean getDisabled() {
			return disabled;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}

	}

	private boolean getTestcaseDialogConfiguration() {
		return jJConfigurationService.getDialogConfig("TestcaseDialog",
				"testcases.create.saveandclose");
	}

}
