package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTask.class })
public interface JJTaskService {

	public List<JJTask> getTasks(JJSprint sprint, JJProject project,
			JJProduct product, JJContact contact, JJChapter chapter,
			boolean nullChapter, JJRequirement requirement,JJStatus status,
			JJTestcase testcase, JJBuild build, boolean onlyActif,
			boolean sortedByCreationDate, boolean withBuild, String objet);

	public List<JJTask> getSuperimposeTasks(JJContact assignedTo,
			Date startDate, Date endDate, JJTask task);

	public List<JJTask> getTasks(JJProject project, JJProduct product,
			JJContact assignedTo, Date startDate, Date endDate);

	public List<JJTask> getImportTasks(JJBug bug, JJRequirement requirement,
			JJTestcase testcase, boolean onlyActif);

	public boolean haveTask(Object object, boolean onlyActif, boolean finished,
			boolean checkLinkUp);

	public void saveTasks(Set<JJTask> tasks);

	public void updateTasks(Set<JJTask> tasks);

	public List<JJTask> getTasksByStatus(JJStatus status, JJProject project,
			JJSprint sprint, boolean onlyActif);

	public List<JJTask> getSprintTasks(JJSprint sprint, JJProduct product);

	public List<JJTask> getTasksByProduct(JJProduct product, JJProject project);

	public List<JJTask> getToDoTasks(JJContact contact);

	public List<JJTask> getTasksByVersion(JJVersion jJversion);
	
	public List<JJTask> getTasksByObject(JJSprint sprint,JJProject project,JJProduct product,String object,boolean done);

}
