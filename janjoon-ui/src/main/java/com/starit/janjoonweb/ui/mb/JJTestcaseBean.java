package com.starit.janjoonweb.ui.mb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
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

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;
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
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.ReadXMLFile;
import com.starit.janjoonweb.ui.mb.util.TestCaseChartUtil;
import com.starit.janjoonweb.ui.mb.util.itext.HTMLWorkerImpl;
import com.starit.janjoonweb.ui.security.AuthorisationService;

@SuppressWarnings("deprecation")
@RooSerializable
@RooJsfManagedBean(entity = JJTestcase.class, beanName = "jJTestcaseBean")
public class JJTestcaseBean {

	public static final String TEST_SUBSCRIPTION_RATE = "test_subscription_rate";
	public static final String TEST_SUBSCRIPTION_CANCEL_RATE = "test_subscription_cancel_rate";

	@Autowired
	private JJConfigurationService jJConfigurationService;

	@Autowired
	private JJBuildService jJBuildService;

	@Autowired
	private JJTeststepService jJTeststepService;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJMessageService jJMessageService;

	@Autowired
	private JJPermissionService jJPermissionService;

	@Autowired
	private JJCategoryService jJCategoryService;

	@Autowired
	private JJChapterService jJChapterService;

	@Autowired
	private JJTestcaseexecutionService jJTestcaseexecutionService;

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	public void setjJPermissionService(
			JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	public void setjJChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

	public void setjJTestcaseexecutionService(
			JJTestcaseexecutionService jJTestcaseexecutionService) {
		this.jJTestcaseexecutionService = jJTestcaseexecutionService;
	}

	public JJTestcaseexecutionService getjJTestcaseexecutionService() {
		return jJTestcaseexecutionService;
	}

	public void setjJBuildService(JJBuildService jJBuildService) {
		this.jJBuildService = jJBuildService;
	}

	public void setjJTeststepService(JJTeststepService jJTeststepService) {
		this.jJTeststepService = jJTeststepService;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	// private Integer rated;
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
	private JJCategory category;
	private TreeNode rootNode;
	private TreeNode selectedNode;
	private Integer rowCount;
	// private List<TestCaseRecap> testCaseRecaps;
	private List<JJTestcase> testcases;
	private boolean rendredTestCaseRecaps;
	private boolean rendredTestCaseHistorical;
	private boolean rendredEmptySelection;
	private String message;
	private JJRequirement requirement;
	private JJTask task;
	private boolean disabledTestcaseMode;
	private boolean disabledTeststepMode;
	private boolean initiateTask;
	private boolean disabledInitTask;
	private boolean disabledTask;
	private boolean disabledExport;
	private boolean testcaseState;
	private List<JJCategory> categoryList;
	private List<JJMessage> communicationMessages;

	private JJChapter chapter;

	public boolean isRated() {
		return (((LoginBean) LoginBean.findBean("loginBean")).getContact()
				.getTestcases().contains(testcase));
	}

	// public void setRated(Integer rated) {
	// this.rated = rated;
	// }

	public float getReqCoverage() {
		return reqCoverage;
	}

	public void setReqCoverage(float reqCoverage) {
		this.reqCoverage = reqCoverage;
	}

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

		if (!rendredTestCaseRecaps && !rendredTestCaseHistorical) {
			if (rowNames == null || rowNames.isEmpty()) {

				colNames = jJTestcaseService.getImportTestcases(category,
						LoginBean.getProject(), LoginBean.getProduct(), null,
						null, true, false);
				// rowNames=new ArrayList<Object>();
				if (colNames != null && !colNames.isEmpty()) {

					JJBuild build = ((JJBuildBean) LoginBean
							.findBean("jJBuildBean")).getBuild();

					if (build == null) {
						if (rowNames == null)
							rowNames = new ArrayList<JJBuild>(
									((JJBuildBean) LoginBean
											.findBean("jJBuildBean"))
													.getBuilds());
						else
							rowNames.addAll(new ArrayList<JJBuild>(
									((JJBuildBean) LoginBean
											.findBean("jJBuildBean"))
													.getBuilds()));
					} else {
						if (rowNames == null)
							rowNames = new ArrayList<JJBuild>();

						rowNames.add(build);
					}
					if (!rowNames.isEmpty()) {
						value = new ArrayList<ArrayList<Boolean>>();
						for (int i = 0; i < colNames.size(); i++) {
							value.add(new ArrayList<Boolean>());
							boolean val = (colNames.get(i)
									.getAllBuilds() != null
									&& colNames.get(i).getAllBuilds());
							for (int j = 0; j < rowNames.size(); j++) {
								// boolean vall = val;
								// if (!vall)
								// vall = (rowNames.get(j).getAllTestcases() !=
								// null && rowNames
								// .get(j).getAllTestcases());
								//
								// if (!vall)
								// vall = colNames.get(i).getBuilds()
								// .contains(rowNames.get(j));
								value.get(i)
										.add(val || (rowNames.get(j)
												.getAllTestcases() != null
												&& rowNames.get(j)
														.getAllTestcases())
										|| colNames.get(i).getBuilds()
												.contains(rowNames.get(j)));
							}
						}
					} else {
						rowNames = null;
						colNames = null;
					}

				}
				if (rowNames != null) {
					width = 230 + (rowNames.size() * 70) + "";
					width = "width: " + width + "px";

					if (230 + (rowNames.size() * 70) > 910)
						width = "";
				} else
					width = "width: " + 220 + "px";

			}
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

	public JJTestcase getTestcase() {
		return testcase;
	}

	public void setTestcase(JJTestcase testcase) {
		this.testcase = testcase;
	}

	public Integer getScrollWidth() {
		if (180 + (rowNames.size() * 70) > 910)
			return 910;
		else
			return null;
	}

	public Integer getScrollHeight() {
		if ((colNames.size() * 45) > 550)
			return 550;
		else
			return null;
	}

	public boolean getScrollable() {
		return getScrollHeight() != null || getScrollWidth() != null;
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

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public List<TestCaseRecap> getTestCaseRecaps() {

		List<TestCaseRecap> testCaseRecaps = new ArrayList<TestCaseRecap>();
		// List<JJTestcase> testcases = new ArrayList<>();
		if (rendredTestCaseRecaps) {

			// if (testcases == null && chapter != null) {
			// testcases = jJTestcaseService.getTestcases(null, chapter,
			// LoginBean.getVersion(), null, true, true, false);
			//
			// } else if (testcases == null) {
			// testcases = jJTestcaseService.getImportTestcases(category,
			// LoginBean.getProject(), LoginBean.getProduct(),
			// LoginBean.getVersion(), null, true, true);
			// }

			for (JJTestcase testcase : getTestcases()) {
				TestCaseRecap testCaseRecap = new TestCaseRecap(testcase);
				testCaseRecaps.add(testCaseRecap);
			}
		}

		return testCaseRecaps;
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

	public void setRendredTestCaseHistorical(
			boolean rendredTestCaseHistorical) {
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
				.isEnable() && testcase != null && rendredTestCaseHistorical
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

		if (chapter != null) {
			return jJRequirementService.getRequirements(LoginBean.getCompany(),
					null,
					((LoginBean) LoginBean.findBean("loginBean"))
							.getAuthorizedMap("testcase",
									LoginBean.getProject(),
									LoginBean.getProduct()),
					LoginBean.getVersion(), null, chapter, true, true, true,
					false, null);

		} else {
			return jJRequirementService.getRequirementsWithOutChapter(
					LoginBean.getCompany(), category,
					((LoginBean) LoginBean.findBean("loginBean"))
							.getAuthorizedMap("Requirement",
									LoginBean.getProject(),
									LoginBean.getProduct()),
					LoginBean.getVersion(), null, true, true);
		}

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

	public List<JJCategory> getCategoryList() {

		if (categoryList == null)
			categoryList = jJCategoryService.getCategories(null, false, true,
					true, LoginBean.getCompany());
		return categoryList;
	}

	public void setCategoryList(List<JJCategory> categoryList) {
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

	public void setCommunicationMessages(
			List<JJMessage> communicationMessages) {
		this.communicationMessages = communicationMessages;
	}

	public boolean getDisableRun() {
		JJBuildBean jJBuildBean = (JJBuildBean) LoginBean
				.findBean("jJBuildBean");

		JJBuild build = jJBuildBean.getBuild();
		boolean disabled = true;

		if (build != null) {
			disabled = false;
			disabled = build.getAllTestcases() == null
					|| !build.getAllTestcases();
			if (disabled)
				disabled = testcase.getAllBuilds() == null
						|| !testcase.getAllBuilds();
			if (disabled)
				disabled = !testcase.getBuilds().contains(build);
		}
		return disabled;
	}

	public boolean allBuilds(JJTestcase test) {

		boolean allBuilds = true;
		int i = 0;

		while (allBuilds && i < rowNames.size()) {
			allBuilds = test.getBuilds().contains(rowNames.get(i));
			i++;
		}

		return allBuilds;
	}

	public boolean allTestCases(JJBuild build) {

		boolean allTestCases = true;
		int i = 0;

		while (allTestCases && i < colNames.size()) {
			allTestCases = colNames.get(i).getBuilds().contains(build);
			i++;
		}

		return allTestCases;
	}

	public void onCellEdit(Object object, JJBuildBean jJBuildBean, boolean bb) {

		if (object instanceof JJBuild) {

			JJBuild build = (JJBuild) object;

			if (build.getAllTestcases() != null)
				build.setAllTestcases(!build.getAllTestcases());
			else
				build.setAllTestcases(true);
			jJBuildBean.updateJJBuild(build);

			build = jJBuildService.findJJBuild(build.getId());
			if (jJBuildBean.getBuild() != null
					&& build.equals(jJBuildBean.getBuild()))
				jJBuildBean.setBuild(build);

			createTestcaseTree();
			FacesContext.getCurrentInstance().addMessage(null, MessageFactory
					.getMessage("message_successfully_updated", "Build", ""));

		} else if (object instanceof JJTestcase) {

			JJTestcase test = (JJTestcase) object;
			if (test.getAllBuilds() != null)
				test.setAllBuilds(!test.getAllBuilds());
			else
				test.setAllBuilds(true);
			updateJJTestcase(test);

			createTestcaseTree();
			FacesContext.getCurrentInstance().addMessage(null, MessageFactory
					.getMessage("message_successfully_updated", "Test", ""));

		}
	}

	public void onCellEdit(int colIdx, int rowIdx) {

		JJTestcase columnName = jJTestcaseService
				.findJJTestcase(colNames.get(colIdx).getId());
		JJBuild rowName = ((JJBuildBean) LoginBean
				.findBean("jJBuildBean")).jJBuildService
						.findJJBuild(((JJBuild) rowNames.get(rowIdx)).getId());
		boolean successOperation = false;
		if (columnName.getBuilds().contains(rowName)) {
			successOperation = columnName.getBuilds().remove(rowName);

		} else {
			successOperation = columnName.getBuilds().add(rowName);
		}

		if (successOperation) {

			updateJJTestcase(columnName);

			createTestcaseTree();
			FacesContext.getCurrentInstance().addMessage(null, MessageFactory
					.getMessage("message_successfully_updated", "Test", ""));
		}

	}

	public void loadData(JJCategory c, boolean projNull) {

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
				show = jJPermissionService.isAuthorized(
						((LoginBean) LoginBean.findBean("loginBean"))
								.getContact(),
						testcase.getRequirement().getProject(),
						testcase.getRequirement().getProduct(), "Testcase");

			if (show && !jJProjectBean.getProjectList()
					.contains(testcase.getRequirement().getProject()))
				show = false;

			if (show) {

				boolean change = false;
				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);

				if (jJProjectBean.getProject() == null) {
					change = true;

					jJProjectBean
							.setProject(testcase.getRequirement().getProject());
					jJProductBean
							.setProduct(testcase.getRequirement().getProduct());
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(
							testcase.getRequirement().getVersioning());
					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());
				} else if (!jJProjectBean.getProject()
						.equals(testcase.getRequirement().getProject())) {
					change = true;
					jJProjectBean
							.setProject(testcase.getRequirement().getProject());
					jJProductBean
							.setProduct(testcase.getRequirement().getProduct());
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(
							testcase.getRequirement().getVersioning());

					session.setAttribute("jJSprintBean", new JJSprintBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJTaskBean", new JJTaskBean());
				} else if (testcase.getRequirement().getProduct() != null
						&& jJProductBean.getProduct() != null) {
					if (!testcase.getRequirement().getProduct()
							.equals(jJProductBean.getProduct())) {
						change = true;
						jJProductBean.setProduct(
								testcase.getRequirement().getProduct());
						jJVersionBean.getVersionList();
						jJVersionBean.setVersion(
								testcase.getRequirement().getVersioning());
						session.setAttribute("jJTaskBean", new JJTaskBean());
						session.setAttribute("jJStatusBean",
								new JJStatusBean());
						session.setAttribute("jJSprintBean",
								new JJSprintBean());
					} else if (testcase.getRequirement().getVersioning() != null
							&& jJVersionBean.getVersion() != null) {
						if (!testcase.getRequirement().getVersioning()
								.equals(jJVersionBean.getVersion())) {
							change = true;
							jJVersionBean.getVersionList();
							jJVersionBean.setVersion(
									testcase.getRequirement().getVersioning());

							session.setAttribute("jJStatusBean",
									new JJStatusBean());
						}
					}
				} else if (jJProductBean.getProduct() != null
						&& testcase.getRequirement().getProduct() == null) {
					change = true;
					jJProductBean.setProduct(null);
					jJVersionBean.getVersionList();
					jJVersionBean.setVersion(null);
					session.setAttribute("jJTaskBean", new JJTaskBean());
					session.setAttribute("jJStatusBean", new JJStatusBean());
					session.setAttribute("jJSprintBean", new JJSprintBean());
				}

				if (change) {

					((LoginBean) LoginBean.findBean("loginBean"))
							.setAuthorisationService(
									new AuthorisationService(session,
											((LoginBean) LoginBean
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
				FacesMessage facesMessage = MessageFactory
						.getMessage("validator_page_access", "Testcase");
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
		testcase.setAllBuilds(true);
		testcase.setStatus(
				jJStatusService.getOneStatus("NEW", "TestCase", true));
		requirement = null;

		task = new JJTask();

		initiateTask = false;
		disabledInitTask = false;
		disabledTask = true;

		disabledTestcaseMode = false;
		disabledTeststepMode = true;
		testcaseState = true;
		builds = new ArrayList<JJBuild>();
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
			task.setEndDatePlanned(new Date(task.getStartDatePlanned().getTime()
					+ task.getWorkloadPlanned().longValue() * 3600000));

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
		testcase.setAllBuilds(copyTestcase.getAllBuilds());
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
				null, false, null, null, testcase, build, true, false, true,
				null);
		if (tasks.isEmpty()) {
			tasks = jJTaskService.getTasks(null, null, null, null, null, false,
					null, null, testcase, null, true, false, false, null);
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

				jJTaskBean.saveJJTask(task, false, new MutableInt(0));
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
			jJTaskBean.saveJJTask(task, true, new MutableInt(0));
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
			rowNames = null;
			colNames = null;
			value = null;

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

			FacesContext.getCurrentInstance().addMessage(null, MessageFactory
					.getMessage("message_successfully_created", "Test", ""));

		}

	}

	public void saveAndclose(JJTeststepBean jJTeststepBean) {

		RequestContext context = RequestContext.getCurrentInstance();
		if (testcaseState) {

			if (getTestcaseDialogConfiguration()) {
				context.execute("PF('testcaseDialogWidget').hide()");
				RequestContext.getCurrentInstance().update("growlForm");
			} else {
				newTestcase(jJTeststepBean);
			}

		} else {

			if (!requirement.equals(testcase.getRequirement())) {

				testcase.setRequirement(requirement);

			}

			if (testcase.getAllBuilds() == null || !testcase.getAllBuilds())
				testcase.setBuilds(new HashSet<JJBuild>(builds));
			updateJJTestcase(testcase);
			rowNames = null;
			colNames = null;
			value = null;
			testcase = jJTestcaseService.findJJTestcase(testcase.getId());
			requirement.getTestcases().add(testcase);

			FacesContext.getCurrentInstance().addMessage(null, MessageFactory
					.getMessage("message_successfully_updated", "Test", ""));

			context.execute("PF('testcaseDialogWidget').hide()");
			RequestContext.getCurrentInstance().update("growlForm");

		}
		createTestcaseTree();
		// colNames = null;
		// rowNames = null;

	}

	public void closeDialog() {
		task = null;
		builds = null;
		pieChart = null;
		testcases = null;
		categoryModel = null;
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
				requirement.getChapter(), null, null, false, false, false);

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
		pieChart = null;
		categoryModel = null;
		testcases = null;
		rootNode = new DefaultTreeNode("Root", null);
		rowCount = 0;

		JJBuild build = ((JJBuildBean) LoginBean.findBean("jJBuildBean"))
				.getBuild();
		TreeNode projectNode = new DefaultTreeNode("project", project,
				rootNode);

		projectNode.setExpanded(true);

		if (category != null) {

			TreeNode categoryNode = new DefaultTreeNode("category", category,
					projectNode);

			categoryNode.setExpanded(true);

			List<JJChapter> parentChapters = jJChapterService.getParentsChapter(
					LoginBean.getCompany(), project, category, true, true);

			for (JJChapter ch : parentChapters) {
				if (jJChapterService.haveRequirements(ch)) {
					TreeNode node = createTree(ch, categoryNode, category,
							build);
					if (chapter != null && ch.equals(chapter)) {
						selectedNode = node;
						selectedNode.setExpanded(true);
					}

				}

			}

			List<JJRequirement> withOutChapter = jJRequirementService
					.getRequirementsWithOutChapter(LoginBean.getCompany(),
							category,
							((LoginBean) LoginBean.findBean("loginBean"))
									.getAuthorizedMap("Requirement", project,
											LoginBean.getProduct()),
							LoginBean.getVersion(), null, true, true);

			List<JJTestcase> testWithOutChapter = jJTestcaseService
					.getImportTestcases(category, LoginBean.getProject(),
							LoginBean.getProduct(), LoginBean.getVersion(),
							build, true, true);

			if ((withOutChapter != null && !withOutChapter.isEmpty())
					|| (testWithOutChapter != null
							&& !testWithOutChapter.isEmpty())) {

				TreeNode newNode = new DefaultTreeNode("withOutChapter",
						"withOutChapter", categoryNode);

				for (JJRequirement requirement : withOutChapter) {
					if (!jJRequirementService.haveTestcase(requirement)) {
						new DefaultTreeNode("TestcaseRequirement", requirement,
								newNode);
					}
				}

				for (JJTestcase test : testWithOutChapter) {
					String type = getType(test);
					rowCount++;
					TreeNode newNode3 = new DefaultTreeNode(type, test,
							newNode);

					if (testcase != null && testcase.equals(test)) {
						selectedNode = newNode3;
						newNode.setExpanded(true);
					}

				}
			}
		}

	}

	public void onNodeSelect(NodeSelectEvent event) {

		TreeNode selectedNode = event.getTreeNode();

		// String code = getSubString(selectedNode.toString(), 0, "-");

		if (selectedNode.getData() instanceof JJProject) {

			rendredTestCaseRecaps = false;
			rendredTestCaseHistorical = false;
			rendredEmptySelection = false;

		} else if (selectedNode.getData() instanceof JJCategory) {

			rendredTestCaseRecaps = false;
			rendredTestCaseHistorical = false;
			rendredEmptySelection = false;

		} else if (selectedNode.getData() instanceof JJChapter) {

			// LoginBean loginBean=(LoginBean) LoginBean.findBean("loginBean");
			selectedNode.setExpanded(true);
			// long id = Long.parseLong(getSubString(selectedNode.toString(), 1,
			// "-"));
			chapter = (JJChapter) selectedNode.getData();
			List<JJRequirement> rqs = jJRequirementService.getRequirements(
					LoginBean.getCompany(), null,
					((LoginBean) LoginBean.findBean("loginBean"))
							.getAuthorizedMap("testcase",
									LoginBean.getProject(),
									LoginBean.getProduct()),
					LoginBean.getVersion(), null, chapter, true, true, true,
					false, null);

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
			JJBuild build = ((JJBuildBean) LoginBean.findBean("jJBuildBean"))
					.getBuild();
			if (build == null) {
				testcases = new ArrayList<JJTestcase>();
				for (TreeNode node : selectedNode.getChildren()) {
					if (node.getData() instanceof JJTestcase)
						testcases.add((JJTestcase) node.getData());
				}
			} else
				testcases = null;

			rendredTestCaseRecaps = true;
			pieChart = null;
			categoryModel = null;
			rendredTestCaseHistorical = false;
			rendredEmptySelection = false;
		} else if (selectedNode.getType().equalsIgnoreCase("withOutChapter")) {

			// LoginBean loginBean=(LoginBean) LoginBean.findBean("loginBean");
			chapter = null;
			selectedNode.setExpanded(true);
			List<JJRequirement> withOutChapter = jJRequirementService
					.getRequirementsWithOutChapter(LoginBean.getCompany(),
							category,
							((LoginBean) LoginBean.findBean("loginBean"))
									.getAuthorizedMap("Requirement", project,
											LoginBean.getProduct()),
							LoginBean.getVersion(), null, true, true);

			if (withOutChapter.size() > 0) {
				int i = 0;
				for (JJRequirement r : withOutChapter) {
					if (jJRequirementService.haveTestcase(r))
						i = i + 1;
				}
				reqCoverage = (float) (i * 100 / withOutChapter.size());
			} else
				reqCoverage = 0;

			testcase = null;

			rendredTestCaseRecaps = true;
			pieChart = null;
			JJBuild build = ((JJBuildBean) LoginBean.findBean("jJBuildBean"))
					.getBuild();
			if (build == null) {
				testcases = new ArrayList<JJTestcase>();
				for (TreeNode node : selectedNode.getChildren()) {
					if (node.getData() instanceof JJTestcase)
						testcases.add((JJTestcase) node.getData());
				}
			} else
				testcases = null;
			categoryModel = null;
			rendredTestCaseHistorical = false;
			rendredEmptySelection = false;

		} else if (selectedNode.getData() instanceof JJTestcase) {

			// long id = Long.parseLong(getSubString(selectedNode.toString(), 1,
			// "-"));
			rendredTestCaseRecaps = false;

			testcase = (JJTestcase) selectedNode.getData();
			selectedNode.getParent().setExpanded(true);

			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(FacesContext.getCurrentInstance()
								.getExternalContext().getRequestContextPath()
								+ "/pages/test.jsf?testcase=" + testcase.getId()
								+ "&faces-redirect=true");
			} catch (IOException e) {
			}
		} else if (selectedNode.getData() instanceof JJRequirement
				&& ((LoginBean) LoginBean.findBean("loginBean"))
						.getAuthorisationService().iswTest()) {
			JJTeststepBean jJTeststepBean = (JJTeststepBean) LoginBean
					.findBean("jJTeststepBean");
			if (jJTeststepBean == null)
				jJTeststepBean = new JJTeststepBean();
			newTestcase(jJTeststepBean);
			requirement = jJRequirementService.findJJRequirement(
					((JJRequirement) selectedNode.getData()).getId());
			category = requirement.getCategory();
			chapter = requirement.getChapter();
			RequestContext.getCurrentInstance()
					.execute("PF('testcaseDialogWidget').show()");
		}
	}

	// private String getSubString(String s, int index, String c) {
	// String[] temp = s.split(c);
	// return temp[index];
	// }

	// Recursive function to create tree
	private TreeNode createTree(JJChapter chapterParent, TreeNode rootNode,
			JJCategory category, JJBuild build) {

		TreeNode newNode = new DefaultTreeNode(chapterParent, rootNode);

		List<Object> elements = getSortedElements(chapterParent, project,
				product, category, true);
		boolean expanded = false;

		// Set<JJTestcase> testcaseElements = new HashSet<JJTestcase>();;

		for (Object entry : elements) {
			// String className = entry.getValue().getClass().getSimpleName();

			if (entry instanceof JJChapter) {

				JJChapter ch = (JJChapter) entry;
				if (jJChapterService.haveRequirements(ch)) {
					TreeNode node = createTree(ch, newNode, category, build);
					if (chapter != null && ch.equals(chapter)) {
						selectedNode = node;
						selectedNode.setExpanded(true);
						expanded = true;
					}

				}

			} else if (entry instanceof JJRequirement) {

				JJRequirement requirement = (JJRequirement) entry;
				// List<JJTestcase> testcases = jJTestcaseService.getTestcases(
				// requirement, null, LoginBean.getVersion(), build, true,
				// true, false);
				// for (JJTestcase testcase : testcases) {
				// testcaseElements.add(testcase);
				// }

				if (!jJRequirementService.haveTestcase(requirement)) {
					new DefaultTreeNode("TestcaseRequirement", requirement,
							newNode);
				}
			}
		}

		for (JJTestcase testcaseEntry : jJTestcaseService.getTestcases(null,
				chapterParent, LoginBean.getVersion(), build, true, true,
				false)) {
			JJTestcase test = testcaseEntry;
			String type = getType(test);
			rowCount++;
			TreeNode newNode3 = new DefaultTreeNode(type, test, newNode);
			if (testcase != null && testcase.equals(test)) {
				selectedNode = newNode3;
				expanded = true;
			}

		}

		newNode.setExpanded(expanded);

		return newNode;
	}

	private String getType(JJTestcase tc) {

		// JJTestcase tc = jJTestcaseService.findJJTestcase(testcase.getId());

		if (tc != null) {
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
						.getTestcaseexecutions(tc, build, true, true, false);

				if (build != null) {
					if (testcaseexecutions.isEmpty()) {

						testcaseexecutions = jJTestcaseexecutionService
								.getTestcaseexecutions(tc, null, true, true,
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
		} else
			return "";

	}

	public List<Object> getSortedElements(JJChapter parent, JJProject project,
			JJProduct product, JJCategory category, boolean onlyActif) {

		Set<Object> elements = new HashSet<Object>();

		if (parent != null) {

			List<JJChapter> chapters = jJChapterService
					.getChildrenOfParentChapter(parent, onlyActif, true);

			elements.addAll(chapters);
			List<JJRequirement> requirements = jJRequirementService
					.getRequirementChildrenWithChapterSortedByOrder(
							LoginBean.getCompany(), parent,
							LoginBean.getProduct(), LoginBean.getVersion(),
							onlyActif);

			elements.addAll(requirements);
			// for (JJRequirement requirement : requirements) {
			// if (requirement.getOrdering() != null)
			// elements.put(requirement.getOrdering(), requirement);
			// else
			// elements.put(0, requirement);
			//
			// }
		} else {

			List<JJChapter> chapters = jJChapterService.getParentsChapter(
					LoginBean.getCompany(), project, category, onlyActif, true);

			elements.addAll(chapters);

		}

		return new ArrayList<Object>(elements);

	}

	public StreamedContent getPreProcessPDF()
			throws IOException, BadElementException, DocumentException {

		Document pdf = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// PdfWriter writer = PdfWriter.getInstance(pdf, baos);
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
				LoginBean.getCompany(), project, category, true, true);

		JJBuild build = ((JJBuildBean) LoginBean.findBean("jJBuildBean"))
				.getBuild();
		for (JJChapter chapter : parentChapters) {
			createTreeDocument(chapter, build, category, paragraph,
					fontTeststep, fontChapter, fontTestcase, style);
		}

		List<JJTestcase> withOutChapter = jJTestcaseService.getImportTestcases(
				category, project, LoginBean.getProduct(),
				LoginBean.getVersion(), build, true, true);

		if (withOutChapter != null && !withOutChapter.isEmpty()) {
			paragraph.add(new Chunk("\n " + MessageFactory
					.getMessage("test_tree_withOutChapter", "").getDetail()
					+ "\n", fontChapter));

			for (JJTestcase test : withOutChapter) {
				paragraph.add(new Chunk(test.getName() + "\n", fontTestcase));

				List<JJTeststep> teststeps = jJTeststepService
						.getTeststeps(test, true, true);

				for (JJTeststep teststep : teststeps) {

					StringReader strReader = new StringReader(
							teststep.getActionstep());

					List<Element> arrList = HTMLWorkerImpl
							.parseToList(strReader, style);

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

		return new DefaultStreamedContent(
				new ByteArrayInputStream(baos.toByteArray()), "pdf",
				category.getName().toUpperCase().trim() + "-test.pdf");

	}

	public void onCopyEvent() {
		System.err.println(copyTestcase.getName());

		FacesContext.getCurrentInstance().addMessage(null, MessageFactory
				.getMessage("message_successfully_copied", "Test", ""));
	}

	private void createTreeDocument(JJChapter chapterParent, JJBuild build,
			JJCategory category, Paragraph paragraph, Font fontTeststep,
			Font fontChapter, Font fontTestcase, StyleSheet style)
					throws IOException {

		paragraph.add(
				new Chunk("\n" + chapterParent.getName() + "\n", fontChapter));

		List<Object> elements = getSortedElements(chapterParent, project,
				product, category, true);

		// Set<JJTestcase> testcaseElements = new HashSet<JJTestcase>();

		for (Object entry : elements) {
			// String className = entry.getValue().getClass().getSimpleName();

			if (entry instanceof JJChapter) {

				JJChapter chapter = (JJChapter) entry;
				createTreeDocument(chapter, build, category, paragraph,
						fontTeststep, fontChapter, fontTestcase, style);

				// } else if (entry instanceof JJRequirement) {
				//
				// JJRequirement requirement = (JJRequirement) entry;
				// List<JJTestcase> testcases = jJTestcaseService.getTestcases(
				// requirement, null, LoginBean.getVersion(), null, true,
				// true, false);
				// for (JJTestcase testcase : testcases) {
				// testcaseElements.add(testcase);
				// }

			}
		}

		for (JJTestcase testcaseEntry : jJTestcaseService.getTestcases(null,
				chapterParent, LoginBean.getVersion(), null, true, true,
				false)) {

			JJTestcase testcase = testcaseEntry;

			paragraph.add(new Chunk(testcase.getName() + "\n", fontTestcase));

			List<JJTeststep> teststeps = jJTeststepService
					.getTeststeps(testcase, true, true);

			for (JJTeststep teststep : teststeps) {

				StringReader strReader = new StringReader(
						teststep.getActionstep().replace(
								"/pages/ckeditor/getimage?imageId=",
								"/images/"));

				List<Element> arrList = HTMLWorker.parseToList(strReader,
						style);

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

		if (testcases == null) {
			JJBuild build = ((JJBuildBean) LoginBean.findBean("jJBuildBean"))
					.getBuild();
			if (chapter != null)
				testcases = jJTestcaseService.getTestcases(null, chapter,
						LoginBean.getVersion(), build, true, false, true);
			else
				testcases = jJTestcaseService.getImportTestcases(category,
						LoginBean.getProject(), LoginBean.getProduct(),
						LoginBean.getVersion(), build, true, true);
		}

		return testcases;

	}

	public class TestCaseRecap {

		private JJTestcase testcase;
		private String status;
		private boolean disabled;

		public TestCaseRecap(JJTestcase testcase) {
			super();
			this.testcase = testcase;

			List<JJTeststep> teststeps = jJTeststepService
					.getTeststeps(testcase, true, true);

			if (!teststeps.isEmpty()) {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJBuildBean jJBuildBean = (JJBuildBean) session
						.getAttribute("jJBuildBean");

				JJBuild build = jJBuildBean.getBuild();

				if (build != null) {
					disabled = false;
					disabled = build.getAllTestcases() == null
							|| !build.getAllTestcases();
					if (disabled)
						disabled = testcase.getAllBuilds() == null
								|| !testcase.getAllBuilds();
					if (disabled)
						disabled = !testcase.getBuilds().contains(build);
					if (!disabled) {
						List<JJTestcaseexecution> testcaseexecutions = jJTestcaseexecutionService
								.getTestcaseexecutions(testcase, build, true,
										false, true);

						if (!testcaseexecutions.isEmpty()) {
							JJTestcaseexecution testcaseexecution = testcaseexecutions
									.get(0);
							if (testcaseexecution.getPassed() != null) {

								if (testcaseexecution.getPassed()) {
									status = MessageFactory.getMessage(
											"test_status_SUCCESS_label", "")
											.getDetail();
								} else {
									status = MessageFactory.getMessage(
											"test_status_FAILED_label", "")
											.getDetail();
								}
							} else {
								status = MessageFactory.getMessage(
										"test_status_nonFinished_label", "")
										.getDetail();
							}
						} else {
							status = MessageFactory
									.getMessage("test_status_nonRUNED_label",
											"")
									.getDetail();
						}
					} else {
						status = MessageFactory
								.getMessage("test_status_BuildNonAllowed_label",
										"")
								.getDetail();// "not
												// allowed
												// on
												// this
												// build";
					}

				} else {
					disabled = true;
					status = MessageFactory
							.getMessage("test_status_BuildNonSelected_label",
									"")
							.getDetail();// "Select
											// a
											// Build";
				}
			} else {
				disabled = true;
				status = MessageFactory
						.getMessage("test_status_NoTestStep_label", "")
						.getDetail();// "This
										// testcase
										// doesn't
										// have
										// teststeps";
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
		pieChart = null;
		categoryModel = null;
		testcases = null;
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJTestcaseService.saveJJTestcase(b);
		JJRequirementBean.updateRowState(b.getRequirement(),
				jJRequirementService, b);
	}

	public void updateJJTestcase(JJTestcase b) {
		pieChart = null;
		categoryModel = null;
		testcases = null;
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJTestcaseService.updateJJTestcase(b);
		JJRequirementBean.updateRowState(b.getRequirement(),
				jJRequirementService, b);
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
			e.printStackTrace();
		}
	}

	// import as XML

	public void handleImportXML(FileUploadEvent event) throws IOException {

		JJProduct prod = LoginBean.getProduct();

		JJProject proj = LoginBean.getProject();
		try {
			List<Object> objects = ReadXMLFile.getTestcasesFromXml(this,
					event.getFile().getInputstream(), jJTestcaseService,
					jJCategoryService, jJRequirementService, proj, prod,
					proj.getCompany());
			@SuppressWarnings("unchecked")
			List<JJTestcase> testcases = (List<JJTestcase>) objects.get(0);
			@SuppressWarnings("unchecked")
			List<JJTeststep> teststeps = (List<JJTeststep>) objects.get(1);

			if (testcases.isEmpty()) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"No TestCase Found in This File",
						FacesMessage.SEVERITY_WARN, "TestCase");
				FacesContext.getCurrentInstance().addMessage(null,
						facesMessage);
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
				FacesContext.getCurrentInstance().addMessage(null,
						facesMessage);
			}
		} catch (SAXParseException e) {
			FacesMessage facesMessage = MessageFactory.getMessage(
					"Error While Parsing File", FacesMessage.SEVERITY_WARN,
					"TestCase");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

	}

	public String getMarginLeft(JJCategory cc) {
		int jj = categoryList.indexOf(cc);
		if (jj == -1 || jj == 0)
			return "";
		else {
			if (categoryList.get(jj - 1).getStage().equals(cc.getStage()))
				return "";
			else
				return "margin-left: 20px;";
		}
	}

	public void testCaseInfo(long id) throws IOException {
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(FacesContext.getCurrentInstance().getExternalContext()
						.getRequestContextPath() + "/pages/test.jsf?testcase="
						+ id + "&faces-redirect=true");
	}

	@SuppressWarnings("rawtypes")
	public StreamedContent getFile() {

		if (category != null) {
			// JJBuild build = ((JJBuildBean) LoginBean.findBean("jJBuildBean"))
			// .getBuild();
			String buffer = "<category name=\""
					+ category.getName().toUpperCase() + "\">";
			List<JJTestcase> tests = jJTestcaseService.getImportTestcases(
					category, project, LoginBean.getProduct(),
					LoginBean.getVersion(), null, true, false);
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
						+ System.getProperty("line.separator") + "enabled=\"1\""
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
			return new DefaultStreamedContent(stream, "xml",
					category.getName() + "-Tests.xml");
		} else
			return null;
	}

	public void onrate() {

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
			contact.getTestcases()
					.add(jJTestcaseService.findJJTestcase(testcase.getId()));

			if (LoginBean.findBean("jJContactBean") == null) {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJContactBean", new JJContactBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory
					.getMessage(JJTestcaseBean.TEST_SUBSCRIPTION_RATE);
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
			contact.getTestcases()
					.remove(jJTestcaseService.findJJTestcase(testcase.getId()));

			if (LoginBean.findBean("jJContactBean") == null) {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJContactBean", new JJContactBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory
					.getMessage(JJTestcaseBean.TEST_SUBSCRIPTION_CANCEL_RATE);
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);

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
			contact.getTestcases()
					.remove(jJTestcaseService.findJJTestcase(testcase.getId()));

			if (LoginBean.findBean("jJContactBean") == null) {
				FacesContext fContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fContext
						.getExternalContext().getSession(false);
				session.setAttribute("jJContactBean", new JJContactBean());
			}
			((JJContactBean) LoginBean.findBean("jJContactBean"))
					.updateJJContact(contact);

			FacesMessage facesMessage = MessageFactory
					.getMessage(JJTestcaseBean.TEST_SUBSCRIPTION_CANCEL_RATE);
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);

			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	// Chart
	private LineChartModel categoryModel;
	private PieChartModel pieChart;
	private String seriesColors = "000000, 097D0B, D8115A";
	private String pieChartSerieColor = "0000FF,00FF00,FF0000";

	public LineChartModel getCategoryModel() {

		if (categoryModel == null && rendredTestCaseRecaps) {
			List<TestCaseChartUtil> testcases = TestCaseChartUtil
					.getTestCaseUtilFromJJTesCase(getTestcases());

			if (testcases != null && !testcases.isEmpty()) {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				DateFormat f = new SimpleDateFormat("yyyy-MM-dd");

				Set<String> datesTMP = new HashSet<String>();

				for (TestCaseChartUtil testcase : testcases) {

					datesTMP.add(
							f.format(testcase.getTestcase().getCreationDate()));

				}

				JJTestcaseexecutionBean jJTestcaseexecutionBean = (JJTestcaseexecutionBean) session
						.getAttribute("jJTestcaseexecutionBean");

				if (jJTestcaseexecutionBean == null) {
					jJTestcaseexecutionBean = new JJTestcaseexecutionBean();
				}

				Set<JJTestcaseexecution> testcaseexecutions = jJTestcaseexecutionBean
						.getTestcaseexecutions();

				for (JJTestcaseexecution tce : testcaseexecutions) {

					if (tce.getEnabled() && tce.getUpdatedDate() != null) {
						datesTMP.add(f.format(tce.getUpdatedDate()));
					}

				}
				List<String> dates = new ArrayList<String>();
				dates.addAll(datesTMP);

				Collections.sort(dates, new Comparator<String>() {
					DateFormat ff = new SimpleDateFormat("yyyy-MM-dd");

					@Override
					public int compare(String o1, String o2) {
						try {
							return ff.parse(o1).compareTo(ff.parse(o2));
						} catch (ParseException e) {
							throw new IllegalArgumentException(e);
						}
					}
				});

				Map<String, String> mapTotalTC = new LinkedHashMap<String, String>();
				Map<String, String> mapSuccessTC = new LinkedHashMap<String, String>();
				Map<String, String> mapFailedTC = new LinkedHashMap<String, String>();

				for (int i = 0; i < dates.size(); i++) {
					String date = dates.get(i);
					int compteur = 0;

					int compteurSuccess = 0;
					int compteurFailed = 0;

					for (TestCaseChartUtil testcase : testcases) {

						if (date.equalsIgnoreCase(f.format(
								testcase.getTestcase().getCreationDate()))) {
							compteur++;
						}

					}

					if (i == 0) {
						mapTotalTC.put(date, String.valueOf(compteur));

						for (TestCaseChartUtil testcase : testcases) {
							List<JJTestcaseexecution> exec = new ArrayList<JJTestcaseexecution>();
							for (JJTestcaseexecution tce : testcaseexecutions) {
								Date updatedDate = tce.getUpdatedDate();
								if ((updatedDate != null)
										&& (date.equalsIgnoreCase(
												f.format(updatedDate)))
										&& (tce.getTestcase().equals(
												testcase.getTestcase()))) {

									if (tce.getPassed() != null) {
										exec.add(tce);
									}

								}
							}
							if (!exec.isEmpty()) {
								if (exec.size() == 1) {
									if (exec.get(0).getPassed()) {
										compteurSuccess++;
										testcase.setSuccess("compteurSuccess");
									} else {
										compteurFailed++;
										testcase.setSuccess("compteurFailed");
									}
								} else {
									Collections.sort(exec,
											new Comparator<JJTestcaseexecution>() {

												@Override
												public int compare(
														JJTestcaseexecution o1,
														JJTestcaseexecution o2) {
													return o2.getUpdatedDate()
															.compareTo(o1
																	.getUpdatedDate());
												}
											});
									if (exec.get(0).getPassed()) {
										compteurSuccess++;
										testcase.setSuccess("compteurSuccess");
									} else {
										compteurFailed++;
										testcase.setSuccess("compteurFailed");
									}
								}
							}
						}

						mapSuccessTC.put(date, String.valueOf(compteurSuccess));
						mapFailedTC.put(date, String.valueOf(compteurFailed));

					} else {
						compteurFailed = Integer
								.parseInt(mapFailedTC.get(dates.get(i - 1)));
						compteurSuccess = Integer
								.parseInt(mapSuccessTC.get(dates.get(i - 1)));
						mapTotalTC.put(date, String.valueOf(compteur + Integer
								.parseInt(mapTotalTC.get(dates.get(i - 1)))));

						for (TestCaseChartUtil testcase : testcases) {
							List<JJTestcaseexecution> exec = new ArrayList<JJTestcaseexecution>();
							for (JJTestcaseexecution tce : testcaseexecutions) {
								Date updatedDate = tce.getUpdatedDate();
								if ((updatedDate != null)
										&& (date.equalsIgnoreCase(
												f.format(updatedDate)))
										&& (tce.getTestcase().equals(
												testcase.getTestcase()))) {

									if (tce.getPassed() != null) {
										exec.add(tce);
									}

								}
							}
							if (!exec.isEmpty()) {
								if (exec.size() == 1) {
									if (exec.get(0).getPassed()) {
										if (testcase.getSuccess() != null) {
											if (testcase.getSuccess()
													.equalsIgnoreCase(
															"compteurFailed")) {
												compteurFailed--;
												compteurSuccess++;
												testcase.setSuccess(
														"compteurSuccess");
											}
										} else {
											compteurSuccess++;
											testcase.setSuccess(
													"compteurSuccess");
										}

									} else {
										if (testcase.getSuccess() != null) {
											if (testcase.getSuccess()
													.equalsIgnoreCase(
															"compteurSuccess")) {
												compteurFailed++;
												compteurSuccess--;
												testcase.setSuccess(
														"compteurFailed");
											}
										} else {
											compteurFailed++;
											testcase.setSuccess(
													"compteurFailed");
										}
									}
								} else {
									Collections.sort(exec,
											new Comparator<JJTestcaseexecution>() {

												@Override
												public int compare(
														JJTestcaseexecution o1,
														JJTestcaseexecution o2) {
													return o2.getUpdatedDate()
															.compareTo(o1
																	.getUpdatedDate());
												}
											});
									if (exec.get(0).getPassed()) {
										if (testcase.getSuccess() != null) {
											if (testcase.getSuccess()
													.equalsIgnoreCase(
															"compteurFailed")) {
												compteurFailed--;
												compteurSuccess++;
												testcase.setSuccess(
														"compteurSuccess");
											}
										} else {
											compteurSuccess++;
											testcase.setSuccess(
													"compteurSuccess");
										}

									} else {
										if (testcase.getSuccess() != null) {
											if (testcase.getSuccess()
													.equalsIgnoreCase(
															"compteurSuccess")) {
												compteurFailed++;
												compteurSuccess--;
												testcase.setSuccess(
														"compteurFailed");
											}
										} else {
											compteurFailed++;
											testcase.setSuccess(
													"compteurFailed");
										}
									}
								}
							}
						}
						if (compteurFailed < 0)
							compteurFailed = 0;
						if (compteurSuccess < 0)
							compteurSuccess = 0;

						mapSuccessTC.put(date, String.valueOf(compteurSuccess));
						mapFailedTC.put(date, String.valueOf(compteurFailed));

					}
				}

				ChartSeries totalTC = new ChartSeries("Total TC");
				if (mapTotalTC.isEmpty()) {
					totalTC.set("0", 0);
				} else

					for (Map.Entry<String, String> entry : mapTotalTC
							.entrySet()) {
						totalTC.set(entry.getKey(),
								Integer.parseInt(entry.getValue()));
					}

				ChartSeries successTC = new ChartSeries("Success TC");
				if (mapSuccessTC.isEmpty()) {
					successTC.set("0", 0);
				} else

					for (Map.Entry<String, String> entry : mapSuccessTC
							.entrySet()) {
						successTC.set(entry.getKey(),
								Integer.parseInt(entry.getValue()));
					}

				ChartSeries failedTC = new ChartSeries("Failed TC");
				if (mapFailedTC.isEmpty()) {
					failedTC.set("0", 0);
				} else

					for (Map.Entry<String, String> entry : mapFailedTC
							.entrySet()) {
						failedTC.set(entry.getKey(),
								Integer.parseInt(entry.getValue()));
					}

				categoryModel = new LineChartModel();
				categoryModel.addSeries(totalTC);
				categoryModel.addSeries(successTC);
				categoryModel.addSeries(failedTC);
				categoryModel.setAnimate(true);
				categoryModel.setLegendPosition("ne");
				categoryModel.setTitle(MessageFactory
						.getMessage("test_graph_testcase_title", "")
						.getDetail());
				categoryModel.setSeriesColors(seriesColors);

				DateAxis axis = new DateAxis();
				axis.setTickAngle(-50);
				axis.setLabel(MessageFactory
						.getMessage("test_graph_ordinate_label", "")
						.getDetail());
				axis.setTickFormat("%b %#d, %y");
				categoryModel.getAxes().put(AxisType.X, axis);

				// cate goryModel.getAxis(AxisType.X).setTickAngle(-50);
				// categoryModel.getAxis(AxisType.X).setTickFormat("%b %#d,
				// %y");
				// categoryModel.getAxis(AxisType.X).setLabel(MessageFactory
				// .getMessage("test_graph_ordinate_label", "").getDetail());

				categoryModel.getAxis(AxisType.Y)
						.setLabel(MessageFactory
								.getMessage("test_graph_abscissa_label", "")
								.getDetail());
			}

		}
		if (categoryModel == null) {

			LineChartModel categoryModel1 = new LineChartModel();
			ChartSeries totalTC = new ChartSeries("Total TC");
			totalTC.set(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
					0);
			categoryModel1.addSeries(totalTC);
			return categoryModel1;
		} else
			return categoryModel;
	}

	public PieChartModel getPieChart() {

		if (pieChart == null && rendredTestCaseRecaps) {
			List<TestCaseChartUtil> testcases = TestCaseChartUtil
					.getTestCaseUtilFromJJTesCase(getTestcases());

			if (testcases != null && !testcases.isEmpty()) {

				JJBuild build = ((JJBuildBean) LoginBean
						.findBean("jJBuildBean")).getBuild();
				JJVersion version = LoginBean.getVersion();

				pieChart = new PieChartModel();
				int noExec = 0;
				int passed = 0;
				int notPasses = 0;

				for (TestCaseChartUtil test : testcases) {

					Boolean isPassed = jJTestcaseexecutionService
							.isPassed(test.getTestcase(), build, version);

					if (isPassed == null)
						noExec++;
					else if (isPassed)
						passed++;
					else
						notPasses++;
				}

				pieChart.set("Sans Exec", noExec);
				pieChart.set("Passed", passed);
				pieChart.set("Non Passed", notPasses);
				pieChart.setSeriesColors(pieChartSerieColor);
				pieChart.setFill(false);
				pieChart.setLegendPosition("e");
				pieChart.setSliceMargin(5);
				pieChart.setShowDataLabels(true);

			}
		}
		return pieChart;
	}

	public String getSeriesColors() {
		return seriesColors;
	}

	public String getPieChartSerieColor() {
		return pieChartSerieColor;
	}

	public String getActiveIndex() {

		return ((LoginBean) LoginBean.findBean("loginBean")).isMobile()
				? "-1"
				: "0";

	}

	public void setActiveIndex(String index) {

	}

	public static void resetTestcaseBean() {
		JJTestcaseBean testcaseBean = (JJTestcaseBean) LoginBean
				.findBean("jJTestcaseBean");
		if (testcaseBean != null) {
			testcaseBean.setProject(null);
		}

	}

}
