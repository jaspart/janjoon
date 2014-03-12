package com.starit.janjoonweb.ui.mb;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJTask;
import com.sun.mail.imap.protocol.Namespaces.Namespace;

import org.apache.xmlbeans.impl.config.NameSet;
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

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJContact user = (JJContact) session.getAttribute("JJContact");

		// Set initial start / end dates for the axis of the timeline
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Date now = new Date();

		// Before 4 hours for now
		cal.setTimeInMillis(now.getTime() - 4 * 60 * 60 * 1000);
		start = cal.getTime();

		// After 8 hours for now
		cal.setTimeInMillis(now.getTime() + 8 * 60 * 60 * 1000);
		end = cal.getTime();

		// Contacts

		List<JJContact> contacts = jJContactService.getContacts(null, true);

		// Create timeline model
		model = new TimelineModel();

		for (JJContact contact : contacts) {

			List<JJTask> tasks = jJTaskService.getTasks(project, contact);

			for (JJTask task : tasks) {
				Date start = task.getCreationDate();
				Date end = new Date(start.getTime() + task.getWorkloadPlanned()
						* 60 * 60 * 1000);

				model.add(new TimelineEvent(task, start, end, true, contact
						.getFirstname() + " " + contact.getName()));
			}
		}

	}

}
