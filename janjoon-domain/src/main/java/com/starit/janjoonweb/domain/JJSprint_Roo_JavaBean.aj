// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTask;
import java.util.Date;
import java.util.Set;

privileged aspect JJSprint_Roo_JavaBean{

public String JJSprint.getName(){return this.name;}

public void JJSprint.setName(String name){this.name=name;}

public String JJSprint.getDescription(){return this.description;}

public void JJSprint.setDescription(String description){this.description=description;}

public Date JJSprint.getCreationDate(){return this.creationDate;}

public void JJSprint.setCreationDate(Date creationDate){this.creationDate=creationDate;}

public JJContact JJSprint.getCreatedBy(){return this.createdBy;}

public void JJSprint.setCreatedBy(JJContact createdBy){this.createdBy=createdBy;}

public Date JJSprint.getUpdatedDate(){return this.updatedDate;}

public void JJSprint.setUpdatedDate(Date updatedDate){this.updatedDate=updatedDate;}

public JJContact JJSprint.getUpdatedBy(){return this.updatedBy;}

public void JJSprint.setUpdatedBy(JJContact updatedBy){this.updatedBy=updatedBy;}

public Boolean JJSprint.getEnabled(){return this.enabled;}

public void JJSprint.setEnabled(Boolean enabled){this.enabled=enabled;}

public Integer JJSprint.getOrdering(){return this.ordering;}

public void JJSprint.setOrdering(Integer ordering){this.ordering=ordering;}

public Date JJSprint.getStartDate(){return this.startDate;}

public void JJSprint.setStartDate(Date startDate){this.startDate=startDate;}

public Date JJSprint.getEndDate(){return this.endDate;}

public void JJSprint.setEndDate(Date endDate){this.endDate=endDate;}

public JJProject JJSprint.getProject(){return this.project;}

public void JJSprint.setProject(JJProject project){this.project=project;}

public Set<JJBuild>JJSprint.getBuilds(){return this.builds;}

public void JJSprint.setBuilds(Set<JJBuild>builds){this.builds=builds;}

public Set<JJTask>JJSprint.getObstacles(){return this.obstacles;}

public void JJSprint.setObstacles(Set<JJTask>obstacles){this.obstacles=obstacles;}

public void JJSprint.setContacts(Set<JJContact>contacts){this.contacts=contacts;}

public Set<JJMessage>JJSprint.getMessages(){return this.messages;}

public void JJSprint.setMessages(Set<JJMessage>messages){this.messages=messages;}

}
