// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJImportance;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.reference.JJRelationship;
import java.util.Date;
import java.util.Set;

privileged aspect JJRequirement_Roo_JavaBean{

public String JJRequirement.getName(){return this.name;}

public void JJRequirement.setName(String name){this.name=name;}

public String JJRequirement.getDescription(){return this.description;}

public void JJRequirement.setDescription(String description){this.description=description;}

public Date JJRequirement.getCreationDate(){return this.creationDate;}

public void JJRequirement.setCreationDate(Date creationDate){this.creationDate=creationDate;}

public JJContact JJRequirement.getCreatedBy(){return this.createdBy;}

public void JJRequirement.setCreatedBy(JJContact createdBy){this.createdBy=createdBy;}

public Date JJRequirement.getUpdatedDate(){return this.updatedDate;}

public void JJRequirement.setUpdatedDate(Date updatedDate){this.updatedDate=updatedDate;}

public JJContact JJRequirement.getUpdatedBy(){return this.updatedBy;}

public void JJRequirement.setUpdatedBy(JJContact updatedBy){this.updatedBy=updatedBy;}

public Boolean JJRequirement.getEnabled(){return this.enabled;}

public void JJRequirement.setEnabled(Boolean enabled){this.enabled=enabled;}

public Integer JJRequirement.getOrdering(){return this.ordering;}

public void JJRequirement.setOrdering(Integer ordering){this.ordering=ordering;}

public Integer JJRequirement.getNumero(){return this.numero;}

public void JJRequirement.setNumero(Integer numero){this.numero=numero;}

public JJProject JJRequirement.getProject(){return this.project;}

public void JJRequirement.setProject(JJProject project){this.project=project;}

public Date JJRequirement.getStartDate(){return this.startDate;}

public void JJRequirement.setStartDate(Date startDate){this.startDate=startDate;}

public Date JJRequirement.getEndDate(){return this.endDate;}

public void JJRequirement.setEndDate(Date endDate){this.endDate=endDate;}

public JJChapter JJRequirement.getChapter(){return this.chapter;}

public void JJRequirement.setChapter(JJChapter chapter){this.chapter=chapter;}

public JJVersion JJRequirement.getVersioning(){return this.versioning;}

public void JJRequirement.setVersioning(JJVersion versioning){this.versioning=versioning;}

public JJProduct JJRequirement.getProduct(){return this.product;}

public void JJRequirement.setProduct(JJProduct product){this.product=product;}

public JJCategory JJRequirement.getCategory(){return this.category;}

public void JJRequirement.setCategory(JJCategory category){this.category=category;}

public JJCriticity JJRequirement.getCriticity(){return this.criticity;}

public void JJRequirement.setCriticity(JJCriticity criticity){this.criticity=criticity;}

public JJImportance JJRequirement.getImportance(){return this.importance;}

public void JJRequirement.setImportance(JJImportance importance){this.importance=importance;}

public JJStatus JJRequirement.getStatus(){return this.status;}

public void JJRequirement.setStatus(JJStatus status){this.status=status;}

public JJStatus JJRequirement.getState(){return this.state;}

public void JJRequirement.setState(JJStatus state){this.state=state;}

public JJSprint JJRequirement.getSprint(){return this.sprint;}

public void JJRequirement.setSprint(JJSprint sprint){this.sprint=sprint;}

public String JJRequirement.getImpact(){return this.impact;}

public void JJRequirement.setImpact(String impact){this.impact=impact;}

public String JJRequirement.getNote(){return this.note;}

public void JJRequirement.setNote(String note){this.note=note;}

public Boolean JJRequirement.getOperation(){return this.operation;}

public void JJRequirement.setOperation(Boolean operation){this.operation=operation;}

public Boolean JJRequirement.getFinalState(){return this.finalState;}

public void JJRequirement.setFinalState(Boolean finalState){this.finalState=finalState;}

public Boolean JJRequirement.getCompletion(){return this.completion;}

public void JJRequirement.setCompletion(Boolean completion){this.completion=completion;}

public JJContact JJRequirement.getAssignedTo(){return this.assignedTo;}

public void JJRequirement.setAssignedTo(JJContact assignedTo){this.assignedTo=assignedTo;}

public JJRelationship JJRequirement.getRelation(){return this.relation;}

public void JJRequirement.setRelation(JJRelationship relation){this.relation=relation;}

public Set<JJBug>JJRequirement.getBugs(){return this.bugs;}

public void JJRequirement.setBugs(Set<JJBug>bugs){this.bugs=bugs;}

public Set<JJTask>JJRequirement.getTasks(){return this.tasks;}

public void JJRequirement.setTasks(Set<JJTask>tasks){this.tasks=tasks;}

public Set<JJMessage>JJRequirement.getMessages(){return this.messages;}

public void JJRequirement.setMessages(Set<JJMessage>messages){this.messages=messages;}

public Set<JJRequirement>JJRequirement.getRequirementLinkDown(){return this.requirementLinkDown;}

public void JJRequirement.setRequirementLinkDown(Set<JJRequirement>requirementLinkDown){this.requirementLinkDown=requirementLinkDown;}

public Set<JJRequirement>JJRequirement.getRequirementLinkUp(){return this.requirementLinkUp;}

public void JJRequirement.setRequirementLinkUp(Set<JJRequirement>requirementLinkUp){this.requirementLinkUp=requirementLinkUp;}

public Set<JJTestcase>JJRequirement.getTestcases(){return this.testcases;}

public void JJRequirement.setTestcases(Set<JJTestcase>testcases){this.testcases=testcases;}

public Set<JJContact>JJRequirement.getContacts(){return this.contacts;}

public void JJRequirement.setContacts(Set<JJContact>contacts){this.contacts=contacts;}

}
