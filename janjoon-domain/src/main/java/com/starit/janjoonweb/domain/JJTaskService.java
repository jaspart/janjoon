package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTask.class })
public interface JJTaskService {

	public List<JJTask> getTasks(JJProject project, JJProduct product,
			JJContact contact, JJChapter chapter, boolean onlyActif,
			boolean sortedByCreationDate);

	public void saveTasks(Set<JJTask> tasks);

	public void updateTasks(Set<JJTask> tasks);
	
	public List<JJTask> getTasksByStatus(JJStatus status,JJProject project,JJSprint sprint,boolean onlyActif);
	
	public List<JJTask> getSprintTasks(JJSprint sprint);

}
