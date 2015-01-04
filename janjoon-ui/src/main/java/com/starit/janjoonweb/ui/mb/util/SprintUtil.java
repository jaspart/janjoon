package com.starit.janjoonweb.ui.mb.util;

import java.util.ArrayList;
import java.util.List;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;

import com.starit.janjoonweb.ui.mb.util.SprintChart;

public class SprintUtil {

	JJSprint sprint;
	private boolean neditabale;
	private List<JJTask> todoTask;
	private List<JJTask> doneTask;
	private List<JJTask> progressTask;
	private List<JJContact> contacts;
	
	Integer consumed;
	Integer workload;
	private boolean render;
	
	private SprintChart chart;
	
	public SprintUtil(JJSprint sprint, List<JJTask> tasks) {
		this.sprint = sprint;
		this.neditabale=false;
		this.chart = new SprintChart(sprint, tasks);
		calculateField(tasks);
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
	
	public void setChart(JJSprint sprint, List<JJTask> tasks){
		this.chart = new SprintChart(sprint, tasks);
	}
	
	public SprintChart getChart(){
		return this.chart;
	}
	
	private void calculateField(List<JJTask> tasks) {
		Integer i = 0;
		Integer j = 0;
		this.render = (sprint.getName() == null);
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

	public static List<SprintUtil> generateSprintUtilList(
			List<JJSprint> sprints, JJTaskService jJTaskService) {
		List<SprintUtil> sprintUtils = null;
		if (!sprints.isEmpty()) {
			sprintUtils = new ArrayList<SprintUtil>();
			for (JJSprint s : sprints) {
				SprintUtil ss = new SprintUtil(s,
						jJTaskService.getSprintTasks(s));
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
