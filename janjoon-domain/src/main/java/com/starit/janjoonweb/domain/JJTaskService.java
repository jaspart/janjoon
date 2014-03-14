package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTask.class })
public interface JJTaskService {

	public List<JJTask> getTasks(JJProject project, JJContact contact,
			boolean onlyActif);

	public void saveTasks(Set<JJTask> tasks);

	public void updateTasks(Set<JJTask> tasks);

	public List<JJTask> getTasksByProduct(JJProduct product,JJProject project);
}
