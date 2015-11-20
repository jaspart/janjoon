package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.model.SelectItem;
import javax.faces.validator.LengthValidator;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
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
import com.starit.janjoonweb.domain.JJSprintService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.converter.JJContactConverter;
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

	private List<JJStatus> statusList;
	private LazyStatusDataModel lazyStatusList;
	private JJStatus selectedStatus;
	private PieChartModel pieChart;
	private PieChartModel bugPieChart;
	private SelectItem[] objectOptions;
	private MeterGaugeChartModel bugMetergauge;
	private MeterGaugeChartModel prjMetergauge;
	private JJProject project;
	private List<CategoryDataModel> categoryDataModel;
	private int activeTabIndex;
	private int activeTabSprintIndex;

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
			activeTabSprintIndex = 0;
			prjMetergauge = null;
			bugPieChart = null;
			bugMetergauge = null;

		}

	}

	public List<CategoryDataModel> getCategoryDataModel() {
		return categoryDataModel;
	}

	public void setCategoryDataModel(List<CategoryDataModel> categoryDataModel) {
		this.categoryDataModel = categoryDataModel;
	}

	public SelectItem[] getObjectOptions() {

		if (objectOptions == null) {

			Set<String> objects = jJStatusService.getAllObject();
			objectOptions = new SelectItem[objects.size() + 1];

			objectOptions[0] = new SelectItem("", "Select");
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

				int i = Integer.parseInt(""
						+ jJRequirementService.getReqCountByStaus(
								((LoginBean) LoginBean.findBean("loginBean"))
										.getContact().getCompany(), project,
								LoginBean.getProduct(), LoginBean.getVersion(),
								s, true));
				render = render || i > 0;
				if (i > 0)
					pieChart.set(
							MessageFactory.getMessage("status_" + s.getName(),
									"").getDetail(), i);
			}

			if (render) {
				pieChart.setLegendPosition("e");
				pieChart.setTitle("% "
						+ MessageFactory.getMessage("label_requirement", "")
								.getDetail());
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

					int i = Integer.parseInt(""
							+ jJBugService.getBugsCountByStaus(
									((LoginBean) LoginBean
											.findBean("loginBean"))
											.getContact().getCompany(),
									project, LoginBean.getProduct(), LoginBean
											.getVersion(), s, true));
					render = render || i > 0;
					if (i > 0)
						bugPieChart.set(
								MessageFactory.getMessage(
										"status_" + s.getName(), "")
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
						.getRequirements(null, ((LoginBean) LoginBean
								.findBean("loginBean")).getAuthorizedMap(
								"Requirement", LoginBean.getProject(),
								LoginBean.getProduct()), LoginBean.getVersion());

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
			emptysprintList = jJSprintService.getSprints(
					LoginBean.getProject(), true).isEmpty();
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
		} else if (paramMap.get("activeSprintIndex") != null) {

			String paramIndex = paramMap.get("activeSprintIndex");
			setActiveTabSprintIndex(Integer.valueOf(paramIndex));
			System.out.println("###### ACtive tab: " + activeTabSprintIndex);
		}
	}

	@SuppressWarnings("serial")
	public void loadData() {

		if (project == null) {
			getProject();

			if (project != null) {
				categoryDataModel = new ArrayList<CategoryDataModel>();

				List<JJCategory> categoryList = jJCategoryService
						.getCategories(null, false, true, true,
								((LoginBean) LoginBean.findBean("loginBean"))
										.getContact().getCompany());

				for (JJCategory category : categoryList) {
					categoryDataModel.add(new CategoryDataModel(category));
				}

				pieChart = new PieChartModel();
				List<JJStatus> statReq = jJStatusService.getStatus(
						"Requirement", true, null, false);
				boolean render = false;
				for (JJStatus s : statReq) {

					int i = Integer.parseInt(""
							+ jJRequirementService.getReqCountByStaus(
									((LoginBean) LoginBean
											.findBean("loginBean"))
											.getContact().getCompany(),
									project, LoginBean.getProduct(), LoginBean
											.getVersion(), s, true));
					render = render || i > 0;
					if (i > 0)
						pieChart.set(
								MessageFactory.getMessage(
										"status_" + s.getName(), "")
										.getDetail(), i);
				}

				if (render) {
					pieChart.setLegendPosition("e");
					pieChart.setTitle("% "
							+ MessageFactory
									.getMessage("label_requirement", "")
									.getDetail());
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
						.getRequirements(null, loginBean.getAuthorizedMap(
								"Requirement", LoginBean.getProject(),
								LoginBean.getProduct()), LoginBean.getVersion());

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
					float tmpsProj = (new Date().getTime() - project
							.getStartDate().getTime());
					float tmps = (project.getEndDate().getTime() - project
							.getStartDate().getTime());

					projKPI = (projKPI / (1 + requirements.size()))
							- (tmpsProj / (1 + tmps));
				}

				// if (requirements != null && !requirements.isEmpty())
				// bugKPI = bugKPI / requirements.size();

				List<Number> prjIntervalls = new ArrayList<Number>() {
					{
						add(-1);
						add(-0.50);
						add(-0.30);
						add(-0.15);
						add(0.15);
						add(0.30);
						add(0.50);
						add(1);
					}
				};

				prjMetergauge = new MeterGaugeChartModel(projKPI, prjIntervalls);
				prjMetergauge.setTitle(MessageFactory.getMessage(
						"label_project", "").getDetail()
						+ " KPI");
				prjMetergauge.setGaugeLabel("KPI");
				prjMetergauge.setShowTickLabels(true);
				prjMetergauge.setMin(-1);
				prjMetergauge.setMax(1);
				prjMetergauge
						.setSeriesColors("FF0000,FF0000,FF7700,FFD000,008000,FFD000,FF7700,FF0000");

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
					null, false, true, true, ((LoginBean) LoginBean
							.findBean("loginBean")).getContact().getCompany());

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
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", "Status");
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

		return jJStatusService.getTablesName();
	}

	public String persistStatus() {
		String message = "";

		if (getJJStatus_().getId() != null) {
			updateJJStatus(getJJStatus_());
			message = "message_successfully_updated";

		} else {
			getJJStatus_().setDescription(
					"Satus : " + getJJStatus_().getName() + " for "
							+ getJJStatus_().getObjet() + "object");
			getJJStatus_().setEnabled(true);
			saveJJStatus(getJJStatus_());
			message = "message_successfully_created";
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('createSatDialogWidget').hide()");
		context.execute("PF('editSatDialogWidget').hide()");

		FacesMessage facesMessage = MessageFactory
				.getMessage(message, "Status");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		return findAllJJStatuses();

	}

	public HtmlPanelGrid populateCreatePanel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		javax.faces.application.Application application = facesContext
				.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		OutputLabel nameCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		nameCreateOutput.setFor("nameStatCreateInput");
		nameCreateOutput.setId("nameStatCreateOutput");
		nameCreateOutput.setValue(MessageFactory.getMessage(
				"admin_status_table_name_title", "").getDetail()
				+ ":");
		htmlPanelGrid.getChildren().add(nameCreateOutput);

		InputTextarea nameCreateInput = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		nameCreateInput.setId("nameStatCreateInput");
		nameCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.name}", String.class));
		LengthValidator nameCreateInputValidator = new LengthValidator();
		nameCreateInputValidator.setMaximum(100);
		nameCreateInput.addValidator(nameCreateInputValidator);
		nameCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(nameCreateInput);

		Message nameCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		nameCreateInputMessage.setId("nameStatCreateInputMessage");
		nameCreateInputMessage.setFor("nameStatCreateInput");
		nameCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(nameCreateInputMessage);

		OutputLabel objetCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		objetCreateOutput.setFor("objetStatCreateInput");
		objetCreateOutput.setId("objetStatCreateOutput");
		objetCreateOutput.setValue("Objet:");
		htmlPanelGrid.getChildren().add(objetCreateOutput);

		AutoComplete objetCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		objetCreateInput.setId("objetStatCreateInput");
		objetCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.objet}", String.class));
		objetCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJStatusBean.completeObject}", List.class,
						new Class[] { String.class }));
		objetCreateInput.setDropdown(true);
		objetCreateInput.setRequired(true);
		objetCreateInput.setScrollHeight(200);
		objetCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "objet", String.class));
		objetCreateInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{objet}", String.class));
		objetCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{objet}", String.class));
		htmlPanelGrid.getChildren().add(objetCreateInput);

		Message objetCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		objetCreateInputMessage.setId("objetStatCreateInputMessage");
		objetCreateInputMessage.setFor("objetStatCreateInput");
		objetCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(objetCreateInputMessage);

		OutputLabel levelStatusCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		levelStatusCreateOutput.setFor("levelStatusCreateInput");
		levelStatusCreateOutput.setId("levelStatusCreateOutput");
		levelStatusCreateOutput.setValue("Level Status:");
		htmlPanelGrid.getChildren().add(levelStatusCreateOutput);

		Spinner levelStatusCreateInput = (Spinner) application
				.createComponent(Spinner.COMPONENT_TYPE);
		levelStatusCreateInput.setId("levelStatusCreateInput");
		levelStatusCreateInput
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.levelStatus}",
								Integer.class));
		levelStatusCreateInput.setRequired(false);

		htmlPanelGrid.getChildren().add(levelStatusCreateInput);

		Message levelStatusCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		levelStatusCreateInputMessage.setId("levelStatusCreateInputMessage");
		levelStatusCreateInputMessage.setFor("levelStatusCreateInput");
		levelStatusCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(levelStatusCreateInputMessage);

		HtmlOutputText messagesCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesCreateOutput.setId("messagesStatCreateOutput");
		messagesCreateOutput.setValue("Messages:");
		htmlPanelGrid.getChildren().add(messagesCreateOutput);

		HtmlOutputText messagesCreateInput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesCreateInput.setId("messagesStatCreateInput");
		messagesCreateInput
				.setValue("This relationship is managed from the JJMessage side");
		htmlPanelGrid.getChildren().add(messagesCreateInput);

		Message messagesCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		messagesCreateInputMessage.setId("messagesStatCreateInputMessage");
		messagesCreateInputMessage.setFor("messagesStatCreateInput");
		messagesCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(messagesCreateInputMessage);

		return htmlPanelGrid;
	}

	public HtmlPanelGrid populateEditPanel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		javax.faces.application.Application application = facesContext
				.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		OutputLabel nameEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		nameEditOutput.setFor("nameStatEditInput");
		nameEditOutput.setId("nameStatEditOutput");
		nameEditOutput.setValue(MessageFactory.getMessage(
				"admin_status_table_name_title", "").getDetail()
				+ ":");

		htmlPanelGrid.getChildren().add(nameEditOutput);

		InputTextarea nameEditInput = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		nameEditInput.setId("nameStatEditInput");
		nameEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.name}", String.class));
		LengthValidator nameEditInputValidator = new LengthValidator();
		nameEditInputValidator.setMaximum(100);
		nameEditInput.addValidator(nameEditInputValidator);
		nameEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(nameEditInput);

		Message nameEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		nameEditInputMessage.setId("nameStatEditInputMessage");
		nameEditInputMessage.setFor("nameStatEditInput");
		nameEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(nameEditInputMessage);

		OutputLabel objetEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		objetEditOutput.setFor("objetStatEditInput");
		objetEditOutput.setId("objetStatEditOutput");
		objetEditOutput.setValue("Objet:");
		htmlPanelGrid.getChildren().add(objetEditOutput);

		AutoComplete objetEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		objetEditInput.setId("objetStatEditInput");
		// objetEditInput.
		objetEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.objet}", String.class));
		objetEditInput.setRequired(true);
		objetEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJStatusBean.completeObject}", List.class,
						new Class[] { String.class }));
		objetEditInput.setDropdown(true);
		objetEditInput.setScrollHeight(200);
		objetEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "objet", String.class));
		objetEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{objet}", String.class));
		objetEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{objet}", String.class));
		objetEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(objetEditInput);

		Message objetEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		objetEditInputMessage.setId("objetStatEditInputMessage");
		objetEditInputMessage.setFor("objetStatEditInput");
		objetEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(objetEditInputMessage);

		OutputLabel levelStatusEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		levelStatusEditOutput.setFor("levelStatusEditInput");
		levelStatusEditOutput.setId("levelStatusEditOutput");
		levelStatusEditOutput.setValue("Level Status:");
		htmlPanelGrid.getChildren().add(levelStatusEditOutput);

		Spinner levelStatusEditInput = (Spinner) application
				.createComponent(Spinner.COMPONENT_TYPE);
		levelStatusEditInput.setId("levelStatusEditInput");
		levelStatusEditInput
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.levelStatus}",
								Integer.class));
		levelStatusEditInput.setRequired(false);

		htmlPanelGrid.getChildren().add(levelStatusEditInput);

		Message levelStatusEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		levelStatusEditInputMessage.setId("levelStatusEditInputMessage");
		levelStatusEditInputMessage.setFor("levelStatusEditInput");
		levelStatusEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(levelStatusEditInputMessage);

		return htmlPanelGrid;
	}

	public HtmlPanelGrid populateViewPanel() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		javax.faces.application.Application application = facesContext
				.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		HtmlOutputText nameLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		nameLabel.setId("nameLabel");
		nameLabel.setValue(MessageFactory.getMessage(
				"admin_status_table_name_title", "").getDetail()
				+ ":");
		htmlPanelGrid.getChildren().add(nameLabel);

		InputTextarea nameValue = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		nameValue.setId("nameValue");
		nameValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.name}", String.class));
		nameValue.setReadonly(true);
		nameValue.setDisabled(true);
		htmlPanelGrid.getChildren().add(nameValue);

		HtmlOutputText descriptionLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		descriptionLabel.setId("descriptionLabel");
		descriptionLabel.setValue("Description:");
		htmlPanelGrid.getChildren().add(descriptionLabel);

		InputTextarea descriptionValue = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		descriptionValue.setId("descriptionValue");
		descriptionValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.description}", String.class));
		descriptionValue.setReadonly(true);
		descriptionValue.setDisabled(true);
		htmlPanelGrid.getChildren().add(descriptionValue);

		HtmlOutputText creationDateLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		creationDateLabel.setId("creationDateLabel");
		creationDateLabel.setValue("Creation Date:");
		htmlPanelGrid.getChildren().add(creationDateLabel);

		HtmlOutputText creationDateValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		creationDateValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.creationDate}", Date.class));
		DateTimeConverter creationDateValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		creationDateValueConverter.setPattern("dd/MM/yyyy");
		creationDateValue.setConverter(creationDateValueConverter);
		htmlPanelGrid.getChildren().add(creationDateValue);

		HtmlOutputText createdByLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		createdByLabel.setId("createdByLabel");
		createdByLabel.setValue("Created By:");
		htmlPanelGrid.getChildren().add(createdByLabel);

		HtmlOutputText createdByValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		createdByValue
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.createdBy}",
								JJContact.class));
		createdByValue.setConverter(new JJContactConverter());
		htmlPanelGrid.getChildren().add(createdByValue);

		HtmlOutputText updatedDateLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedDateLabel.setId("updatedStatDateLabel");
		updatedDateLabel.setValue("Updated Date:");
		htmlPanelGrid.getChildren().add(updatedDateLabel);

		HtmlOutputText updatedDateValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedDateValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.updatedDate}", Date.class));
		DateTimeConverter updatedDateValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		updatedDateValueConverter.setPattern("dd/MM/yyyy");
		updatedDateValue.setConverter(updatedDateValueConverter);
		htmlPanelGrid.getChildren().add(updatedDateValue);

		HtmlOutputText updatedByLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedByLabel.setId("updatedStatByLabel");
		updatedByLabel.setValue("Updated By:");
		htmlPanelGrid.getChildren().add(updatedByLabel);

		HtmlOutputText updatedByValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedByValue
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.updatedBy}",
								JJContact.class));
		updatedByValue.setConverter(new JJContactConverter());
		htmlPanelGrid.getChildren().add(updatedByValue);

		HtmlOutputText enabledLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		enabledLabel.setId("enabledStatLabel");
		enabledLabel.setValue("Enabled:");
		htmlPanelGrid.getChildren().add(enabledLabel);

		HtmlOutputText enabledValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		enabledValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.enabled}", String.class));
		htmlPanelGrid.getChildren().add(enabledValue);

		HtmlOutputText objetLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		objetLabel.setId("objetLabel");
		objetLabel.setValue("Objet:");
		htmlPanelGrid.getChildren().add(objetLabel);

		HtmlOutputText objetValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		objetValue.setId("objetValue");
		objetValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.objet}", String.class));
		htmlPanelGrid.getChildren().add(objetValue);

		HtmlOutputText levelStatusLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		levelStatusLabel.setId("levelStatusLabel");
		levelStatusLabel.setValue("Level Status:");
		htmlPanelGrid.getChildren().add(levelStatusLabel);

		HtmlOutputText levelStatusValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		levelStatusValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.levelStatus}", String.class));
		htmlPanelGrid.getChildren().add(levelStatusValue);

		return htmlPanelGrid;
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
						&& !jJRequirementBean.getTableDataModelList().isEmpty()) {

					for (int i = 0; i < jJRequirementBean
							.getTableDataModelList().size(); i++) {
						if (jJRequirementBean.getTableDataModelList().get(i)
								.getCategoryId() != 0
								&& category.getId().equals(
										jJRequirementBean
												.getTableDataModelList().get(i)
												.getCategoryId())) {
							if (jJRequirementBean.getTableDataModelList()
									.get(i).getCoverageProgress() == -1)
								jJRequirementBean.getTableDataModelList()
										.get(i).calculCoverageProgress();
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
							.getRequirements(((LoginBean) LoginBean
									.findBean("loginBean")).getContact()
									.getCompany(), category, loginBean
									.getAuthorizedMap("Requirement", project,
											LoginBean.getProduct()), LoginBean
									.getVersion(), null, null, false, true,
									false, false, null);

					boolean sizeIsOne = false;

					if (jJCategoryService.isLowLevel(category,
							LoginBean.getCompany())) {

						for (JJRequirement requirement : dataList) {
							if (jJRequirementService.haveLinkUp(requirement))
								compteur++;
						}

						sizeIsOne = true;
					} else if (jJCategoryService.isHighLevel(category,
							LoginBean.getCompany()) && !sizeIsOne) {

						for (JJRequirement requirement : dataList) {
							boolean linkUp = false;
							boolean linkDown = false;
							linkDown = jJRequirementService
									.haveLinkDown(requirement);
							linkUp = jJTaskService.haveTask(requirement, true,
									false, false);

							if (linkUp && linkDown) {
								compteur++;
							} else if (linkUp || linkDown) {
								compteur += 0.5;
							}

						}
					} else {

						for (JJRequirement requirement : dataList) {

							boolean linkUp = false;
							boolean linkDown = false;

							linkUp = jJRequirementService
									.haveLinkUp(requirement);
							linkDown = jJRequirementService
									.haveLinkDown(requirement);

							if (linkUp && linkDown) {
								compteur++;
							} else if (linkUp || linkDown) {
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
						&& !jJRequirementBean.getTableDataModelList().isEmpty()) {

					for (int i = 0; i < jJRequirementBean
							.getTableDataModelList().size(); i++) {
						if (jJRequirementBean.getTableDataModelList().get(i)
								.getCategoryId() != 0
								&& category.getId().equals(
										jJRequirementBean
												.getTableDataModelList().get(i)
												.getCategoryId())) {
							if (jJRequirementBean.getTableDataModelList()
									.get(i).getCompletionProgress() == -1)
								jJRequirementBean.getTableDataModelList()
										.get(i).calculCompletionProgress();
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
							.getRequirements(((LoginBean) LoginBean
									.findBean("loginBean")).getContact()
									.getCompany(), category, loginBean
									.getAuthorizedMap("Requirement", project,
											LoginBean.getProduct()), LoginBean
									.getVersion(), null, null, false, true,
									false, false, null);

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

			if (jJTaskService.haveTask(requirement, true, true, false)) {
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

			return (obj instanceof CategoryDataModel)
					&& (getCategory() != null) ? getCategory().equals(
					((CategoryDataModel) obj).getCategory()) : (obj == this);
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

}
