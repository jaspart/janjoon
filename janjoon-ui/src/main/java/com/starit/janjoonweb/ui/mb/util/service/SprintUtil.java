package com.starit.janjoonweb.ui.mb.util.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;

public class SprintUtil {

	JJSprint sprint;
	private List<JJTask> todoTask;
	private List<JJTask> doneTask;
	private List<JJTask> progressTask;
	private List<JJTask> taskList;
	Integer consumed;
	Integer workload;
	private boolean render;
	private boolean renderTaskForm;

	public SprintUtil(JJSprint sprint, List<JJTask> tasks) {
		this.sprint = sprint;
		renderTaskForm = false;
		calculateField(tasks);
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

	public List<JJTask> getTaskList() {
		
			if (taskList == null) {
				taskList = new ArrayList<JJTask>();
				for (JJTask t : sprint.getTasks()) {
					if (t.getEnabled())
					taskList.add(t);
				}
			}		
		
		return taskList;
	}

	public void setTaskList(List<JJTask> tasks) {
		this.taskList = tasks;
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

	private void calculateField(List<JJTask> tasks) {

		Integer i = 0;
		Integer j = 0;
		this.render = (sprint.getName() == null);
		if (!render) {
			if (tasks != null) {
				if (!tasks.isEmpty()) {
					progressTask = new ArrayList<JJTask>();
					todoTask = new ArrayList<JJTask>();
					doneTask = new ArrayList<JJTask>();

					for (JJTask task : tasks) {

						System.out.println(task.getSprint().getName());
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

	public boolean isRenderTaskForm() {
		return renderTaskForm;
	}

	public void setRenderTaskForm(boolean renderTaskForm) {
		this.renderTaskForm = renderTaskForm;
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
