package com.starit.janjoonweb.ui.mb.util;

import java.util.Date;
import java.util.List;

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.ui.mb.JJSprintBean;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class PlanningConfiguration {

	static String SCRUM = "newScrum.xhtml";
	static String GANTT = "gantt.xhtml";

	private JJConfigurationService jJConfigurationService;
	private JJConfiguration planingTabsConf;
	private boolean renderScrum;
	private boolean renderGantt;
	private boolean renderKanban;
	private boolean render;
	private Integer kanban_Tab;
	private String firstPage;
	private String secondPage;
	private String firstPageHeader;
	private String secondPageHeader;

	public PlanningConfiguration(
			JJConfigurationService jJConfigurationService) {

		this.jJConfigurationService = jJConfigurationService;

		renderScrum = getPlaningTabsConf().getVal().toLowerCase()
				.contains("scrum".toLowerCase());

		renderGantt = getPlaningTabsConf().getVal().toLowerCase()
				.contains("gantt".toLowerCase());

		renderKanban = getPlaningTabsConf().getVal().toLowerCase()
				.contains("kanban".toLowerCase());

		kanban_Tab = 1;

		if (!renderScrum) {
			render = false;
			firstPage = GANTT;
			firstPageHeader = MessageFactory
					.getMessage("project_gantt_menuitem", "").getDetail();
			secondPage = SCRUM;
			secondPageHeader = MessageFactory
					.getMessage("project_scrum_menuitem", "").getDetail();

		} else if (!renderGantt) {
			render = false;
			firstPage = SCRUM;
			firstPageHeader = MessageFactory
					.getMessage("project_scrum_menuitem", "").getDetail();
			secondPage = GANTT;
			secondPageHeader = MessageFactory
					.getMessage("project_gantt_menuitem", "").getDetail();
		} else {
			render = true;
			kanban_Tab = 2;
			int scrumIndex = getPlaningTabsConf().getVal().toLowerCase()
					.indexOf("scrum".toLowerCase());
			int ganttIndex = getPlaningTabsConf().getVal().toLowerCase()
					.indexOf("gantt".toLowerCase());

			if (scrumIndex < ganttIndex) {
				firstPage = SCRUM;
				firstPageHeader = MessageFactory
						.getMessage("project_scrum_menuitem", "").getDetail();
				secondPage = GANTT;
				secondPageHeader = MessageFactory
						.getMessage("project_gantt_menuitem", "").getDetail();
			} else {
				firstPage = GANTT;
				firstPageHeader = MessageFactory
						.getMessage("project_gantt_menuitem", "").getDetail();
				secondPage = SCRUM;
				secondPageHeader = MessageFactory
						.getMessage("project_scrum_menuitem", "").getDetail();

			}
		}

		if (!renderGantt && LoginBean.findBean("jJSprintBean") != null)
			((JJSprintBean) LoginBean.findBean("jJSprintBean"))
					.setActiveTabGantIndex(0);

		if (!renderScrum && LoginBean.findBean("jJSprintBean") != null)
			((JJSprintBean) LoginBean.findBean("jJSprintBean"))
					.setActiveTabGantIndex(0);

	}

	public static int getSrumIndex() {
		PlanningConfiguration configuration = ((LoginBean) LoginBean
				.findBean("loginBean")).getPlanningConfiguration();
		if (configuration.getFirstPage().contains("scrum"))
			return 0;
		else
			return 1;
	}

	public static int getGanttIndex() {
		PlanningConfiguration configuration = ((LoginBean) LoginBean
				.findBean("loginBean")).getPlanningConfiguration();
		if (configuration.getFirstPage().contains("gantt"))
			return 0;
		else
			return 1;
	}

	public boolean isRenderScrum() {
		return renderScrum;
	}

	public void setRenderScrum(boolean renderScrum) {
		this.renderScrum = renderScrum;
	}

	public boolean isRenderGantt() {
		return renderGantt;
	}

	public void setRenderGantt(boolean renderGantt) {
		this.renderGantt = renderGantt;
	}

	public boolean isRenderKanban() {
		return renderKanban;
	}

	public void setRenderKanban(boolean renderKanban) {
		this.renderKanban = renderKanban;
	}

	public boolean isRender() {
		return render;
	}

	public void setRender(boolean render) {
		this.render = render;
	}

	public Integer getKanban_Tab() {
		return kanban_Tab;
	}

	public void setKanban_Tab(Integer kanban_Tab) {
		this.kanban_Tab = kanban_Tab;
	}

	public String getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(String firstPage) {
		this.firstPage = firstPage;
	}

	public String getSecondPage() {
		return secondPage;
	}

	public void setSecondPage(String secondPage) {
		this.secondPage = secondPage;
	}

	public String getFirstPageHeader() {
		return firstPageHeader;
	}

	public void setFirstPageHeader(String firstPageHeader) {
		this.firstPageHeader = firstPageHeader;
	}

	public String getSecondPageHeader() {
		return secondPageHeader;
	}

	public void setSecondPageHeader(String secondPageHeader) {
		this.secondPageHeader = secondPageHeader;
	}

	public void setPlaningTabsConf(JJConfiguration planingTabsConf) {
		this.planingTabsConf = planingTabsConf;
	}

	public JJConfiguration getPlaningTabsConf() {
		if (planingTabsConf == null) {
			List<JJConfiguration> conf = jJConfigurationService
					.getConfigurations("planning", "project.type", true);
			if (conf != null && !conf.isEmpty())
				planingTabsConf = conf.get(0);
			else {
				JJConfiguration configuration = new JJConfiguration();
				configuration.setName("planning");
				configuration.setDescription(
						"specify available tab in planning vue");
				configuration.setCreatedBy(
						((LoginBean) LoginBean.findBean("loginBean"))
								.getContact());
				configuration.setCreationDate(new Date());
				configuration.setEnabled(true);
				configuration.setParam("project.type");
				configuration.setVal("gantt,scrum");
				jJConfigurationService.saveJJConfiguration(configuration);
				planingTabsConf = jJConfigurationService
						.getConfigurations("planning", "project.type", true)
						.get(0);
			}

		}
		return planingTabsConf;
	}

}
