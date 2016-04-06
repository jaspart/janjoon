package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJSprintService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyStatusDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.SprintUtil;

@RooSerializable
@RooJsfManagedBean(entity = JJStatus.class, beanName = "jJStatusBean")
public class JJStatusBean {

	private static int KPI_TAB = 0;
	private static int SPEC_TAB = 1;
	private static int BUG_TAB = 2;
	private static int SPRINT_TAB = 3;

	@Autowired
	private JJCategoryService jJCategoryService;

	@Autowired
	private JJTestcaseService jJTestcaseService;

	@Autowired
	private JJRequirementService jJRequirementService;

	@Autowired
	private JJPermissionService jJPermissionService;

	@Autowired
	private JJBugService jJBugService;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJSprintService jJSprintService;

	private List<String> tableNames;
	private List<JJStatus> statusList;

	private List<JJContact> contacts = new ArrayList<JJContact>();
	private List<JJStatus> states = new ArrayList<JJStatus>();
	private ArrayList<ArrayList<Integer>> value = new ArrayList<ArrayList<Integer>>();

	private List<JJContact> bugContacts = new ArrayList<JJContact>();
	private List<JJStatus> bugStatus = new ArrayList<JJStatus>();
	private ArrayList<ArrayList<Integer>> bugValue = new ArrayList<ArrayList<Integer>>();

	private List<JJContact> taskContacts = new ArrayList<JJContact>();
	private List<JJStatus> taskStatues = new ArrayList<JJStatus>();
	private ArrayList<ArrayList<Integer>> taskValues = new ArrayList<ArrayList<Integer>>();

	private List<JJContact> testContacts = new ArrayList<JJContact>();
	private List<Boolean> testStates = new ArrayList<Boolean>();
	private ArrayList<ArrayList<Integer>> testValues = new ArrayList<ArrayList<Integer>>();

	private LazyStatusDataModel lazyStatusList;
	private JJStatus selectedStatus;
	private PieChartModel statusPieChart;
	private PieChartModel categoryPieChart;
	private PieChartModel bugPieChart;
	private PieChartModel projectPieChart;
	private PieChartModel productPieChart;
	private SelectItem[] objectOptions;
	private MeterGaugeChartModel bugMetergauge;
	private LineChartModel kpiLineModel;
	private BarChartModel kpiBarModel;
	private MeterGaugeChartModel prjMetergauge;
	private JJProject project;
	private JJBuild build;
	private List<CategoryDataModel> categoryDataModel;
	private int activeTabIndex;
	private int activeTabSprintIndex = -1;
	private boolean renderCreate;

	public LazyStatusDataModel getLazyStatusList() {
		if (lazyStatusList == null) {
			lazyStatusList = new LazyStatusDataModel(jJStatusService);
		}
		return lazyStatusList;
	}

	public void setLazyStatusList(LazyStatusDataModel lazyStatusList) {
		this.lazyStatusList = lazyStatusList;
		this.objectOptions = null;
	}

	public List<JJContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<JJContact> contacts) {
		this.contacts = contacts;
	}

	public List<JJStatus> getStates() {

		if (activeTabIndex == KPI_TAB) {
			if (states == null || states.isEmpty()) {

				contacts = jJRequirementService.getReqContacts(
						LoginBean.getCompany(),
						((LoginBean) LoginBean.findBean("loginBean"))
								.getAuthorizedMap("Requirement",
										LoginBean.getProject(),
										LoginBean.getProduct()),
						LoginBean.getVersion());

				if (contacts != null && !contacts.isEmpty()) {

					states = jJStatusService.getStatus("RequirementState", true,
							null, true);
					if (!states.isEmpty()) {
						value = new ArrayList<ArrayList<Integer>>();
						states.add(null);
						for (int i = 0; i < contacts.size(); i++) {
							value.add(new ArrayList<Integer>());

							for (int j = 0; j < states.size(); j++) {

								int k = Integer.parseInt(
										"" + jJRequirementService.getReqCount(
												LoginBean.getCompany(),
												LoginBean.getProject(),
												LoginBean.getProduct(),
												LoginBean.getVersion(), null,
												null, states.get(j),
												contacts.get(i), true));
								value.get(i).add(k);
							}
						}
					} else {
						states = null;
						contacts = null;
					}

				}

			}

			return states;
		} else
			return null;
	}

	public void setStates(List<JJStatus> states) {
		this.states = states;
	}

	public ArrayList<ArrayList<Integer>> getValue() {
		return value;
	}

	public void setValue(ArrayList<ArrayList<Integer>> value) {
		this.value = value;
	}

	public List<JJContact> getBugContacts() {
		return bugContacts;
	}

	public void setBugContacts(List<JJContact> bugContacts) {
		this.bugContacts = bugContacts;
	}

	public List<JJStatus> getBugStatus() {

		if (activeTabIndex == BUG_TAB) {
			if (bugStatus == null || bugStatus.isEmpty()) {

				bugContacts = jJBugService.getBugsContacts(
						LoginBean.getCompany(), LoginBean.getProject(),
						LoginBean.getProduct(), LoginBean.getVersion());

				if (bugContacts != null && !bugContacts.isEmpty()) {

					bugStatus = jJBugService.getBugsStatus(
							LoginBean.getCompany(), LoginBean.getProject(),
							LoginBean.getProduct(), LoginBean.getVersion());
					if (!bugStatus.isEmpty()) {
						bugValue = new ArrayList<ArrayList<Integer>>();
						bugStatus.add(null);
						for (int i = 0; i < bugContacts.size(); i++) {
							bugValue.add(new ArrayList<Integer>());

							for (int j = 0; j < bugStatus.size(); j++) {

								int k = Integer.parseInt(
										"" + jJBugService.getBugsCountByStaus(
												LoginBean.getCompany(),
												bugContacts.get(i),
												LoginBean.getProject(),
												LoginBean.getProduct(),
												LoginBean.getVersion(),
												bugStatus.get(j), true));
								bugValue.get(i).add(k);
							}
						}
					} else {
						bugStatus = null;
						bugContacts = null;
					}

				}

			}

			return bugStatus;
		} else
			return null;
	}

	public void setBugStatus(List<JJStatus> bugStatus) {
		this.bugStatus = bugStatus;
	}

	public ArrayList<ArrayList<Integer>> getBugValue() {
		return bugValue;
	}

	public void setBugValue(ArrayList<ArrayList<Integer>> bugValue) {
		this.bugValue = bugValue;
	}

	public List<JJContact> getTestContacts() {
		return testContacts;
	}

	public void setTestContacts(List<JJContact> testContacts) {
		this.testContacts = testContacts;
	}

	public List<Boolean> getTestStates() {

		if (activeTabIndex == KPI_TAB) {
			if (testStates == null || testStates.isEmpty()) {

				testContacts = jJTestcaseService.getTestCaseContacts(project,
						LoginBean.getProduct(), LoginBean.getVersion(), true);

				if (testContacts != null && !testContacts.isEmpty()) {

					testStates = new ArrayList<Boolean>();

					testStates.add(true);
					testStates.add(false);
					testStates.add(null);

					testValues = new ArrayList<ArrayList<Integer>>();
					// states.add(null);
					for (int i = 0; i < testContacts.size(); i++) {
						testValues.add(new ArrayList<Integer>());

						for (int j = 0; j < testStates.size(); j++) {
							Long number = jJTestcaseService
									.getTestCaseCountByLastResult(
											LoginBean.getProject(),
											LoginBean.getProduct(),
											LoginBean.getVersion(), build, true,
											testStates.get(j),
											testContacts.get(i));
							testValues.get(i)
									.add(Integer.parseInt("" + number));
						}
					}

				}

			}
		}
		return testStates;
	}

	public void setTestStates(List<Boolean> testStates) {
		this.testStates = testStates;
	}

	public ArrayList<ArrayList<Integer>> getTestValues() {
		return testValues;
	}

	public void setTestValues(ArrayList<ArrayList<Integer>> testValues) {
		this.testValues = testValues;
	}

	public List<JJContact> getTaskContacts() {
		return taskContacts;
	}

	public void setTaskContacts(List<JJContact> taskContacts) {
		this.taskContacts = taskContacts;
	}

	public List<JJStatus> getTaskStatues() {

		if (activeTabIndex == KPI_TAB) {
			if (taskStatues == null || taskStatues.isEmpty()) {

				taskContacts = jJPermissionService.areAuthorized(
						LoginBean.getCompany(), null, LoginBean.getProject(),
						LoginBean.getProduct(), "project");

				if (taskContacts != null && !taskContacts.isEmpty()) {

					taskStatues = jJStatusService.getStatus("Task", true, null,
							true);
					if (!taskStatues.isEmpty()) {
						taskValues = new ArrayList<ArrayList<Integer>>();
						taskStatues.add(null);
						for (int i = 0; i < taskContacts.size(); i++) {
							taskValues.add(new ArrayList<Integer>());

							for (int j = 0; j < taskStatues.size(); j++) {
								List<JJTask> tasks = jJTaskService
										.getExecutedTaks(null,
												LoginBean.getProject(),
												LoginBean.getProduct(),
												taskContacts.get(i),
												taskStatues.get(j), null);
								int number = 0;
								if (tasks != null && !tasks.isEmpty())
									number = tasks.size();
								taskValues.get(i).add(number);
							}
						}
					} else {
						taskStatues = null;
						taskContacts = null;
					}

				}

			}

			return taskStatues;
		} else
			return null;
	}

	public void setTaskStatues(List<JJStatus> taskStatues) {
		this.taskStatues = taskStatues;
	}

	public ArrayList<ArrayList<Integer>> getTaskValues() {
		return taskValues;
	}

	public void setTaskValues(ArrayList<ArrayList<Integer>> taskValues) {
		this.taskValues = taskValues;
	}

	public String onEdit() {
		return null;
	}

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setjJPermissionService(
			JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJSprintService(JJSprintService jJSprintService) {
		this.jJSprintService = jJSprintService;
	}

	public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
		this.jJTestcaseService = jJTestcaseService;
	}

	public List<JJStatus> getStatusList() {

		if (statusList == null)
			statusList = jJStatusService.getStatus(null, true, null, true);
		return statusList;
	}

	public void setStatusList(List<JJStatus> statusList) {
		this.statusList = statusList;
	}

	public JJStatus getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(JJStatus selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public JJProject getProject() {

		if (this.project == null)
			this.project = LoginBean.getProject();
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
		if (this.project == null) {
			categoryDataModel = null;
			activeTabIndex = 0;
			activeTabSprintIndex = -1;
			prjMetergauge = null;
			bugPieChart = null;
			bugContacts = null;
			bugContacts = null;
			bugValue = null;
			bugMetergauge = null;
			kpiLineModel = null;
			kpiBarModel = null;

		}

	}

	public JJBuild getBuild() {
		return build;
	}

	public void setBuild(JJBuild build) {
		this.build = build;
	}

	public List<CategoryDataModel> getCategoryDataModel() {
		if (activeTabIndex == SPEC_TAB) {
			if (categoryDataModel == null) {
				if (getProject() == null) {
					categoryDataModel = new ArrayList<CategoryDataModel>();

					List<JJCategory> categoryList = jJCategoryService
							.getCategories(null, false, true, true,
									((LoginBean) LoginBean
											.findBean("loginBean")).getContact()
													.getCompany());

					for (JJCategory category : categoryList) {
						categoryDataModel.add(new CategoryDataModel(category));
					}
				} else {
					categoryDataModel = new ArrayList<CategoryDataModel>();

					List<JJCategory> categoryList = jJCategoryService
							.getCategories(null, false, true, true,
									((LoginBean) LoginBean
											.findBean("loginBean")).getContact()
													.getCompany());

					for (JJCategory category : categoryList) {
						categoryDataModel.add(new CategoryDataModel(category));
					}
				}

			}
			return categoryDataModel;
		} else
			return null;
	}

	public void setCategoryDataModel(
			List<CategoryDataModel> categoryDataModel) {
		this.categoryDataModel = categoryDataModel;
	}

	public SelectItem[] getObjectOptions() {

		if (objectOptions == null) {

			Set<String> objects = jJStatusService.getAllObject();
			objectOptions = new SelectItem[objects.size() + 1];

			objectOptions[0] = new SelectItem("",
					MessageFactory.getMessage("label_all").getDetail());
			int i = 0;
			for (String comp : objects) {
				objectOptions[i + 1] = new SelectItem(comp, comp);
				i++;

			}

		}
		return objectOptions;

	}

	public void setObjectOptions(SelectItem[] objectOptions) {
		this.objectOptions = objectOptions;
	}

	public PieChartModel getStatusPieChart() {

		if (activeTabIndex == SPEC_TAB) {
			if (statusPieChart == null) {
				statusPieChart = new PieChartModel();
				List<JJStatus> statReq = jJStatusService
						.getStatus("Requirement", true, null, false);
				boolean render = false;
				for (JJStatus s : statReq) {

					int i = Integer
							.parseInt("" + jJRequirementService.getReqCount(
									((LoginBean) LoginBean
											.findBean("loginBean")).getContact()
													.getCompany(),
									project, LoginBean.getProduct(),
									LoginBean.getVersion(), s, null, null, null,
									true));
					render = render || i > 0;
					if (i > 0)
						statusPieChart.set(MessageFactory
								.getMessage("status_" + s.getName(), "")
								.getDetail(), i);
				}

				if (render) {
					statusPieChart.setLegendPosition("e");
					statusPieChart.setTitle("% " + MessageFactory
							.getMessage("label_requirement", "").getDetail());
					statusPieChart.setFill(false);
					statusPieChart.setShowDataLabels(true);
					statusPieChart.setDiameter(150);
					statusPieChart.setSliceMargin(5);
				} else
					statusPieChart = null;
			}
			return statusPieChart;
		} else
			return null;
	}

	public void setStatusPieChart(PieChartModel statusPieChart) {
		this.statusPieChart = statusPieChart;
	}

	public PieChartModel getProjectPieChart() {

		if (activeTabIndex == KPI_TAB) {
			if (projectPieChart == null) {
				projectPieChart = new PieChartModel();
				List<JJProduct> prodReq = ((JJProductBean) LoginBean
						.findBean("jJProductBean")).getProductList();
				boolean render = false;
				for (JJProduct s : prodReq) {

					int i = Integer.parseInt(""
							+ jJRequirementService.getReqCount(
									((LoginBean) LoginBean
											.findBean("loginBean")).getContact()
													.getCompany(),
									project, s, null, null, null, null, null,
									true));
					render = render || i > 0;
					if (i > 0)
						projectPieChart.set(
								MessageFactory.getMessage("label_product", "")
										.getDetail() + " :" + s.getName(),
								i);
				}

				if (render) {
					projectPieChart.setLegendPosition("e");
					projectPieChart
							.setTitle(
									MessageFactory
											.getMessage(
													"statistique_kpi_projectPiechart_header",
													LoginBean.getProject()
															.getName())
											.getDetail());
					projectPieChart.setFill(false);
					projectPieChart.setShowDataLabels(true);
					projectPieChart.setDiameter(150);
					projectPieChart.setSliceMargin(5);
				} else
					projectPieChart = null;
			}
			return projectPieChart;
		} else
			return null;
	}

	public void setProjectPieChart(PieChartModel projectPieChart) {
		this.projectPieChart = projectPieChart;
	}

	public PieChartModel getProductPieChart() {

		if (activeTabIndex == KPI_TAB && LoginBean.getProduct() != null) {
			if (productPieChart == null) {
				productPieChart = new PieChartModel();
				List<JJProject> projReq = ((JJProjectBean) LoginBean
						.findBean("jJProjectBean")).getProjectList();
				boolean render = false;
				for (JJProject s : projReq) {

					int i = Integer
							.parseInt("" + jJRequirementService.getReqCount(
									((LoginBean) LoginBean
											.findBean("loginBean")).getContact()
													.getCompany(),
									s, LoginBean.getProduct(), null, null, null,
									null, null, true));
					render = render || i > 0;
					if (i > 0)
						productPieChart.set(
								MessageFactory.getMessage("label_project", "")
										.getDetail() + " :" + s.getName(),
								i);
				}

				if (render) {
					productPieChart.setLegendPosition("e");
					productPieChart
							.setTitle(
									MessageFactory
											.getMessage(
													"statistique_kpi_productPiechart_header",
													LoginBean.getProduct()
															.getName())
											.getDetail());
					productPieChart.setFill(false);
					productPieChart.setShowDataLabels(true);
					productPieChart.setDiameter(150);
					productPieChart.setSliceMargin(5);
				} else
					productPieChart = null;
			}
			return productPieChart;
		} else
			return null;
	}

	public void setProductPieChart(PieChartModel productPieChart) {
		this.productPieChart = productPieChart;
	}

	public PieChartModel getCategoryPieChart() {
		if (activeTabIndex == KPI_TAB) {
			if (categoryPieChart == null) {
				categoryPieChart = new PieChartModel();
				List<JJCategory> catReq = jJCategoryService.getCategories(null,
						false, true, true, LoginBean.getCompany());
				boolean render = false;
				for (JJCategory s : catReq) {

					int i = Integer
							.parseInt("" + jJRequirementService.getReqCount(
									((LoginBean) LoginBean
											.findBean("loginBean")).getContact()
													.getCompany(),
									project, LoginBean.getProduct(),
									LoginBean.getVersion(), null, s, null, null,
									true));
					render = render || i > 0;
					if (i > 0) {
						String label = MessageFactory.checkMessage(
								"category_" + s.getName().replace(" ", "_"),
								"") != null
										? MessageFactory.checkMessage(
												"category_" + s.getName()
														.replace(" ", "_"),
												"")
										: s.getName();

						categoryPieChart.set(
								MessageFactory.getMessage("label_category", "")
										.getDetail() + ":" + label,
								i);
					}

				}

				if (render) {
					categoryPieChart.setLegendPosition("e");
					categoryPieChart.setTitle(MessageFactory.getMessage(
							"statistique_kpi_categoryPiechart_header", "")
							.getDetail());
					categoryPieChart.setFill(false);
					categoryPieChart.setShowDataLabels(true);
					categoryPieChart.setDiameter(150);
					categoryPieChart.setSliceMargin(5);
				} else
					categoryPieChart = null;
			}
			return categoryPieChart;
		} else
			return null;

	}

	public void setCategoryPieChart(PieChartModel categoryPieChart) {
		this.categoryPieChart = categoryPieChart;
	}

	public PieChartModel getBugPieChart() {

		if (activeTabIndex == BUG_TAB) {
			if (bugPieChart == null) {
				bugPieChart = new PieChartModel();
				List<JJStatus> statBug = jJStatusService.getStatus("Bug", true,
						null, false);
				boolean render = false;
				for (JJStatus s : statBug) {

					int i = Integer
							.parseInt("" + jJBugService.getBugsCountByStaus(
									((LoginBean) LoginBean
											.findBean("loginBean")).getContact()
													.getCompany(),
									null, project, LoginBean.getProduct(),
									LoginBean.getVersion(), s, true));
					render = render || i > 0;
					if (i > 0)
						bugPieChart.set(MessageFactory
								.getMessage("status_" + s.getName(), "")
								.getDetail(), i);
				}

				if (render) {
					bugPieChart.setLegendPosition("e");
					bugPieChart.setTitle("% Bug");
					bugPieChart.setFill(false);
					bugPieChart.setShowDataLabels(true);
					bugPieChart.setDiameter(150);
					bugPieChart.setSliceMargin(5);
				} else
					bugPieChart = null;
			}
			return bugPieChart;
		} else
			return null;

	}

	public void setBugPieChart(PieChartModel bugPieChart) {
		this.bugPieChart = bugPieChart;
	}

	public MeterGaugeChartModel getBugMetergauge() {
		if (activeTabIndex == BUG_TAB) {
			if (bugMetergauge == null) {

				float bugKPI = 0L;

				JJRequirementBean jJRequirementBean = (JJRequirementBean) LoginBean
						.findBean("jJRequirementBean");

				if (jJRequirementBean == null)
					jJRequirementBean = new JJRequirementBean();

				List<JJRequirement> requirements = jJRequirementService
						.getRequirements(null,
								((LoginBean) LoginBean.findBean("loginBean"))
										.getAuthorizedMap("Requirement",
												LoginBean.getProject(),
												LoginBean.getProduct()),
								LoginBean.getVersion(), null);

				for (JJRequirement req : requirements) {

					bugKPI = bugKPI
							+ (1 / (1 + jJBugService.requirementBugCount(req)));
				}

				if (requirements != null && !requirements.isEmpty())
					bugKPI = bugKPI / requirements.size();

				@SuppressWarnings("serial")
				List<Number> bugIntervalls = new ArrayList<Number>() {
					{
						add(0.5);
						add(0.75);
						add(0.85);
						add(0.925);
						add(1);
					}
				};
				bugMetergauge = new MeterGaugeChartModel(bugKPI, bugIntervalls);
				bugMetergauge.setTitle("Bug KPI");
				bugMetergauge.setGaugeLabel("KPI");
				bugMetergauge.setMin(0.5);
				bugMetergauge.setMax(1);
				bugMetergauge.setShowTickLabels(true);
				bugMetergauge
						.setSeriesColors("FF0000,FF0000,FF7700,FFD000,008000");

			}
			return bugMetergauge;
		} else
			return null;

	}

	public void setBugMetergauge(MeterGaugeChartModel bugMetergauge) {
		this.bugMetergauge = bugMetergauge;
	}

	public LineChartModel getKpiLineModel() {

		if (activeTabIndex == KPI_TAB) {
			if (kpiLineModel == null) {

				kpiLineModel = initLinearModel();
			}
			return kpiLineModel;
		} else
			return null;

	}

	public void setKpiLineModel(LineChartModel kpiLineModel) {
		this.kpiLineModel = kpiLineModel;
	}

	public BarChartModel getKpiBarModel() {

		if (activeTabIndex == KPI_TAB) {
			if (kpiBarModel == null) {

				kpiBarModel = initBarModel();
			}
			return kpiBarModel;
		} else
			return null;

	}

	public void setKpiBarModel(BarChartModel kpiBarModel) {
		this.kpiBarModel = kpiBarModel;
	}

	public MeterGaugeChartModel getPrjMetergauge() {
		if (activeTabIndex == SPEC_TAB) {
			if (prjMetergauge == null) {
				float projKPI = 0;
				LoginBean loginBean = (LoginBean) LoginBean
						.findBean("loginBean");
				JJRequirementBean jJRequirementBean = (JJRequirementBean) LoginBean
						.findBean("jJRequirementBean");

				if (jJRequirementBean == null)
					jJRequirementBean = new JJRequirementBean();

				List<JJRequirement> requirements = jJRequirementService
						.getRequirements(null,
								loginBean.getAuthorizedMap("Requirement",
										LoginBean.getProject(),
										LoginBean.getProduct()),
								LoginBean.getVersion(), null);

				for (JJRequirement req : requirements) {

					if (project.getStartDate() != null
							&& project.getEndDate() != null
							&& jJRequirementBean.checkIfFinished(req))
						projKPI++;

				}

				if (project.getStartDate() != null
						&& project.getEndDate() != null) {
					float tmpsProj = (new Date().getTime()
							- project.getStartDate().getTime());
					float tmps = (project.getEndDate().getTime()
							- project.getStartDate().getTime());

					projKPI = (projKPI / (1 + requirements.size()))
							- (tmpsProj / (1 + tmps));
				}

				List<Number> prjIntervalls = new ArrayList<Number>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					{
						add(-1);
						add(-0.25);
						add(0.25);
						add(1);
					}
				};

				prjMetergauge = new MeterGaugeChartModel(projKPI,
						prjIntervalls);
				prjMetergauge.setTitle(MessageFactory
						.getMessage("label_project", "").getDetail() + " KPI");
				prjMetergauge.setGaugeLabel("KPI");
				prjMetergauge.setShowTickLabels(true);
				prjMetergauge.setMin(-1);
				prjMetergauge.setMax(1);
				prjMetergauge.setSeriesColors("FF0000,FF0000,008000,0000FF");
			}
			return prjMetergauge;
		} else
			return null;
	}

	public void setPrjMetergauge(MeterGaugeChartModel prjMetergauge) {
		this.prjMetergauge = prjMetergauge;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabSprintIndex) {
		this.activeTabIndex = activeTabSprintIndex;
	}

	public int getActiveTabSprintIndex() {
		return activeTabSprintIndex;
	}

	public void setActiveTabSprintIndex(int activeTabSprintIndex) {
		this.activeTabSprintIndex = activeTabSprintIndex;
	}

	public void setFirst(Boolean bb) {
		// this.first = bb;
	}

	public List<SprintUtil> getSprintList() {

		if (activeTabIndex == SPRINT_TAB) {
			JJSprintBean jJSprintBean = ((JJSprintBean) LoginBean
					.findBean("jJSprintBean"));
			if (jJSprintBean == null)
				jJSprintBean = new JJSprintBean();
			return jJSprintBean.getSprintList();
		} else
			return new ArrayList<SprintUtil>();

	}

	public Boolean emptysprintList;

	public boolean isEmptysprintList() {
		if (emptysprintList == null)
			emptysprintList = jJSprintService
					.getSprints(LoginBean.getProject(), true).isEmpty();
		return emptysprintList;
	}

	public void onTabStatChange() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> paramMap = context.getExternalContext()
				.getRequestParameterMap();
		if (paramMap.get("activeIndex") != null) {

			String paramIndex = paramMap.get("activeIndex");
			setActiveTabIndex(Integer.valueOf(paramIndex));
			System.out.println("###### ACtive tab: " + activeTabIndex);

			if (activeTabIndex == SPRINT_TAB && activeTabSprintIndex == -1) {
				Date now = new Date();
				int i = 0;
				while (i < getSprintList().size()) {
					if (!getSprintList().get(i).isRender()
							&& now.after(getSprintList().get(i).getSprint()
									.getStartDate())
							&& now.before(getSprintList().get(i).getSprint()
									.getEndDate())) {
						activeTabSprintIndex = i;
						i = getSprintList().size();
					}
					i++;
				}

				if (activeTabSprintIndex == -1) {
					long minDiff = -1, currentTime = new Date().getTime();
					SprintUtil minDate = null;
					for (SprintUtil s : getSprintList()) {
						if (!s.isRender()) {
							long diff = Math.abs(currentTime
									- s.getSprint().getEndDate().getTime());
							if ((minDiff == -1) || (diff < minDiff)) {
								minDiff = diff;
								minDate = s;
							}
						}

					}
					if (minDate != null)
						activeTabSprintIndex = getSprintList().indexOf(minDate);
					else
						activeTabSprintIndex = 0;
				}

			}
		} else if (paramMap.get("activeSprintIndex") != null) {

			String paramIndex = paramMap.get("activeSprintIndex");
			setActiveTabSprintIndex(Integer.valueOf(paramIndex));
			System.out.println("###### ACtive tab: " + activeTabSprintIndex);
		}
	}

	public boolean isRenderCreate() {
		return renderCreate;
	}

	public void setRenderCreate(boolean renderCreate) {
		this.renderCreate = renderCreate;
	}

	public void loadData() {

		if (project == null) {
			getProject();
		}

	}

	public void deleteStatus() {

		selectedStatus.setEnabled(false);
		updateJJStatus(selectedStatus);
		FacesMessage facesMessage = MessageFactory
				.getMessage("message_successfully_deleted", "Status", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		statusList = null;
		statusPieChart = null;
		states = null;
		contacts = null;
		value = null;
		lazyStatusList = null;
		objectOptions = null;

	}

	public void reset() {
		setJJStatus_(null);
		lazyStatusList = null;
		objectOptions = null;
		statusList = null;
		statusPieChart = null;
		states = null;
		contacts = null;
		value = null;
		categoryPieChart = null;
		productPieChart = null;
		projectPieChart = null;
		setCreateDialogVisible(false);
	}

	public List<String> completeObject(String query) {

		List<String> suggestions = new ArrayList<String>();
		if (tableNames == null)
			tableNames = jJStatusService.getTablesName();

		suggestions.add(null);
		for (String req : tableNames) {

			if (req.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(req);
			}
		}
		return suggestions;
	}

	public void persistStatus() {
		String message = "";

		if (getJJStatus_().getId() != null) {
			updateJJStatus(getJJStatus_());
			message = "message_successfully_updated";

		} else {
			getJJStatus_().setDescription("Satus : " + getJJStatus_().getName()
					+ " for " + getJJStatus_().getObjet() + "object");
			getJJStatus_().setEnabled(true);
			saveJJStatus(getJJStatus_());
			message = "message_successfully_created";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message, "Status",
				"");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('statusDialogWidget').hide()");

		RequestContext.getCurrentInstance().update("growlForm");

		reset();

	}
	private BarChartModel initBarModel() {
		BarChartModel model = new BarChartModel();

		List<JJContact> contacts = jJPermissionService.areAuthorized(
				LoginBean.getCompany(), null, LoginBean.getProject(),
				LoginBean.getProduct(), "project");
		JJStatus doneStatus = jJStatusService.getOneStatus("DONE", "task",
				true);
		ChartSeries planned = new ChartSeries();
		planned.setLabel(MessageFactory
				.getMessage("statistique_kpi_taskBarchart_planned", "")
				.getDetail());
		ChartSeries executed = new ChartSeries();
		executed.setLabel(MessageFactory
				.getMessage("statistique_kpi_taskBarchart_executed", "")
				.getDetail());
		ChartSeries defined = new ChartSeries();
		defined.setLabel(MessageFactory
				.getMessage("statistique_kpi_taskBarchart_defined", "")
				.getDetail());

		for (JJContact contact : contacts) {
			int plannedDeviation = 0, executeDeviation = 0,
					definedDeviation = 0;
			boolean add = false;
			List<JJTask> tasks = jJTaskService.getExecutedTaks(null,
					LoginBean.getProject(), LoginBean.getProduct(), contact,
					doneStatus, null);

			for (JJTask tt : tasks) {
				add = true;
				if (tt.getWorkloadReal() != null)
					executeDeviation = executeDeviation + Math.abs(
							tt.getWorkloadPlanned() - tt.getWorkloadReal());
			}

			tasks = jJTaskService.getPlannedTaks(null, LoginBean.getProject(),
					LoginBean.getProduct(), contact, null);

			for (JJTask tt : tasks) {
				add = true;
				if (tt.getWorkloadReal() != null)
					plannedDeviation = plannedDeviation + Math.abs(
							tt.getWorkloadPlanned() - tt.getWorkloadReal());
			}
			tasks = jJTaskService.getDefinedTaks(null, LoginBean.getProject(),
					LoginBean.getProduct(), contact, null);

			for (JJTask tt : tasks) {
				add = true;
				if (tt.getWorkloadReal() != null)
					definedDeviation = definedDeviation + Math.abs(
							tt.getWorkloadPlanned() - tt.getWorkloadReal());
			}
			if (add) {
				executed.set(contact.getFirstname() + " " + contact.getName(),
						executeDeviation);

				planned.set(contact.getFirstname() + " " + contact.getName(),
						plannedDeviation);

				defined.set(contact.getFirstname() + " " + contact.getName(),
						definedDeviation);
			}

		}

		model.addSeries(executed);
		model.addSeries(planned);
		model.addSeries(defined);

		model.setTitle(MessageFactory
				.getMessage("statistique_kpi_taskBarchart_header", "")
				.getDetail());
		model.setLegendPosition("e");
		model.setSeriesColors("109618,FF9900,3366CC");
		model.setStacked(true);

		Axis xAxis = model.getAxis(AxisType.X);
		xAxis.setLabel("Contact");

		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel("Temps (h)");

		return model;
	}

	private LineChartModel initLinearModel() {

		LineChartModel model = new LineChartModel();
		final long Hours_IN_MILLIS = 60 * 60 * 1000;
		List<JJSprint> sprints = jJSprintService
				.getSprints(LoginBean.getProject(), true);

		ChartSeries blueSeries = new ChartSeries();
		blueSeries.setLabel(MessageFactory
				.getMessage("statistique_help_kpi_blue", "").getDetail());

		ChartSeries purpleSerie = new ChartSeries();
		purpleSerie.setLabel(MessageFactory
				.getMessage("statistique_help_kpi_purple", "").getDetail());

		ChartSeries greenSerie = new ChartSeries();
		greenSerie.setLabel(MessageFactory
				.getMessage("statistique_help_kpi_green", "").getDetail());

		for (JJSprint sp : sprints) {
			List<JJTask> tasks = jJTaskService.getTasksByObject(sp,
					LoginBean.getProject(), LoginBean.getProduct(), "bug",
					true);

			float moyBlue = 0;
			float moyPurple = 0;

			for (JJTask ta : tasks) {
				if (ta.getWorkloadReal() != null)
					moyBlue = moyBlue + ta.getWorkloadReal();
				Date endDate = ta.getEndDateReal();
				if (endDate == null)
					endDate = new Date();

				moyPurple = moyPurple + endDate.getTime() / Hours_IN_MILLIS
						- ta.getBug().getCreationDate().getTime()
								/ Hours_IN_MILLIS;

			}
			moyBlue = moyBlue / tasks.size();
			moyPurple = moyPurple / tasks.size();
			blueSeries.set(sp.getName(), moyBlue);
			purpleSerie.set(sp.getName(), moyPurple);

			tasks = jJTaskService.getTasksByObject(sp, LoginBean.getProject(),
					LoginBean.getProduct(), "requirement", true);
			float moyGreen = 0;
			for (JJTask ta : tasks) {
				moyGreen = moyGreen + ta.getWorkloadReal();
			}
			moyGreen = moyGreen / tasks.size();
			greenSerie.set(sp.getName(), moyGreen);

		}
		model.addSeries(blueSeries);
		model.addSeries(purpleSerie);
		model.addSeries(greenSerie);

		model.setTitle(MessageFactory
				.getMessage("statistique_kpi_kpiLineModel_header", "")
				.getDetail());
		model.setLegendPosition("e");
		model.setShowPointLabels(true);
		model.setSeriesColors("0288D1,5C6BC0,59A45D");
		model.getAxes().put(AxisType.X, new CategoryAxis("Sprint"));
		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel("Temps(h)");
		yAxis.setMin(0);
		yAxis.setMax(700);

		return model;
	}

	public void handleTestStatChange(AjaxBehaviorEvent e) {

		testContacts = null;
		testStates = null;
		testValues = null;
	}

	public class CategoryDataModel {

		private JJCategory category;

		private float coverageProgress = -1;
		private float completionProgress = -1;
		private PieChartModel categoryPieChart;

		public CategoryDataModel(JJCategory category) {
			this.category = category;
			this.coverageProgress = calculateCoverageProgress();
			this.completionProgress = calculateCompletionProgress();

		}

		public JJCategory getCategory() {
			return category;
		}

		public void setCategory(JJCategory category) {
			this.category = category;
		}

		public PieChartModel getCategoryPieChart() {

			categoryPieChart = new PieChartModel();
			categoryPieChart.set("Done", completionProgress);
			categoryPieChart.set("Not", 100 - completionProgress);
			categoryPieChart.setShowDataLabels(true);
			categoryPieChart.setFill(true);
			categoryPieChart.setSeriesColors("8BADC6,FFFDF6");

			return categoryPieChart;
		}

		public void setCategoryPieChart(PieChartModel categoryPieChart) {
			this.categoryPieChart = categoryPieChart;
		}

		public float getCoverageProgress() {
			return coverageProgress;
		}

		public void setCoverageProgress(float coverageProgress) {
			this.coverageProgress = coverageProgress;
		}

		public float getCompletionProgress() {

			return completionProgress;

		}

		public void setCompletionProgress(float completionProgress) {
			this.completionProgress = completionProgress;
		}

		public float calculateCoverageProgress() {

			if (coverageProgress == -1) {
				JJRequirementBean jJRequirementBean = (JJRequirementBean) LoginBean
						.findBean("jJRequirementBean");
				boolean containCategory = false;
				if (jJRequirementBean != null
						&& jJRequirementBean.getTableDataModelList() != null
						&& !jJRequirementBean.getTableDataModelList()
								.isEmpty()) {

					for (int i = 0; i < jJRequirementBean
							.getTableDataModelList().size(); i++) {
						if (jJRequirementBean.getTableDataModelList().get(i)
								.getCategoryId() != 0
								&& category.getId()
										.equals(jJRequirementBean
												.getTableDataModelList().get(i)
												.getCategoryId())) {
							if (jJRequirementBean.getTableDataModelList().get(i)
									.getCoverageProgress() == -1)
								jJRequirementBean.getTableDataModelList().get(i)
										.calculCoverageProgress();
							coverageProgress = jJRequirementBean
									.getTableDataModelList().get(i)
									.getCoverageProgress() / 100;
							containCategory = true;
							i = jJRequirementBean.getTableDataModelList()
									.size();
						}
					}
				}
				if (!containCategory) {
					float compteur = 0;
					LoginBean loginBean = (LoginBean) LoginBean
							.findBean("loginBean");
					List<JJRequirement> dataList = jJRequirementService
							.getRequirements(
									((LoginBean) LoginBean
											.findBean("loginBean")).getContact()
													.getCompany(),
									category,
									loginBean.getAuthorizedMap("Requirement",
											project, LoginBean.getProduct()),
									LoginBean.getVersion(), null, null, false,false,
									true, false, false, null);

					boolean isLowCategory = jJCategoryService
							.isLowLevel(category, LoginBean.getCompany());
					boolean isHighCategory = jJCategoryService
							.isHighLevel(category, LoginBean.getCompany());

					for (JJRequirement requirement : dataList) {
						{
							requirement = JJRequirementBean.getRowState(
									requirement, jJRequirementService);
							if (requirement.getState().getName()
									.equalsIgnoreCase(
											JJRequirementBean.jJRequirement_InProgress)
									|| requirement.getState().getName()
											.equalsIgnoreCase(
													JJRequirementBean.jJRequirement_InTesting)
									|| requirement.getState().getName()
											.equalsIgnoreCase(
													JJRequirementBean.jJRequirement_Finished)) {
								compteur++;
							} else if (isLowCategory && requirement.getState()
									.getName().equalsIgnoreCase(
											JJRequirementBean.jJRequirement_Specified)) {
								compteur++;

							} else if (isHighCategory && (requirement.getState()
									.getName().equalsIgnoreCase(
											JJRequirementBean.jJRequirement_Specified)
									|| jJTaskService.haveTask(requirement, true,
											false, false))) {

								compteur += 0.5;

							} else if (!isHighCategory && !isLowCategory
									&& requirement.getState().getName()
											.equalsIgnoreCase(
													JJRequirementBean.jJRequirement_Specified)) {
								compteur++;
							} else if (!isHighCategory && !isLowCategory
									&& (jJRequirementService
											.haveLinkUp(requirement)
											|| jJRequirementService
													.haveLinkDown(
															requirement))) {
								compteur += 0.5;
							}
						}
					}

					if (dataList.isEmpty()) {
						coverageProgress = 0;
					} else {
						coverageProgress = compteur / dataList.size();
					}
				}

			}

			return coverageProgress * 100;

		}

		public float calculateCompletionProgress() {

			if (completionProgress == -1) {

				JJRequirementBean jJRequirementBean = (JJRequirementBean) LoginBean
						.findBean("jJRequirementBean");
				boolean containCategory = false;
				if (jJRequirementBean != null
						&& jJRequirementBean.getTableDataModelList() != null
						&& !jJRequirementBean.getTableDataModelList()
								.isEmpty()) {

					for (int i = 0; i < jJRequirementBean
							.getTableDataModelList().size(); i++) {
						if (jJRequirementBean.getTableDataModelList().get(i)
								.getCategoryId() != 0
								&& category.getId()
										.equals(jJRequirementBean
												.getTableDataModelList().get(i)
												.getCategoryId())) {
							if (jJRequirementBean.getTableDataModelList().get(i)
									.getCompletionProgress() == -1)
								jJRequirementBean.getTableDataModelList().get(i)
										.calculCompletionProgress();
							completionProgress = jJRequirementBean
									.getTableDataModelList().get(i)
									.getCompletionProgress() / 100;
							containCategory = true;
							i = jJRequirementBean.getTableDataModelList()
									.size();
						}
					}
				}
				if (!containCategory) {
					float compteur = 0;
					LoginBean loginBean = (LoginBean) LoginBean
							.findBean("loginBean");
					List<JJRequirement> dataList = jJRequirementService
							.getRequirements(
									((LoginBean) LoginBean
											.findBean("loginBean")).getContact()
													.getCompany(),
									category,
									loginBean.getAuthorizedMap("Requirement",
											project, LoginBean.getProduct()),
									LoginBean.getVersion(), null, null, false,false,
									true, false, false, null);

					for (JJRequirement requirement : dataList) {
						compteur = compteur + calculCompletion(requirement);
					}

					if (dataList.isEmpty()) {
						completionProgress = 0;
					} else {
						completionProgress = compteur / dataList.size();
					}
				}

			}

			return completionProgress * 100;
		}

		private float calculCompletion(JJRequirement requirement) {
			float compteur = 0;
			int size = 0;
			Set<JJRequirement> linksUp = requirement.getRequirementLinkUp();
			for (JJRequirement req : linksUp) {
				if (req.getEnabled()) {
					compteur = compteur + calculCompletion(req);
					size++;
				}
			}

			int hasTaskCompleted = 0;

			// if (jJTaskService.haveTask(requirement, true, true, false)) {
			// compteur++;
			// hasTaskCompleted = 1;
			// }

			if (JJRequirementBean.getRowState(requirement, jJRequirementService)
					.getState().getName().equalsIgnoreCase(
							JJRequirementBean.jJRequirement_InProgress)
					|| JJRequirementBean
							.getRowState(requirement, jJRequirementService)
							.getState().getName().equalsIgnoreCase(
									JJRequirementBean.jJRequirement_InTesting)
					|| JJRequirementBean
							.getRowState(requirement, jJRequirementService)
							.getState().getName().equalsIgnoreCase(
									JJRequirementBean.jJRequirement_Finished)) {
				compteur++;
				hasTaskCompleted = 1;
			}
			if (size > 0) {
				compteur = compteur / (size + hasTaskCompleted);
			}

			return compteur;
		}

		@Override
		public boolean equals(Object obj) {

			return (obj instanceof CategoryDataModel) && (getCategory() != null)
					? getCategory()
							.equals(((CategoryDataModel) obj).getCategory())
					: (obj == this);
		}

	}

	public void saveJJStatus(JJStatus b) {

		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJStatusService.saveJJStatus(b);
	}

	public void updateJJStatus(JJStatus b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJStatusService.updateJJStatus(b);
	}

	public HtmlPanelGrid populateCreatePanel() {
		return null;
	}

	public HtmlPanelGrid populateEditPanel() {
		return null;
	}

	public HtmlPanelGrid populateViewPanel() {
		return null;
	}

}
