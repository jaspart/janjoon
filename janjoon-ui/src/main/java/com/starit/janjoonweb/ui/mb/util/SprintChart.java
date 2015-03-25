package com.starit.janjoonweb.ui.mb.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartSeries;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class SprintChart {

	private CartesianChartModel chartModel;
	private JJSprint sprint;

	public SprintChart(JJSprint sprint, List<JJTask> tasks) {
		this.chartModel = new CartesianChartModel();
		this.sprint = sprint;
		if (tasks != null)
			initChartModel(tasks);
	}

	private void initChartModel(List<JJTask> tasks) {

		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		int workload = 0;
		int diff = 0;

		for (JJTask task : tasks) {
			if (task.getWorkloadPlanned() != null)
				workload = workload + task.getWorkloadPlanned();
		}

		diff = workload;
		List<JJTask> removedTasks=new ArrayList<JJTask>();
		ChartSeries chartSeries = new ChartSeries();
		LineChartSeries lineSeries = new LineChartSeries();
		ContactCalendarUtil calendar = new ContactCalendarUtil(
				((LoginBean) LoginBean.findBean("loginBean")).getContact()
						.getCompany());

		float dayWorkload = workload
				* 1.0f
				/ (calendar.getNumberOfWorkingDay(sprint.getStartDate(),
						sprint.getEndDate()) - 1);
		chartSeries.set(f.format(sprint.getStartDate()), workload);
		lineSeries.set(f.format(sprint.getStartDate()), workload);
		chartSeries.setLabel(sprint.getName() + " Workload");
		lineSeries.setLabel("BurnDown Ideal");

		Date staDate = sprint.getStartDate();
		
		for (JJTask task : tasks) {
			if (task.getEndDateReal() != null) {
				if ((f.format(task.getEndDateReal()).equalsIgnoreCase(
						f.format(staDate)) || (task.getEndDateReal()
						.before(staDate))) && task.getWorkloadPlanned() != null) {
					workload = workload - task.getWorkloadPlanned();
					removedTasks.add(task);
				}
			}
		}
		

		chartSeries.set(f.format(staDate), workload);
		lineSeries.set(f.format(staDate), diff);
		staDate = CalendarUtil.getAfterDay(staDate);
		Date currentTime = new GregorianCalendar().getTime();
		tasks.removeAll(removedTasks);
		removedTasks=new ArrayList<JJTask>();

		while (staDate.before(CalendarUtil.getAfterDay(sprint.getEndDate()))) {
			if (staDate.before(CalendarUtil.getAfterDay(currentTime))) {
				for (JJTask task : tasks) {
					if (task.getEndDateReal() != null) {
						if (f.format(task.getEndDateReal()).equalsIgnoreCase(
								f.format(staDate))
								&& task.getWorkloadPlanned() != null) {
							workload = workload - task.getWorkloadPlanned();
							removedTasks.add(task);
							
						}
					}

				}
				workload = Math.max(workload, 0);
				chartSeries.set(f.format(staDate), workload);
			} else {
				chartSeries.set(f.format(staDate), 0);
			}
			if (!calendar.isHoliday(staDate) && !calendar.isWeekEnd(staDate)) {
				diff = Math.round(diff - dayWorkload);
			}
			diff = Math.max(diff, 0);
			lineSeries.set(f.format(staDate), diff);
			staDate = CalendarUtil.getAfterDay(staDate);
			tasks.removeAll(removedTasks);
			removedTasks=new ArrayList<JJTask>();
			
		}

		staDate = sprint.getEndDate();	

		if (staDate.before(CalendarUtil.getAfterDay(currentTime))) {
			for (JJTask task : tasks) {
				if (task.getStatus() != null
						&& task.getWorkloadPlanned() != null
						&& task.getStatus().getName().equalsIgnoreCase("DONE")) {
					workload = workload - task.getWorkloadPlanned();
					}

			}
			workload = Math.max(workload, 0);
			chartSeries.set(f.format(staDate), workload);
		}

		chartModel.addSeries(chartSeries);
		chartModel.addSeries(lineSeries);
		DateAxis axis = new DateAxis("Dates");
		axis.setTickAngle(-50);
		chartModel.getAxes().put(AxisType.X, axis);
		chartModel.getAxis(AxisType.Y).setMin(0);
	}

	public CartesianChartModel getChartModel() {
		return chartModel;
	}

	public void setChartModel(CartesianChartModel chartModel) {
		this.chartModel = chartModel;
	}

	public JJSprint getSprint() {
		return sprint;
	}

	public void setSprint(JJSprint sprint) {
		this.sprint = sprint;
	}

	public static List<SprintChart> generateSprintChartList(
			List<JJSprint> sprints, JJTaskService jJTaskService) {
		List<SprintChart> sprintUtils = null;
		if (!sprints.isEmpty()) {
			sprintUtils = new ArrayList<SprintChart>();
			for (JJSprint s : sprints) {
				SprintChart ss = new SprintChart(s,
						jJTaskService.getSprintTasks(s, LoginBean.getProduct()));
				sprintUtils.add(ss);
			}
		}
		return sprintUtils;
	}

}
