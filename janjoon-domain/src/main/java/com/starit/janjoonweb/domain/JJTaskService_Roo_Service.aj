// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import java.util.List;

privileged aspect JJTaskService_Roo_Service{

public abstract long JJTaskService.countAllJJTasks();public abstract void JJTaskService.deleteJJTask(JJTask JJTask_);public abstract JJTask JJTaskService.findJJTask(Long id);public abstract List<JJTask>JJTaskService.findAllJJTasks();public abstract List<JJTask>JJTaskService.findJJTaskEntries(int firstResult,int maxResults);public abstract void JJTaskService.saveJJTask(JJTask JJTask_);public abstract JJTask JJTaskService.updateJJTask(JJTask JJTask_);}
