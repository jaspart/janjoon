package com.starit.janjoonweb.ui.mb.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartSeries;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class SprintUtil {

	JJSprint sprint;
	private boolean neditabale;
	private CartesianChartModel chartModel;
	private List<JJTask> todoTask;
	private List<JJTask> doneTask;
	private List<JJTask> progressTask;
	private List<JJContact> contacts;
	
	Integer consumed;
	Integer workload;
	private boolean render;
	
	public SprintUtil(JJSprint sprint, List<JJTask> tasks) {
		this.sprint = sprint;
		this.neditabale=false;	
		this.chartModel = new CartesianChartModel();
		calculateField(tasks);		
		if (tasks != null && !tasks.isEmpty())
			initChartModel(tasks);
		
	}

	public boolean isNeditabale() {
		return neditabale;
	}

	public void setNeditabale(boolean neditabale) {
		this.neditabale = neditabale;
	}

	public SprintUtil() {

	}

	public JJSprint getSprint() {
		return sprint;
	}

	public void setSprint(JJSprint sprint) {
		this.sprint = sprint;
	}

	public List<JJTask> getTodoTask() {
		return todoTask;
	}

	public void setTodoTask(List<JJTask> todoTask) {
		this.todoTask = todoTask;
	}

	public List<JJTask> getDoneTask() {
		return doneTask;
	}

	public void setDoneTask(List<JJTask> doneTask) {
		this.doneTask = doneTask;
	}

	public List<JJTask> getProgressTask() {
		return progressTask;
	}

	public void setProgressTask(List<JJTask> progressTask) {
		this.progressTask = progressTask;
	}
	
	public List<JJContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<JJContact> contacts) {
		this.contacts = contacts;
	}

	public Integer getConsumed() {
		return consumed;
	}

	public void setConsumed(Integer consumed) {
		this.consumed = consumed;
	}

	public Integer getWorkload() {
		return workload;
	}

	public void setWorkload(Integer workload) {
		this.workload = workload;
	}

	public boolean isRender() {
		return render;
	}

	public void setRender(boolean render) {
		this.render = render;
	}
	
	public CartesianChartModel getChartModel() {
		return chartModel;
	}

	public void setChartModel(CartesianChartModel chartModel) {
		this.chartModel = chartModel;
	}
	
	private void calculateField(List<JJTask> tasks) {
		Integer i = 0;
		Integer j = 0;
		this.render = (sprint.getId() == null);
		if (!render) {
			
			contacts = new ArrayList<JJContact>(sprint.getContacts());

			if (tasks != null) {
				if (!tasks.isEmpty()) {

					progressTask = new ArrayList<JJTask>();
					todoTask = new ArrayList<JJTask>();
					doneTask = new ArrayList<JJTask>();
					

					for (JJTask task : tasks) {
						
						if (task.getWorkloadPlanned() != null)
							i = i + task.getWorkloadPlanned();

						if (task.getWorkloadReal() != null)
							j = j + task.getWorkloadReal();

						if (task.getStatus() != null) {
							if (task.getStatus().getName()
									.equalsIgnoreCase("DONE"))
								doneTask.add(task);
							else {
								if (task.getStatus().getName()
										.equalsIgnoreCase("IN PROGRESS"))
									progressTask.add(task);
								else
									todoTask.add(task);
							}
						} else {
							todoTask.add(task);
						}
					}
				}
			}
		}
		this.consumed = j;
		this.workload = i;

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
			if(f.format(staDate).equalsIgnoreCase(f.format(sprint.getEndDate())))
			{
				lineSeries.set(f.format(staDate), 0);
				
			}else
			{
				if (!calendar.isHoliday(staDate) && !calendar.isWeekEnd(staDate)) {
					diff = Math.round(diff - dayWorkload);
				}
				diff = Math.max(diff, 0);
				lineSeries.set(f.format(staDate), diff);
			}			
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
	

	public static List<SprintUtil> generateSprintUtilList(
			List<JJSprint> sprints, JJTaskService jJTaskService) {
		List<SprintUtil> sprintUtils = null;
		if (!sprints.isEmpty()) {
			sprintUtils = new ArrayList<SprintUtil>();
			for (JJSprint s : sprints) {
				SprintUtil ss = new SprintUtil(s,
						jJTaskService.getSprintTasks(s,LoginBean.getProduct()));
				sprintUtils.add(ss);
			}
		}
		return sprintUtils;
	}

	public static SprintUtil getSprintUtil(Long id, List<SprintUtil> list) {
		SprintUtil s = null;
		for (SprintUtil l : list) {
			if (l.getSprint().getId() == id) {
				s = l;
				break;
			}
		}
		return s;
	}
	
}
