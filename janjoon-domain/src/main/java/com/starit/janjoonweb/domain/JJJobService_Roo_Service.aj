// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJJob;
import com.starit.janjoonweb.domain.JJJobService;
import java.util.List;

privileged aspect JJJobService_Roo_Service{

public abstract long JJJobService.countAllJJJobs();public abstract void JJJobService.deleteJJJob(JJJob JJJob_);public abstract JJJob JJJobService.findJJJob(Long id);public abstract List<JJJob>JJJobService.findAllJJJobs();public abstract List<JJJob>JJJobService.findJJJobEntries(int firstResult,int maxResults);public abstract void JJJobService.saveJJJob(JJJob JJJob_);public abstract JJJob JJJobService.updateJJJob(JJJob JJJob_);}
