package com.starit.janjoonweb.ui.mb;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJTask;

import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJTask.class, beanName = "jJTaskBean")
public class JJTaskBean {

	private TimelineModel model;
	private Date start;
	private Date end;

	private JJProject project;

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
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public void loadData(JJProjectBean jJProjectBean) {
		
		project = jJProjectBean.getProject();
		
		// set initial start / end dates for the axis of the timeline
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Date now = new Date();

		cal.setTimeInMillis(now.getTime() - 4 * 60 * 60 * 1000);
		start = cal.getTime();

		cal.setTimeInMillis(now.getTime() + 8 * 60 * 60 * 1000);
		end = cal.getTime();

		// groups
		String[] NAMES = new String[] { "Thomas", "Oleg", "Nilesh", "Mauricio",
				"Pavol", "Sudheer" };

		// create timeline model
		model = new TimelineModel();

		for (String name : NAMES) {
			now = new Date();
			Date end = new Date(now.getTime() - 12 * 60 * 60 * 1000);

			for (int i = 0; i < 5; i++) {
				Date start = new Date(end.getTime()
						+ Math.round(Math.random() * 5) * 60 * 60 * 1000);
				end = new Date(start.getTime()
						+ Math.round(4 + Math.random() * 5) * 60 * 60 * 1000);

				long r = Math.round(Math.random() * 2);
				String availability = (r == 0 ? "Unavailable"
						: (r == 1 ? "Available" : "Maybe"));

				// create an event with content, start / end dates, editable
				// flag, group name and custom style class
				TimelineEvent event = new TimelineEvent(availability, start,
						end, true, name, availability.toLowerCase());
				model.add(event);
			}
		}
	}

}
