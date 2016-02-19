package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJSprintService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyStatusDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.SprintUtil;

@RooSerializable
@RooJsfManagedBean(entity = JJStatus.class, beanName = "jJStatusBean")
public class JJStatusBean {

	@Autowired
	private JJCategoryService jJCategoryService;

	@Autowired
	private JJRequirementService jJRequirementService;

	@Autowired
	private JJBugService jJBugService;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJSprintService jJSprintService;

	private List<String> tableNames;

	private List<JJStatus> statusList;
	private LazyStatusDataModel lazyStatusList;
	private JJStatus selectedStatus;
	private PieChartModel pieChart;
	private PieChartModel bugPieChart;
	private SelectItem[] objectOptions;
	private MeterGaugeChartModel bugMetergauge;
	private LineChartModel bugLineModel;
	private MeterGaugeChartModel prjMetergauge;
	private JJProject project;
	private List<CategoryDataModel> categoryDataModel;
	private int activeTabIndex;
	private int activeTabSprintIndex = -1;
	private boolean renderCreate;

	// private Boolean first;

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

	public String onEdit() {
		return null;
	}

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
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
			bugMetergauge = null;
			bugLineModel = null;

		}

	}

	public List<CategoryDataModel> getCategoryDataModel() {
		return categoryDataModel;
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

	public PieChartModel getPieChart() {

		if (pieChart == null) {
			pieChart = new PieChartModel();
			List<JJStatus> statReq = jJStatusService.getStatus("Requirement",
					true, null, false);
			boolean render = false;
			for (JJStatus s : statReq) {

				int i = Integer
						.parseInt(
								"" + jJRequirementService.getReqCountByStaus(
										((LoginBean) LoginBean
												.findBean("loginBean"))
														.getContact()
														.getCompany(),
										project, LoginBean.getProduct(),
										LoginBean.getVersion(), s, true));
				render = render || i > 0;
				if (i > 0)
					pieChart.set(MessageFactory
							.getMessage("status_" + s.getName(), "")
							.getDetail(), i);
			}

			if (render) {
				pieChart.setLegendPosition("e");
				pieChart.setTitle("% " + MessageFactory
						.getMessage("label_requirement", "").getDetail());
				pieChart.setFill(false);
				pieChart.setShowDataLabels(true);
				pieChart.setDiameter(150);
				pieChart.setSliceMargin(5);
			} else
				pieChart = null;
		}
		return pieChart;
	}

	public PieChartModel getBugPieChart() {

		if (activeTabIndex == 1) {
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
									project, LoginBean.getProduct(),
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
		if (activeTabIndex == 1) {
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
								LoginBean.getVersion());

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

	public LineChartModel getBugLineModel() {

		if (activeTabIndex == 1) {
			if (bugLineModel == null) {

				bugLineModel = initLinearModel();
			}
			return bugLineModel;
		} else
			return null;

	}

	public void setBugLineModel(LineChartModel bugLineModel) {
		this.bugLineModel = bugLineModel;
	}

	public MeterGaugeChartModel getPrjMetergauge() {
		return prjMetergauge;
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

		if (activeTabIndex == 2) {
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

			if (activeTabIndex == 2 && activeTabSprintIndex == -1) {
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

	// public void beforeDialogShow(ActionEvent event) {
	//
	// setJJStatus__(new JJStatus());
	// renderCreate = true;
	// RequestContext context = RequestContext.getCurrentInstance();
	// context.execute("PF('statusDialogWidget').show()");
	//
	// }

	@SuppressWarnings("serial")
	public void loadData() {

		if (project == null) {
			getProject();

			if (project != null) {
				categoryDataModel = new ArrayList<CategoryDataModel>();

				List<JJCategory> categoryList = jJCategoryService.getCategories(
						null, false, true, true,
						((LoginBean) LoginBean.findBean("loginBean"))
								.getContact().getCompany());

				for (JJCategory category : categoryList) {
					categoryDataModel.add(new CategoryDataModel(category));
				}

				pieChart = new PieChartModel();
				List<JJStatus> statReq = jJStatusService
						.getStatus("Requirement", true, null, false);
				boolean render = false;
				for (JJStatus s : statReq) {

					int i = Integer.parseInt(
							"" + jJRequirementService.getReqCountByStaus(
									((LoginBean) LoginBean
											.findBean("loginBean")).getContact()
													.getCompany(),
									project, LoginBean.getProduct(),
									LoginBean.getVersion(), s, true));
					render = render || i > 0;
					if (i > 0)
						pieChart.set(MessageFactory
								.getMessage("status_" + s.getName(), "")
								.getDetail(), i);
				}

				if (render) {
					pieChart.setLegendPosition("e");
					pieChart.setTitle("% " + MessageFactory
							.getMessage("label_requirement", "").getDetail());
					pieChart.setFill(false);
					pieChart.setShowDataLabels(true);
					pieChart.setDiameter(150);
					pieChart.setSliceMargin(5);
				} else
					pieChart = null;

				// float bugKPI = 0L;
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
								LoginBean.getVersion());

				for (JJRequirement req : requirements) {

					// bugKPI = bugKPI
					// + (1 / (1 + jJBugService.requirementBugCount(req)));

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

				// if (requirements != null && !requirements.isEmpty())
				// bugKPI = bugKPI / requirements.size();

				List<Number> prjIntervalls = new ArrayList<Number>() {
					// {
					// add(-1);
					// add(-0.50);
					// add(-0.30);
					// add(-0.15);
					// add(0.15);
					// add(0.30);
					// add(0.50);
					// add(1);
					// }
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

				// List<Number> bugIntervalls = new ArrayList<Number>() {
				// {
				// add(0.5);
				// add(0.75);
				// add(0.85);
				// add(0.925);
				// add(1);
				// }
				// };
				// bugMetergauge = new MeterGaugeChartModel(bugKPI,
				// bugIntervalls);
				// bugMetergauge.setTitle("Bug KPI");
				// bugMetergauge.setGaugeLabel("KPI");
				// bugMetergauge.setMin(0.5);
				// bugMetergauge.setMax(1);
				// bugMetergauge.setShowTickLabels(true);
				// bugMetergauge
				// .setSeriesColors("FF0000,FF0000,FF7700,FFD000,008000");

			}

		} else if (categoryDataModel == null) {

			categoryDataModel = new ArrayList<CategoryDataModel>();

			List<JJCategory> categoryList = jJCategoryService.getCategories(
					null, false, true, true,
					((LoginBean) LoginBean.findBean("loginBean")).getContact()
							.getCompany());

			for (JJCategory category : categoryList) {
				categoryDataModel.add(new CategoryDataModel(category));
			}
		}
	}

	public void setPieChart(PieChartModel pieChart) {
		this.pieChart = pieChart;
	}

	public void deleteStatus() {

		selectedStatus.setEnabled(false);
		updateJJStatus(selectedStatus);
		FacesMessage facesMessage = MessageFactory
				.getMessage("message_successfully_deleted", "Status", "");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		statusList = null;
		pieChart = null;
		lazyStatusList = null;
		objectOptions = null;

	}

	public void reset() {
		setJJStatus_(null);
		lazyStatusList = null;
		objectOptions = null;
		statusList = null;
		pieChart = null;
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

	private LineChartModel initLinearModel() {

		LineChartModel model = new LineChartModel();
		final long Hours_IN_MILLIS = 60 * 60 * 1000;
		List<JJSprint> sprints = jJSprintService
				.getSprints(LoginBean.getProject(), true);

		ChartSeries blueSeries = new ChartSeries();
		blueSeries.setLabel(MessageFactory
				.getMessage("statistique_help_bug_blue", "").getDetail());

		ChartSeries purpleSerie = new ChartSeries();
		purpleSerie.setLabel(MessageFactory
				.getMessage("statistique_help_bug_purple", "").getDetail());

		ChartSeries greenSerie = new ChartSeries();
		greenSerie.setLabel(MessageFactory
				.getMessage("statistique_help_bug_green", "").getDetail());

		for (JJSprint sp : sprints) {
			List<JJTask> tasks = jJTaskService.getTasksByObject(sp,
					LoginBean.getProject(), LoginBean.getProduct(), "bug",
					true);

			float moyBlue = 0;
			float moyPurple = 0;

			for (JJTask ta : tasks) {
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

		model.setTitle("Statistique d'exécution");
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
									LoginBean.getVersion(), null, null, false,
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
									LoginBean.getVersion(), null, null, false,
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
