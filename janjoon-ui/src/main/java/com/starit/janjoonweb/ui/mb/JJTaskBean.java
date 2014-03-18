package com.starit.janjoonweb.ui.mb;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJTask;

@RooSerializable
@RooJsfManagedBean(entity = JJTask.class, beanName = "jJTaskBean")
public class JJTaskBean {

	private List<JJTask> tasks;

	private TimelineModel model;
	private Date start;
	private Date end;

	private JJProject project;

	public List<JJTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<JJTask> tasks) {
		this.tasks = tasks;
	}

	public TimelineModel getModel() {
		return model;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public JJProject getProject() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProjectBean projbean = (JJProjectBean) session
				.getAttribute("jJProjectBean");
		this.project = projbean.getProject();
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public void loadData() {
		this.getProject();

		tasks = jJTaskService.getTasks(project, null, true);
		Date now = new Date();

		for (JJTask task : tasks) {
			task.setStartDatePlanned(now);
			task.setEndDatePlanned(new Date(now.getTime()
					+ task.getWorkloadPlanned() * 60 * 60 * 1000));
		}
		createTimeLineModel(now);
	}

	private void createTimeLineModel(Date now) {
		// Set initial start / end dates for the axis of the timeline
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		// Before 4 hours for now
		cal.setTimeInMillis(now.getTime() - 4 * 60 * 60 * 1000);
		start = cal.getTime();

		// After 8 hours for now
		cal.setTimeInMillis(now.getTime() + 8 * 60 * 60 * 1000);
		end = cal.getTime();

		// Create timeline model
		model = new TimelineModel();

		for (JJTask task : tasks) {
			// Date start = task.getCreationDate();
			Date end = new Date(now.getTime() + task.getWorkloadPlanned() * 60
					* 60 * 1000);

			model.add(new TimelineEvent(task, now, end, true, task.getName()));
		}

	}

}
