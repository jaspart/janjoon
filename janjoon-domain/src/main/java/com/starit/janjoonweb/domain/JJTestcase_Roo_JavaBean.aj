// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJHardware;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJSoftware;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTeststep;
import java.util.Date;
import java.util.Set;

privileged aspect JJTestcase_Roo_JavaBean{

public String JJTestcase.getName(){return this.name;}

public void JJTestcase.setName(String name){this.name=name;}

public String JJTestcase.getDescription(){return this.description;}

public void JJTestcase.setDescription(String description){this.description=description;}

public Date JJTestcase.getCreationDate(){return this.creationDate;}

public void JJTestcase.setCreationDate(Date creationDate){this.creationDate=creationDate;}

public JJContact JJTestcase.getCreatedBy(){return this.createdBy;}

public void JJTestcase.setCreatedBy(JJContact createdBy){this.createdBy=createdBy;}

public Date JJTestcase.getUpdatedDate(){return this.updatedDate;}

public void JJTestcase.setUpdatedDate(Date updatedDate){this.updatedDate=updatedDate;}

public JJContact JJTestcase.getUpdatedBy(){return this.updatedBy;}

public void JJTestcase.setUpdatedBy(JJContact updatedBy){this.updatedBy=updatedBy;}

public Boolean JJTestcase.getEnabled(){return this.enabled;}

public void JJTestcase.setEnabled(Boolean enabled){this.enabled=enabled;}

public Integer JJTestcase.getOrdering(){return this.ordering;}

public void JJTestcase.setOrdering(Integer ordering){this.ordering=ordering;}

public JJRequirement JJTestcase.getRequirement(){return this.requirement;}

public void JJTestcase.setRequirement(JJRequirement requirement){this.requirement=requirement;}

public Boolean JJTestcase.getAllBuilds(){return this.allBuilds;}

public void JJTestcase.setAllBuilds(Boolean allBuilds){this.allBuilds=allBuilds;}

public JJSprint JJTestcase.getSprint(){return this.sprint;}

public void JJTestcase.setSprint(JJSprint sprint){this.sprint=sprint;}

public JJStatus JJTestcase.getStatus(){return this.status;}

public void JJTestcase.setStatus(JJStatus status){this.status=status;}

public Integer JJTestcase.getWorkload(){return this.workload;}

public void JJTestcase.setWorkload(Integer workload){this.workload=workload;}

public Integer JJTestcase.getPricepoint(){return this.pricepoint;}

public void JJTestcase.setPricepoint(Integer pricepoint){this.pricepoint=pricepoint;}

public Boolean JJTestcase.getAutomatic(){return this.automatic;}

public void JJTestcase.setAutomatic(Boolean automatic){this.automatic=automatic;}

public Boolean JJTestcase.getRegression(){return this.regression;}

public void JJTestcase.setRegression(Boolean regression){this.regression=regression;}

public Set<JJBuild>JJTestcase.getBuilds(){return this.builds;}

public void JJTestcase.setBuilds(Set<JJBuild>builds){this.builds=builds;}

public Set<JJSoftware>JJTestcase.getSoftwares(){return this.softwares;}

public void JJTestcase.setSoftwares(Set<JJSoftware>softwares){this.softwares=softwares;}

public Set<JJHardware>JJTestcase.getHardwares(){return this.hardwares;}

public void JJTestcase.setHardwares(Set<JJHardware>hardwares){this.hardwares=hardwares;}

public Set<JJTeststep>JJTestcase.getTeststeps(){return this.teststeps;}

public void JJTestcase.setTeststeps(Set<JJTeststep>teststeps){this.teststeps=teststeps;}

public Set<JJTask>JJTestcase.getTasks(){return this.tasks;}

public void JJTestcase.setTasks(Set<JJTask>tasks){this.tasks=tasks;}

public Set<JJMessage>JJTestcase.getMessages(){return this.messages;}

public void JJTestcase.setMessages(Set<JJMessage>messages){this.messages=messages;}

public Set<JJContact>JJTestcase.getContacts(){return this.contacts;}

public void JJTestcase.setContacts(Set<JJContact>contacts){this.contacts=contacts;}

}
