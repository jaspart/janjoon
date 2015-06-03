package com.starit.janjoonweb.ui.mb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.component.dialog.Dialog;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.RateEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.xml.sax.SAXParseException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfWriter;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJMessageService;
import com.starit.janjoonweb.domain.JJPermissionService;
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
import com.starit.janjoonweb.ui.mb.util.CategoryUtil;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.ReadXMLFile;
import com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl;
import com.starit.janjoonweb.ui.security.AuthorisationService;

@SuppressWarnings("deprecation")
@RooSerializable
@RooJsfManagedBean(entity = JJTestcase.class, beanName = "jJTestcaseBean")
public class JJTestcaseBean {

	@Autowired
	private JJConfigurationService jJConfigurationService;

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	@Autowired
	private JJPermissionService jJPermissionService;

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	@Autowired
	private JJCategoryService jJCategoryService;

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	@Autowired
	private JJChapterService jJChapterService;

	public void setjJChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

	@Autowired
	private JJTestcaseexecutionService jJTestcaseexecutionService;

	public void setjJTestcaseexecutionService(
			JJTestcaseexecutionService jJTestcaseexecutionService) {
		this.jJTestcaseexecutionService = jJTestcaseexecutionService;
	}

	@Autowired
	private JJBuildService jJBuildService;

	public void setjJBuildService(JJBuildService jJBuildService) {
		this.jJBuildService = jJBuildService;
	}

	@Autowired
	private JJTeststepService jJTeststepService;

	public void setjJTeststepService(JJTeststepService jJTeststepService) {
		this.jJTeststepService = jJTeststepService;
	}

	@Autowired
	private JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	@Autowired
	private JJMessageService jJMessageService;

	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	private Integer rated;
	private JJTestcase testcase;
	private JJProject project;
	private JJProduct product;
	private JJVersion version;
	private float reqCoverage;
	private List<JJBuild> builds;
	private String width;
	private JJTestcase copyTestcase;

	private List<JJBuild> rowNames = new ArrayList<JJBuild>();
	private List<JJTestcase> colNames = new ArrayList<JJTestcase>();
	private ArrayList<ArrayList<Boolean>> value = new ArrayList<ArrayList<Boolean>>();

	public Integer getRated() {
		return rated;
	}

	public void setRated(Integer rated) {
		this.rated = rated;
	}

	public float getReqCoverage() {
		return reqCoverage;
	}

	public void setReqCoverage(float reqCoverage) {
		this.reqCoverage = reqCoverage;
	}

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
	private List<CategoryUtil> categoryList;
	private List<JJMessage> communicationMessages;

	public List<JJBuild> getAvailableBuilds() {

		return jJBuildService.getBuilds(LoginBean.getProduct(),
				LoginBean.getVersion(), true);
	}

	public boolean isTestcaseState() {
		return testcaseState;
	}

	public void setTestcaseState(boolean testcaseState) {
		this.testcaseState = testcaseState;
	}

	public List<JJBuild> getRowNames() {

		if (rowNames == null || rowNames.isEmpty()) {

			colNames = jJTestcaseService
					.getImportTestcases(category, LoginBean.getProject(),
							LoginBean.getProduct(), true, false);
			// rowNames=new ArrayList<Object>();
			if (colNames != null && !colNames.isEmpty()) {

				JJBuild build = ((JJBuildBean) LoginBean
						.findBean("jJBuildBean")).getBuild();

				if (build == null) {
					if (rowNames == null)
						rowNames = new ArrayList<JJBuild>(
								((JJBuildBean) LoginBean
										.findBean("jJBuildBean")).getBuilds());
					else
						rowNames.addAll(new ArrayList<JJBuild>(
								((JJBuildBean) LoginBean
										.findBean("jJBuildBean")).getBuilds()));
				} else {
					if (rowNames == null)
						rowNames = new ArrayList<JJBuild>();

					rowNames.add(build);
				}
				if (!rowNames.isEmpty()) {
					for (int i = 0; i < colNames.size(); i++) {
						value.add(new ArrayList<Boolean>());
						for (int j = 0; j < rowNames.size(); j++) {
							value.get(i).add(
									jJTestcaseService
											.findJJTestcase(
													colNames.get(i).getId())
											.getBuilds()
											.contains(rowNames.get(j)));
						}
					}
				} else {
					rowNames = null;
					colNames = null;
				}

			}
			if (rowNames != null) {
				width = 180 + (rowNames.size() * 70) + "px";

				if (180 + (rowNames.size() * 70) > 1000)
					width = "100%";
			} else
				width = 170 + "px";

		}
		return rowNames;
	}

	public void setRowNames(List<JJBuild> rowNames) {
		this.rowNames = rowNames;
	}

	public List<JJTestcase> getColNames() {
		return colNames;
	}

	public void setColNames(List<JJTestcase> colNames) {
		this.colNames = colNames;
	}

	public ArrayList<ArrayList<Boolean>> getValue() {
		return value;
	}

	public void setValue(ArrayList<ArrayList<Boolean>> value) {
		this.value = value;
	}

	public void onCellEdit(int colIdx, int rowIdx) {
		System.err.println("void");
		JJTestcase columnName = jJTestcaseService.findJJTestcase(colNames.get(
				colIdx).getId());
		JJBuild rowName = ((JJBuildBean) LoginBean.findBean("jJBuildBean")).jJBuildService
				.findJJBuild(((JJBuild) rowNames.get(rowIdx)).getId());
		boolean successOperation = false;
		if (value.get(colIdx).get(rowIdx)) {
			System.err.println("Size = " + columnName.getBuilds().size());
			successOperation = columnName.getBuilds().remove((JJBuild) rowName);
			System.err.println("Size = " + columnName.getBuilds().size());

		} else {
			System.err.println("Size = " + columnName.getBuilds().size());
			successOperation = columnName.getBuilds().add((JJBuild) rowName);

			System.err.println("Size = " + columnName.getBuilds().size());

		}

		if (successOperation) {

			updateJJTestcase(columnName);
			colNames.set(colIdx,
					jJTestcaseService.findJJTestcase(columnName.getId()));
			value.get(colIdx).set(rowIdx, !value.get(colIdx).get(rowIdx));
			createTestcaseTree();
			FacesContext.getCurrentInstance().addMessage(
					null,
					MessageFactory.getMessage("message_successfully_updated",
							"Testcase"));
			// colNames=null;
			// rowNames=null;
		}

	}

	public JJTestcase getTestcase() {
		return testcase;
	}

	public void setTestcase(JJTestcase testcase) {
		this.testcase = testcase;
	}

	public Integer getScrollWidth() {
		if (180 + (rowNames.size() * 70) > 1110)
			return 1100;
		else
			return null;
	}

	public Integer getScrollHeight() {
		if ((colNames.size() * 50) > 610)
			return 600;
		else
			return null;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public JJProject getProject() {
		this.project = LoginBean.getProject();
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public JJProduct getProduct() {

		this.product = LoginBean.getProduct();
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
				chapter, null, true, true, false);

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

	public boolean getRenderCommentsPanel() {
		boolean returnValue = ((LoginBean) LoginBean.findBean("loginBean"))
				.isEnable()
				&& testcase != null
				&& rendredTestCaseHistorical
				&& !rendredTestCaseRecaps;
		return returnValue;
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

		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		requirements = jJRequirementService.getRequirements(
				((LoginBean) LoginBean.findBean("loginBean")).getContact()
						.getCompany(), null,
				new HashMap<JJProject, JJProduct>(), null, null, chapter, true,
				true, true, false, null);

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

	public List<JJBuild> getBuilds() {
		return builds;
	}

	public void setBuilds(List<JJBuild> builds) {
		this.builds = builds;
	}

	public List<CategoryUtil> getCategoryList() {

		if (categoryList == null)
			categoryList = CategoryUtil.getCategoryList(
					jJCategoryService.getCategories(null, false, true, true),
					category);
		return categoryList;
	}

	public void setCategoryList(List<CategoryUtil> categoryList) {
		this.categoryList = categoryList;
	}

	public JJTestcase getCopyTestcase() {
		return copyTestcase;
	}

	public void setCopyTestcase(JJTestcase copyTestcase) {
		this.copyTestcase = copyTestcase;
	}

	public List<JJMessage> getCommunicationMessages() {
		if (testcase != null && testcase.getId() != null
				&& testcase.getId() != 0) {
			communicationMessages = jJMessageService.getCommMessages(testcase);
			return communicationMessages;
		} else
			return new ArrayList<JJMessage>();
	}

	public void setCommunicationMessages(List<JJMessage> communicationMessages) {
		this.communicationMessages = communicationMessages;
	}

	public void loadData(Dialog dialog, JJCategory c, boolean projNull) {

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String value = request.getParameter("testcase");
		try {
			long id = Long.parseLong(value);
			JJProductBean jJProductBean = ((JJProductBean) LoginBean
					.findBean("jJProductBean"));
			JJVersionBean jJVersionBean = ((JJVersionBean) LoginBean
					.findBean("jJVersionBean"));
			JJProjectBean jJProjectBean = ((JJProjectBean) LoginBean
					.findBean("jJProjectBean"));

			testcase = jJTestcaseService.findJJTestcase(id);

			boolean show = testcase != null;

			if (show)
				show = testcase.getEnabled();

			if (show)
				show = jJPermissionService.isAuthorized(((LoginBean) LoginBean
						.findBean("loginBean")).getContact(), testcase
						.getRequirement().getProject(), testcase
						.getRequirement().getProduct(), "Testcase");

			if (show
					&& !jJProjectBean.getProjectList().contains(
							testcase.getRequirement().getProject()))
				show = false;

			if (show) {

				boolean change = false;
				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);

				if (jJProjectBean.getProject() == null) {
					change = true;

					jJProjectBean.setProject(testcase.getRequirement()
							.getProject());
					jJProductBean.setProduct(testcase.getRequirement()
							.getProduct());
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(testcase.getRequirement()
							.getVersioning());
					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());
				} else if (!jJProjectBean.getProject().equals(
						testcase.getRequirement().getProject())) {
					change = true;
					jJProjectBean.setProject(testcase.getRequirement()
							.getProject());
					jJProductBean.setProduct(testcase.getRequirement()
							.getProduct());
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(testcase.getRequirement()
							.getVersioning());
					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());
				} else if (testcase.getRequirement().getProduct() != null
						&& jJProductBean.getProduct() != null) {
					if (!testcase.getRequirement().getProduct()
							.equals(jJProductBean.getProduct())) {
						change = true;
						jJProductBean.setProduct(testcase.getRequirement()
								.getProduct());
						jJVersionBean.getVersionList();
						jJVersionBean.setVersion(testcase.getRequirement()
								.getVersioning());
						session.setAttribute("jJTaskBean", new JJTaskBean());
						session.setAttribute("jJSprintBean", new JJSprintBean());
					} else if (testcase.getRequirement().getVersioning() != null
							&& jJVersionBean.getVersion() != null) {
						if (!testcase.getRequirement().getVersioning()
								.equals(jJVersionBean.getVersion())) {
							change = true;
							jJVersionBean.getVersionList();
							jJVersionBean.setVersion(testcase.getRequirement()
									.getVersioning());
						}
					}
				}

				if (change) {

					((LoginBean) LoginBean.findBean("loginBean"))
							.setAuthorisationService(new AuthorisationService(
									session, ((LoginBean) LoginBean
											.findBean("loginBean"))
											.getContact()));

					session.setAttribute("jJBugBean", new JJBugBean());
					session.setAttribute("jJMessageBean", null);
					session.setAttribute("jJRequirementBean", null);

					if (session.getAttribute("requirementBean") != null)
						((RequirementBean) session
								.getAttribute("requirementBean"))
								.setRootNode(null);
				}

				category = testcase.getRequirement().getCategory();
				this.getProject();
				this.getProduct();
				this.getVersion();
				rated = (((LoginBean) LoginBean.findBean("loginBean"))
						.getContact().getTestcases().contains(testcase)) ? 1
						: 0;

				chapter = null;

				requirement = null;
				rowNames = null;
				colNames = null;
				disabledExport = true;
				disabledExport = category == null;
				createTestcaseTree();
				rowNames = null;
				colNames = null;
				rendredEmptySelection = true;
				categoryList = null;
				chapter = null;
				rendredTestCaseRecaps = false;
				rendredTestCaseHistorical = true;
				rendredEmptySelection = false;

			} else {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"validator_page_access", "Testcase");
				facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
				((LoginBean) LoginBean.findBean("loginBean"))
						.setFacesMessage(facesMessage);
				project = null;
				category = null;
				id = Long.parseLong("gfbfghgf");

			}

		} catch (NumberFormatException e) {

			if (projNull)
				this.project = null;
			if (dialog != null)
				dialog.setVisible(false);

			if (c != null)
				category = c;

			if (project == null) {
				this.getProject();
				this.getProduct();
				this.getVersion();

				chapter = null;
				testcase = null;
				rendredTestCaseRecaps = false;
				rendredTestCaseHistorical = false;
				requirement = null;
				rowNames = null;
				colNames = null;
				disabledExport = true;
				if (category == null) {
					category = ((LoginBean) LoginBean.findBean("loginBean"))
							.getAuthorisationService().getCategory();
				}

				disabledExport = category == null;

				if (project != null)
					createTestcaseTree();
				rendredEmptySelection = true;
				categoryList = null;
			}
		}

	}

	public void newTestcase(JJTeststepBean jJTeststepBean) {
		message = "test_create_header";
		testcase = new JJTestcase();
		testcase.setEnabled(true);
		testcase.setAutomatic(false);
		requirement = null;

		task = new JJTask();

		initiateTask = false;
		disabledInitTask = false;
		disabledTask = true;

		disabledTestcaseMode = false;
		disabledTeststepMode = true;
		testcaseState = true;
		builds = new ArrayList<JJBuild>();

		// HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
		// .getExternalContext().getSession(false);
		//
		// JJBuildBean jJBuildBean = (JJBuildBean) session
		// .getAttribute("jJBuildBean");
		//
		// jJBuildBean.setBuild(null);

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

	public void pasteTestcase(JJTeststepBean jJTeststepBean) {
		message = "test_paste_header";

		testcase = new JJTestcase();
		testcase.setName(copyTestcase.getName());
		testcase.setDescription(copyTestcase.getDescription());
		testcase.setEnabled(true);
		testcase.setAutomatic(copyTestcase.getAutomatic());
		requirement = null;

		task = new JJTask();

		initiateTask = false;
		disabledInitTask = false;
		disabledTask = true;

		disabledTestcaseMode = false;
		disabledTeststepMode = true;
		testcaseState = true;
		builds = new ArrayList<JJBuild>();

		testcase.setTeststeps(new HashSet<JJTeststep>());
		for (JJTeststep teststep : jJTeststepService.getTeststeps(copyTestcase,
				true, true)) {
			JJTeststep ts = new JJTeststep();
			ts.setName(teststep.getName());
			ts.setDescription(teststep.getDescription());
			ts.setCreationDate(new Date());
			ts.setActionstep(teststep.getActionstep());
			ts.setResultstep(teststep.getResultstep());
			ts.setEnabled(true);
			ts.setOrdering(teststep.getOrdering());
			ts.setTestcase(testcase);
			testcase.getTeststeps().add(ts);

		}
		jJTeststepBean.newTeststep();
		jJTeststepBean.setActionTeststep(false);

	}

	public void editTestcase(JJTeststepBean jJTeststepBean) {

		message = "test_edit_header";

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
		builds = new ArrayList<JJBuild>(tc.getBuilds());

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
		JJTaskBean jJTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
		if (jJTaskBean == null)
			jJTaskBean = new JJTaskBean();

		if (jJBuildBean == null) {
			jJBuildBean = new JJBuildBean();
		}

		JJBuild build = jJBuildBean.getBuild();

		List<JJTask> tasks = jJTaskService.getTasks(null, null, null, null,
				null, false, null, testcase, build, true, false, true, null);
		if (tasks.isEmpty()) {
			tasks = jJTaskService.getTasks(null, null, null, null, null, false,
					null, testcase, null, true, false, false, null);
			if (!tasks.isEmpty()) {

				JJTask task1 = tasks.get(0);
				JJTask task = new JJTask();

				task.setName(testcase.getName() + "_"
						+ build.getName().trim().toUpperCase());
				task.setDescription("This is task " + task.getName());
				task.setEnabled(true);

				task.setStartDateReal(new Date());
				task.setEndDateReal(null);
				task.setWorkloadReal(null);

				task.setTestcase(testcase);
				// task.setBuild(build);
				task.setAssignedTo(task1.getAssignedTo());

				task.setStartDateReal(new Date());

				task.setStartDateRevised(task1.getStartDateRevised());
				task.setEndDateRevised(task1.getEndDateRevised());
				task.setWorkloadRevised(task1.getWorkloadRevised());

				task.setStartDatePlanned(task1.getStartDatePlanned());
				task.setEndDatePlanned(task1.getEndDatePlanned());
				task.setWorkloadPlanned(task1.getWorkloadPlanned());

				jJTaskBean.saveJJTask(task, false);
				build.getTasks().add(task);
				jJBuildBean.saveJJBuild(build);

			}

		} else {
			JJTask task = tasks.get(0);
			task.setName(testcase.getName() + "_"
					+ build.getName().trim().toUpperCase());
			task.setStartDateReal(new Date());
			task.setEndDateReal(null);
			task.setWorkloadReal(null);
			jJTaskBean.saveJJTask(task, true);
			build.getTasks().add(task);
			jJBuildBean.saveJJBuild(build);
		}

	}

	public void handleSelectRequirement() {

	}

	public void save() {

		if (testcase.getId() == null) {
			manageTestcaseOrder(requirement);
			testcase.setRequirement(requirement);
			requirement.getTestcases().add(testcase);
			testcase.setBuilds(new HashSet<JJBuild>(builds));
			saveJJTestcase(testcase);

			if (testcase.getTeststeps() != null
					&& !testcase.getTeststeps().isEmpty()) {
				JJTeststepBean jJTeststepBean = ((JJTeststepBean) LoginBean
						.findBean("jJTeststepBean"));
				for (JJTeststep ts : testcase.getTeststeps()) {
					jJTeststepBean.saveJJTeststep(ts);
				}

				jJTeststepBean.getTeststeps();
			}

			disabledInitTask = true;
			disabledTask = true;

			disabledTestcaseMode = true;
			disabledTeststepMode = false;

			FacesContext.getCurrentInstance().addMessage(
					null,
					MessageFactory.getMessage("message_successfully_created",
							"Testcase"));

		}

	}

	public void saveAndclose(JJTeststepBean jJTeststepBean) {

		RequestContext context = RequestContext.getCurrentInstance();
		if (testcaseState) {

			if (getTestcaseDialogConfiguration()) {
				context.execute("PF('testcaseDialogWidget').hide()");
			} else {
				newTestcase(jJTeststepBean);
			}

		} else {

			if (!requirement.equals(testcase.getRequirement())) {

				testcase.setRequirement(requirement);

			}

			testcase.setBuilds(new HashSet<JJBuild>(builds));
			updateJJTestcase(testcase);
			testcase = jJTestcaseService.findJJTestcase(testcase.getId());
			requirement.getTestcases().add(testcase);

			FacesContext.getCurrentInstance().addMessage(
					null,
					MessageFactory.getMessage("message_successfully_updated",
							"Testcase"));

			context.execute("PF('testcaseDialogWidget').hide()");

		}
		createTestcaseTree();
		// colNames = null;
		// rowNames = null;

	}

	public void closeDialog() {
		task = null;
		builds = null;
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
				requirement.getChapter(), null, false, false, false);

		for (JJTestcase testcase : testcases) {
			elements.put(testcase.getOrdering(), testcase);
		}

		if (elements.isEmpty()) {

			testcase.setOrdering(0);
		} else {
			testcase.setOrdering(elements.lastKey() + 1);
		}

	}

	public void createTestcaseTree() {

		rowNames = null;
		colNames = null;
		rootNode = new DefaultTreeNode("Root", null);

		JJBuild build = ((JJBuildBean) LoginBean.findBean("jJBuildBean"))
				.getBuild();
		TreeNode projectNode = new DefaultTreeNode("P-" + project.getId()
				+ "- " + project.getName(), rootNode);

		projectNode.setExpanded(true);

		if (category != null) {

			TreeNode categoryNode = new DefaultTreeNode("C-" + category.getId()
					+ "- " + category.getName(), projectNode);

			categoryNode.setExpanded(true);

			List<JJChapter> parentChapters = jJChapterService
					.getParentsChapter(((LoginBean) LoginBean
							.findBean("loginBean")).getContact().getCompany(),
							project, category, true, true);

			for (JJChapter ch : parentChapters) {
				TreeNode node = createTree(ch, categoryNode, category, build);
				if (chapter != null && ch.equals(chapter))
					selectedNode = node;
			}

			List<JJTestcase> testWithOutChapter = jJTestcaseService
					.getImportTestcases(category, LoginBean.getProject(),
							LoginBean.getProduct(), true, true);
			for (JJTestcase test : testWithOutChapter) {
				String type = getType(test);
				TreeNode newNode3 = new DefaultTreeNode(type, "TC-"
						+ test.getId() + "- " + test.getName(), categoryNode);

				if (testcase != null && testcase.equals(test))
					selectedNode = newNode3;

			}

		}

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

			// LoginBean loginBean=(LoginBean) LoginBean.findBean("loginBean");
			long id = Long.parseLong(getSubString(selectedNode, 1, "-"));

			chapter = jJChapterService.findJJChapter(id);
			List<JJRequirement> rqs = jJRequirementService.getRequirements(
					((LoginBean) LoginBean.findBean("loginBean")).getContact()
							.getCompany(), null,
					new HashMap<JJProject, JJProduct>(), null, null, chapter,
					true, true, true, false, null);

			if (rqs.size() > 0) {
				int i = 0;
				for (JJRequirement r : rqs) {
					if (jJRequirementService.haveTestcase(r))
						i = i + 1;
				}
				reqCoverage = (float) (i * 100 / rqs.size());
			} else
				reqCoverage = 0;

			testcase = null;

			rendredTestCaseRecaps = true;
			rendredTestCaseHistorical = false;
			rendredEmptySelection = false;
		}

		else if (code.equalsIgnoreCase("TC")) {

			long id = Long.parseLong(getSubString(selectedNode, 1, "-"));
			rendredTestCaseRecaps = false;

			testcase = jJTestcaseService.findJJTestcase(id);

			try {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								FacesContext.getCurrentInstance()
										.getExternalContext()
										.getRequestContextPath()
										+ "/pages/test.jsf?testcase="
										+ id
										+ "&faces-redirect=true");
			} catch (IOException e) {
			}
			// chapter = null;
			//
			// rendredTestCaseRecaps = false;
			// rendredTestCaseHistorical = true;
			// rendredEmptySelection = false;

		}
	}

	private String getSubString(String s, int index, String c) {
		String[] temp = s.split(c);
		return temp[index];
	}

	// Recursive function to create tree
	private TreeNode createTree(JJChapter chapterParent, TreeNode rootNode,
			JJCategory category, JJBuild build) {

		TreeNode newNode = new DefaultTreeNode("CH-" + chapterParent.getId()
				+ "- " + chapterParent.getName(), rootNode);

		SortedMap<Integer, Object> elements = getSortedElements(chapterParent,
				project, product, category, true);

		SortedMap<Integer, JJTestcase> testcaseElements = new TreeMap<Integer, JJTestcase>();

		for (Map.Entry<Integer, Object> entry : elements.entrySet()) {
			String className = entry.getValue().getClass().getSimpleName();

			if (className.equalsIgnoreCase("JJChapter")) {

				JJChapter chapter = (JJChapter) entry.getValue();
				TreeNode newNode2 = createTree(chapter, newNode, category,
						build);

			} else if (className.equalsIgnoreCase("JJRequirement")) {

				JJRequirement requirement = (JJRequirement) entry.getValue();
				List<JJTestcase> testcases = jJTestcaseService.getTestcases(
						requirement, null, build, true, true, false);
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
				if (chapter.getOrdering() != null)
					elements.put(chapter.getOrdering(), chapter);
				else
					elements.put(0, chapter);
			}

			List<JJRequirement> requirements = jJRequirementService
					.getRequirementChildrenWithChapterSortedByOrder(
							((LoginBean) LoginBean.findBean("loginBean"))
									.getContact().getCompany(), parent,
							onlyActif);

			for (JJRequirement requirement : requirements) {
				if (requirement.getOrdering() != null)
					elements.put(requirement.getOrdering(), requirement);
				else
					elements.put(0, requirement);

			}
		} else {

			List<JJChapter> chapters = jJChapterService.getParentsChapter(
					((LoginBean) LoginBean.findBean("loginBean")).getContact()
							.getCompany(), project, category, onlyActif, true);

			for (JJChapter chapter : chapters) {
				elements.put(chapter.getOrdering(), chapter);
			}
		}

		return elements;

	}

	@SuppressWarnings("unchecked")
	public StreamedContent getPreProcessPDF() throws IOException,
			BadElementException, DocumentException {

		Document pdf = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(pdf, baos);
		pdf.open();
		pdf.setPageSize(PageSize.A4);

		Font fontTitle = new Font(FontFamily.TIMES_ROMAN, 30, Font.BOLD);
		fontTitle.setColor(0x24, 0x14, 0x14);

		Font fontChapter = new Font(FontFamily.HELVETICA, 15, Font.BOLD);
		fontChapter.setColor(0x4E, 0x4E, 0x4E);

		Font fontTestcase = new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD);
		fontTestcase.setColor(0x5A, 0x5A, 0x5A);

		Font fontTeststep = new Font(FontFamily.COURIER, 8, Font.BOLD);
		fontTeststep.setColor(0x82, 0x82, 0x82);

		StyleSheet style = new StyleSheet();
		style.loadTagStyle("body", "font", "Times New Roman");
		((LoginBean) LoginBean.findBean("loginBean")).loadStyleSheet(style,
				"test.document.stylesheet");

		Phrase phrase = new Phrase(20, new Chunk("\n" + category.getName()
				+ "\n" + project.getName() + "\n" + "\n" + "\n", fontChapter));

		Paragraph paragraph = new Paragraph();
		paragraph.add(phrase);

		List<JJChapter> parentChapters = jJChapterService.getParentsChapter(
				((LoginBean) LoginBean.findBean("loginBean")).getContact()
						.getCompany(), project, category, true, true);

		JJBuild build = ((JJBuildBean) LoginBean.findBean("jJBuildBean"))
				.getBuild();
		for (JJChapter chapter : parentChapters) {
			createTreeDocument(chapter, build, category, paragraph,
					fontTeststep, fontChapter, fontTestcase, style);
		}

		List<JJTestcase> withOutChapter = jJTestcaseService.getImportTestcases(
				category, project, LoginBean.getProduct(), true, true);

		if (withOutChapter != null && !withOutChapter.isEmpty()) {
			paragraph.add(new Chunk("\n "
					+ MessageFactory.getMessage("test_tree_withOutChapter", "")
							.getDetail() + "\n", fontChapter));

			for (JJTestcase test : withOutChapter) {
				paragraph.add(new Chunk(test.getName() + "\n", fontTestcase));

				List<JJTeststep> teststeps = jJTeststepService.getTeststeps(
						test, true, true);

				for (JJTeststep teststep : teststeps) {

					StringReader strReader = new StringReader(
							teststep.getActionstep());

					List<Element> arrList = HTMLWorkerImpl.parseToList(
							strReader, style);

					for (int i = 0; i < arrList.size(); ++i) {
						Element e = (Element) arrList.get(i);

						if (e.getChunks() != null) {
							for (Chunk chunk : (List<Chunk>) e.getChunks()) {
								if (chunk.getImage() != null) {

									Image img = chunk.getImage();
									paragraph.add(Chunk.NEWLINE);
									paragraph.add(img);
									paragraph.add(Chunk.NEWLINE);

								} else {
									chunk.setFont(fontTeststep);
									paragraph.add(chunk);
								}
							}
						} else {
							paragraph.add(e);
						}
					}
					paragraph.add(Chunk.NEWLINE);

					strReader = new StringReader(teststep.getResultstep());
					arrList = HTMLWorkerImpl.parseToList(strReader, style);

					for (int i = 0; i < arrList.size(); ++i) {
						Element e = (Element) arrList.get(i);

						if (e.getChunks() != null) {
							for (Chunk chunk : (List<Chunk>) e.getChunks()) {
								if (chunk.getImage() != null) {

									Image img = chunk.getImage();
									paragraph.add(Chunk.NEWLINE);
									paragraph.add(img);
									paragraph.add(Chunk.NEWLINE);

								} else {
									chunk.setFont(fontTeststep);
									paragraph.add(chunk);
								}
							}
						} else {
							paragraph.add(e);
						}
					}
				}
			}

		}

		pdf.add(paragraph);
		pdf.close();
		LoginBean.copyUploadImages(false);

		return new DefaultStreamedContent(new ByteArrayInputStream(
				baos.toByteArray()), "pdf", category.getName().toUpperCase()
				.trim()
				+ "-test.pdf");

	}

	public void onCopyEvent() {
		System.err.println(copyTestcase.getName());
		FacesMessage facesMessage = MessageFactory.getMessage(
				"TestCase Successfully copied", FacesMessage.SEVERITY_INFO,
				"TestCase");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	@SuppressWarnings("unchecked")
	private void createTreeDocument(JJChapter chapterParent, JJBuild build,
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
				createTreeDocument(chapter, build, category, paragraph,
						fontTeststep, fontChapter, fontTestcase, style);

			} else if (className.equalsIgnoreCase("JJRequirement")) {

				JJRequirement requirement = (JJRequirement) entry.getValue();
				List<JJTestcase> testcases = jJTestcaseService.getTestcases(
						requirement, null, null, true, true, false);
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

				StringReader strReader = new StringReader(teststep
						.getActionstep()
						.replace("/pages/ckeditor/getimage?imageId=",
								"/images/"));

				List<Element> arrList = HTMLWorker
						.parseToList(strReader, style);

				for (int i = 0; i < arrList.size(); ++i) {
					Element e = (Element) arrList.get(i);

					if (e.getChunks() != null) {
						for (Chunk chunk : (List<Chunk>) e.getChunks()) {
							if (chunk.getImage() != null) {
								Image img = chunk.getImage();
								paragraph.add(img);
							} else {
								chunk.setFont(fontTeststep);
								paragraph.add(chunk);
							}
						}
					} else {
						paragraph.add(e);
					}
				}
				paragraph.add(Chunk.NEWLINE);

				strReader = new StringReader(teststep.getResultstep().replace(
						"/pages/ckeditor/getimage?imageId=", "/images/"));
				arrList = HTMLWorker.parseToList(strReader, style);

				for (int i = 0; i < arrList.size(); ++i) {
					Element e = (Element) arrList.get(i);

					if (e.getChunks() != null) {
						for (Chunk chunk : (List<Chunk>) e.getChunks()) {
							if (chunk.getImage() != null) {
								Image img = chunk.getImage();
								paragraph.add(img);
							} else {
								chunk.setFont(fontTeststep);
								paragraph.add(chunk);
							}
						}
					} else {
						paragraph.add(e);
					}
				}
				paragraph.add(Chunk.NEWLINE);
			}
		}

	}

	public List<JJTestcase> getTestcases() {

		// List<JJTestcase> testcases = new ArrayList<JJTestcase>();
		JJBuild build = ((JJBuildBean) LoginBean.findBean("jJBuildBean"))
				.getBuild();

		return jJTestcaseService.getTestcases(null, chapter, build, true,
				false, true);
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

	public void saveJJTestcase(JJTestcase b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJTestcaseService.saveJJTestcase(b);
	}

	public void updateJJTestcase(JJTestcase b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJTestcaseService.updateJJTestcase(b);
	}

	private boolean getTestcaseDialogConfiguration() {
		return jJConfigurationService.getDialogConfig("TestcaseDialog",
				"testcases.create.saveandclose");
	}

	public void closeXMLDialog() {
		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// import as XML

	public void handleImportXML(FileUploadEvent event) throws IOException {

		JJProduct prod = LoginBean.getProduct();

		JJProject proj = LoginBean.getProject();
		try {
			List<Object> objects = ReadXMLFile.getTestcasesFromXml(this, event
					.getFile().getInputstream(), jJTestcaseService,
					jJCategoryService, jJRequirementService, proj, prod);
			@SuppressWarnings("unchecked")
			List<JJTestcase> testcases = (List<JJTestcase>) objects.get(0);
			@SuppressWarnings("unchecked")
			List<JJTeststep> teststeps = (List<JJTeststep>) objects.get(1);

			if (testcases.isEmpty()) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"No TestCase Found in This File",
						FacesMessage.SEVERITY_WARN, "TestCase");
				FacesContext.getCurrentInstance()
						.addMessage(null, facesMessage);
			} else {

				for (JJTeststep s : teststeps) {
					JJTeststepBean jJTeststepBean = ((JJTeststepBean) LoginBean
							.findBean("jJTeststepBean"));
					if (jJTeststepBean == null)
						jJTeststepBean = new JJTeststepBean();
					jJTeststepBean.saveJJTeststep(s);
				}
				project = null;
				FacesMessage facesMessage = MessageFactory.getMessage(
						testcases.size() + " TestCase and " + teststeps.size()
								+ " TestStep Successfuly added",
						FacesMessage.SEVERITY_INFO, "");
				FacesContext.getCurrentInstance()
						.addMessage(null, facesMessage);
			}
		} catch (SAXParseException e) {
			FacesMessage facesMessage = MessageFactory.getMessage(
					"Error While Parsing File", FacesMessage.SEVERITY_WARN,
					"TestCase");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

	}

	public String getMarginLeft(CategoryUtil cc) {
		int jj = categoryList.indexOf(cc);
		if (jj == -1 || jj == 0)
			return "";
		else {
			if (categoryList.get(jj - 1).getCategory().getStage()
					.equals(cc.getCategory().getStage()))
				return "";
			else
				return "margin-left: 20px;";
		}
	}

	public void testCaseInfo(long id) throws IOException {
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						FacesContext.getCurrentInstance().getExternalContext()
								.getRequestContextPath()
								+ "/pages/test.jsf?testcase="
								+ id
								+ "&faces-redirect=true");
	}

	public StreamedContent getFile() {

		if (category != null) {
			// JJBuild build = ((JJBuildBean) LoginBean.findBean("jJBuildBean"))
			// .getBuild();
			String buffer = "<category name=\""
					+ category.getName().toUpperCase() + "\">";
			List<JJTestcase> tests = jJTestcaseService.getImportTestcases(
					category, project, LoginBean.getProduct(), true, false);
			for (JJTestcase ttt : tests) {
				String description = "";
				StringReader strReader = new StringReader(ttt.getDescription());
				List arrList = null;
				try {
					arrList = HTMLWorker.parseToList(strReader, null);
				} catch (Exception e) {

				}
				for (int i = 0; i < arrList.size(); ++i) {
					description = description
							+ ((Element) arrList.get(i)).toString();
				}
				description = description.replace("[", " ").replace("]", "")
						.replace("&#39;", "'").replace("\"", "'")
						.replace("&&", "and").replace("<", "").replace(">", "");
				String s = "<testcase name=\"" + ttt.getName() + "\""
						+ System.getProperty("line.separator")
						+ "description=\"" + description + "\""
						+ System.getProperty("line.separator")
						+ "enabled=\"1\""
						+ System.getProperty("line.separator") + "Automatic=\""
						+ ((ttt.getAutomatic()) ? 1 : 0) + "\""
						+ System.getProperty("line.separator")
						+ "Requirement=\"" + ttt.getRequirement().getName()
						+ "\" >";
				for (JJTeststep sss : ttt.getTeststeps()) {

					String actionStep = "";

					StringReader str1 = new StringReader(sss.getActionstep());
					List arr1 = null;
					try {
						arr1 = HTMLWorker.parseToList(str1, null);
					} catch (Exception e) {

					}
					for (int i = 0; i < arr1.size(); ++i) {
						actionStep = actionStep
								+ ((Element) arr1.get(i)).toString();
					}
					actionStep = actionStep.replace("[", " ").replace("]", "")
							.replace("&#39;", "'").replace("\"", "'")
							.replace("&&", "and").replace("<", "")
							.replace(">", "");

					String resulstep = "";

					str1 = new StringReader(sss.getResultstep());
					arr1 = null;
					try {
						arr1 = HTMLWorker.parseToList(str1, null);
					} catch (Exception e) {

					}
					for (int i = 0; i < arr1.size(); ++i) {
						resulstep = resulstep
								+ ((Element) arr1.get(i)).toString();
					}
					resulstep = resulstep.replace("[", " ").replace("]", "")
							.replace("&#39;", "'").replace("\"", "'")
							.replace("&&", "and").replace("<", "")
							.replace(">", "");

					String t = "<teststep actionstep=\"" + actionStep
							+ "\" resultstep=\"" + resulstep + "\" />";
					s = s + System.getProperty("line.separator") + t;
				}
				s = s + System.getProperty("line.separator") + "</testcase>";
				buffer = buffer + System.getProperty("line.separator") + s;
			}
			buffer = buffer + System.getProperty("line.separator")
					+ "</category>";
			InputStream stream = new ByteArrayInputStream(buffer.getBytes());
			return new DefaultStreamedContent(stream, "xml", category.getName()
					+ "-Tests.xml");
		} else
			return null;
	}

	public void onrate(RateEvent rateEvent) {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if (!contact.getTestcases().contains(testcase)) {

			((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
			if (((JJMessageBean) LoginBean.findBean("jJMessageBean")) != null) {
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setAlertMessages(null);
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setMainMessages(null);
			}
			contact.getTestcases().add(
					jJTestcaseService.findJJTestcase(testcase.getId()));
			
			if(LoginBean.findBean("jJContactBean") == null)
			{
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJProjectBean", new JJProjectBean());
			}			
			((JJContactBean)LoginBean.findBean("jJContactBean")).updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory.getMessage(
					RequirementBean.REQUIREMENT_SUBSCRIPTION_RATE, "Testcase");
			facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void oncancel() {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if (contact.getTestcases().contains(testcase)) {

			((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
			if (((JJMessageBean) LoginBean.findBean("jJMessageBean")) != null) {
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setAlertMessages(null);
				((JJMessageBean) LoginBean.findBean("jJMessageBean"))
						.setMainMessages(null);
			}
			contact.getTestcases().remove(
					jJTestcaseService.findJJTestcase(testcase.getId()));
			
			if(LoginBean.findBean("jJContactBean") == null)
			{
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJProjectBean", new JJProjectBean());
			}			
			((JJContactBean)LoginBean.findBean("jJContactBean")).updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory.getMessage(
					RequirementBean.REQUIREMENT_SUBSCRIPTION_CANCEL_RATE,
					"Testcase");
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

}
