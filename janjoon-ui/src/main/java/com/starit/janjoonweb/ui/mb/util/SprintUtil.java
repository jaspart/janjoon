package com.starit.janjoonweb.ui.mb.util;

import java.text.*;
import java.util.*;

import org.primefaces.model.chart.*;

import com.starit.janjoonweb.domain.*;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class SprintUtil {

	JJSprint sprint;
	private boolean neditabale;
	private JJContactService jJContactService;
	private CartesianChartModel chartModel;
	private List<JJTask> todoTask;
	private List<JJTask> doneTask;
	private List<JJTask> progressTask;
	private List<JJContact> contacts;
	private boolean disableDragDrop;
	private boolean enableDelete;

	private Integer consumed;
	private Integer workload;
	private boolean render;

	private Integer priseReal;
	private Integer priseSold;

	public SprintUtil(JJSprint sprint, List<JJTask> tasks,
			JJContactService jJContactService, JJTaskService jjTaskService) {
		this.sprint = sprint;
		this.jJContactService = jJContactService;
		this.neditabale = false;
		this.chartModel = new BarChartModel();
		this.disableDragDrop = true;
		this.render = (this.sprint.getId() == null);
		this.enableDelete = sprint.getId() != null
				&& !jjTaskService.haveTask(sprint, true, false, false);
		calculateField(tasks);
		if (tasks != null && !tasks.isEmpty())
			initChartModel(tasks);
		else
			this.chartModel = null;

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

	public boolean isDisableDragDrop() {
		return disableDragDrop;
	}

	public void setDisableDragDrop(boolean disableDragDrop) {
		this.disableDragDrop = disableDragDrop;
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

	public Integer getProgressBarValue() {
		if (workload == 0)
			return 0;
		else {
			return Math.round(100 * consumed / workload);
		}
	}

	public Integer getPriseReal() {

		if (doneTask != null && priseReal == null) {
			priseReal = 0;

			for (JJTask task : doneTask) {

				if (task.getAssignedTo() != null
						&& task.getWorkloadReal() != null
						&& task.getAssignedTo().getPriceReal() != null) {
					priseReal = priseReal
							+ task.getWorkloadReal() * jJContactService
									.findJJContact(task.getAssignedTo().getId())
									.getPriceReal();
				}
			}
		}
		return priseReal;

	}

	public Integer getPriseSold() {

		if (doneTask != null && priseSold == null) {
			priseSold = 0;
			for (JJTask task : doneTask) {
				if (task.getAssignedTo() != null
						&& task.getWorkloadReal() != null
						&& task.getAssignedTo().getPriceSold() != null) {
					priseSold = priseSold
							+ task.getWorkloadReal() * jJContactService
									.findJJContact(task.getAssignedTo().getId())
									.getPriceSold();
				}
			}
		}
		return priseSold;

	}

	public void setPriseReal(Integer priseReal) {
		this.priseReal = priseReal;
	}

	public void setPriseSold(Integer priseSold) {
		this.priseSold = priseSold;
	}

	public boolean isRender() {
		return render;
	}

	public void setRender(boolean render) {
		this.render = render;
	}

	public boolean isEnableDelete() {
		return enableDelete;
	}

	public void setEnableDelete(boolean enableDelete) {
		this.enableDelete = enableDelete;
	}

	public CartesianChartModel getChartModel() {
		return chartModel;
	}

	public String getContactsLabel() {

		if (!render) {
			if (contacts == null || contacts.isEmpty()) {
				return MessageFactory.getMessage("label_select", "")
						.getDetail();
			} else
				return contacts.size() + " Contact";
		} else {
			if (sprint.getContacts() == null
					|| sprint.getContacts().isEmpty()) {
				return MessageFactory.getMessage("label_select", "")
						.getDetail();
			} else
				return sprint.getContacts().size() + " Contact";
		}

	}

	private void calculateField(List<JJTask> tasks) {
		Integer i = 0;
		Integer j = 0;

		this.render = (sprint.getId() == null);
		if (!render) {

			contacts = new ArrayList<JJContact>(sprint.getContacts());
			disableDragDrop = !contacts.contains(
					((LoginBean) LoginBean.findBean("loginBean")).getContact());

			if (tasks != null) {
				if (!tasks.isEmpty()) {

					progressTask = new ArrayList<JJTask>();
					todoTask = new ArrayList<JJTask>();
					doneTask = new ArrayList<JJTask>();

					for (JJTask task : tasks) {

						if (task.getWorkloadPlanned() != null)
							i = i + task.getWorkloadPlanned();

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

					for (JJTask task : doneTask) {
						if (task.getWorkloadPlanned() != null)
							j = j + task.getWorkloadPlanned();
						else
							j++;
					}

					// sort
					Collections.sort(todoTask, new Comparator<JJTask>() {

						@Override
						public int compare(JJTask o1, JJTask o2) {
							return o2.getCreationDate()
									.compareTo(o1.getCreationDate());
						}
					});
					
					Collections.sort(progressTask, new Comparator<JJTask>() {

						@Override
						public int compare(JJTask o1, JJTask o2) {
							return o2.getStartDateReal()
									.compareTo(o1.getStartDateReal());
						}
					});
					
					Collections.sort(doneTask, new Comparator<JJTask>() {

						@Override
						public int compare(JJTask o1, JJTask o2) {
							return o2.getEndDateReal()
									.compareTo(o1.getEndDateReal());
						}
					});
					
					
				}
			}
		}
		this.consumed = j;
		this.workload = i;

	}

	private void initChartModel(List<JJTask> tasks) {

		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		int workload = this.workload;
		float diff = 0;

		diff = workload;
		List<JJTask> removedTasks = new ArrayList<JJTask>();
		BarChartSeries chartSeries = new BarChartSeries();
		LineChartSeries lineSeries = new LineChartSeries();
		ContactCalendarUtil calendar = new ContactCalendarUtil(
				LoginBean.getCompany());

		float dayWorkload = workload * 1.0f
				/ (calendar.getNumberOfWorkingDay(sprint.getStartDate(),
						sprint.getEndDate()) - 1);
		chartSeries.set(f.format(sprint.getStartDate()), workload);
		lineSeries.set(f.format(sprint.getStartDate()), workload);
		chartSeries.setLabel(sprint.getName() + " "
				+ MessageFactory
						.getMessage("specification_create_workload_label", "")
						.getDetail());
		lineSeries.setLabel("BurnDown Ideal");

		Date staDate = sprint.getStartDate();

		for (JJTask task : tasks) {
			if (task.getEndDateReal() != null) {
				if ((f.format(task.getEndDateReal())
						.equalsIgnoreCase(f.format(staDate))
						|| (task.getEndDateReal().before(staDate)))
						&& task.getWorkloadPlanned() != null) {
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
		removedTasks = new ArrayList<JJTask>();

		while (staDate.before(CalendarUtil.getAfterDay(sprint.getEndDate()))) {
			if (staDate.before(CalendarUtil.getAfterDay(currentTime))
					&& !tasks.isEmpty()) {
				for (JJTask task : tasks) {
					if (task.getEndDateReal() != null) {
						if (f.format(task.getEndDateReal())
								.equalsIgnoreCase(f.format(staDate))
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
			if (f.format(staDate)
					.equalsIgnoreCase(f.format(sprint.getEndDate()))) {
				lineSeries.set(f.format(staDate), 0);

			} else {
				if (!calendar.isHoliday(staDate)
						&& !calendar.isWeekEnd(staDate)) {
					// diff = Math.round(diff - dayWorkload);
					diff = diff - dayWorkload;
				}
				diff = Math.max(diff, 0);
				lineSeries.set(f.format(staDate), diff);
			}
			staDate = CalendarUtil.getAfterDay(staDate);
			tasks.removeAll(removedTasks);
			removedTasks = new ArrayList<JJTask>();

		}

		staDate = sprint.getEndDate();

		if (staDate.before(CalendarUtil.getAfterDay(currentTime))) {
			for (JJTask task : tasks) {
				if (task.getStatus() != null
						&& task.getWorkloadPlanned() != null && task.getStatus()
								.getName().equalsIgnoreCase("DONE")) {
					workload = workload - task.getWorkloadPlanned();
				}

			}
			workload = Math.max(workload, 0);
			chartSeries.set(f.format(staDate), workload);
		}

		chartModel.addSeries(chartSeries);
		chartModel.addSeries(lineSeries);
		chartModel.setLegendPosition("ne");
		chartModel.setMouseoverHighlight(false);
		chartModel.setShowDatatip(false);
		chartModel.setShowPointLabels(true);

		chartModel.getAxis(AxisType.X).setTickAngle(-50);
		chartModel.getAxis(AxisType.X).setTickFormat("%b %#d, %y");
		chartModel.getAxis(AxisType.X).setLabel("Date");

		chartModel.getAxis(AxisType.Y).setMin(0);
		chartModel.getAxis(AxisType.Y)
				.setLabel(MessageFactory
						.getMessage("specification_create_workload_label", "")
						.getDetail());
	}

	public static List<SprintUtil> generateSprintUtilList(
			List<JJSprint> sprints, JJTaskService jJTaskService,
			JJContactService jjContactService) {
		List<SprintUtil> sprintUtils = null;
		if (!sprints.isEmpty()) {
			sprintUtils = new ArrayList<SprintUtil>();
			for (JJSprint s : sprints) {
				SprintUtil ss = new SprintUtil(s,
						jJTaskService.getSprintTasks(s, LoginBean.getProduct()),
						jjContactService, jJTaskService);
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
