// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJCompany;
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
import java.util.Date;

privileged aspect JJMessage_Roo_JavaBean{

public String JJMessage.getName(){return this.name;}

public void JJMessage.setName(String name){this.name=name;}

public String JJMessage.getDescription(){return this.description;}

public void JJMessage.setDescription(String description){this.description=description;}

public Date JJMessage.getCreationDate(){return this.creationDate;}

public void JJMessage.setCreationDate(Date creationDate){this.creationDate=creationDate;}

public JJContact JJMessage.getCreatedBy(){return this.createdBy;}

public void JJMessage.setCreatedBy(JJContact createdBy){this.createdBy=createdBy;}

public JJCompany JJMessage.getCompany(){return this.company;}

public void JJMessage.setCompany(JJCompany company){this.company=company;}

public Date JJMessage.getUpdatedDate(){return this.updatedDate;}

public void JJMessage.setUpdatedDate(Date updatedDate){this.updatedDate=updatedDate;}

public JJContact JJMessage.getUpdatedBy(){return this.updatedBy;}

public void JJMessage.setUpdatedBy(JJContact updatedBy){this.updatedBy=updatedBy;}

public Boolean JJMessage.getEnabled(){return this.enabled;}

public void JJMessage.setEnabled(Boolean enabled){this.enabled=enabled;}

public String JJMessage.getMessage(){return this.message;}

public void JJMessage.setMessage(String message){this.message=message;}

public JJBug JJMessage.getBug(){return this.bug;}

public void JJMessage.setBug(JJBug bug){this.bug=bug;}

public JJBuild JJMessage.getBuild(){return this.build;}

public void JJMessage.setBuild(JJBuild build){this.build=build;}

public JJChapter JJMessage.getChapter(){return this.chapter;}

public void JJMessage.setChapter(JJChapter chapter){this.chapter=chapter;}

public JJContact JJMessage.getContact(){return this.contact;}

public void JJMessage.setContact(JJContact contact){this.contact=contact;}

public JJCriticity JJMessage.getCriticity(){return this.criticity;}

public void JJMessage.setCriticity(JJCriticity criticity){this.criticity=criticity;}

public JJImportance JJMessage.getImportance(){return this.importance;}

public void JJMessage.setImportance(JJImportance importance){this.importance=importance;}

public JJProduct JJMessage.getProduct(){return this.product;}

public void JJMessage.setProduct(JJProduct product){this.product=product;}

public JJProject JJMessage.getProject(){return this.project;}

public void JJMessage.setProject(JJProject project){this.project=project;}

public JJRequirement JJMessage.getRequirement(){return this.requirement;}

public void JJMessage.setRequirement(JJRequirement requirement){this.requirement=requirement;}

public JJSprint JJMessage.getSprint(){return this.sprint;}

public void JJMessage.setSprint(JJSprint sprint){this.sprint=sprint;}

public JJStatus JJMessage.getStatus(){return this.status;}

public void JJMessage.setStatus(JJStatus status){this.status=status;}

public JJTask JJMessage.getTask(){return this.task;}

public void JJMessage.setTask(JJTask task){this.task=task;}

public JJTestcase JJMessage.getTestcase(){return this.testcase;}

public void JJMessage.setTestcase(JJTestcase testcase){this.testcase=testcase;}

public JJTestcase JJMessage.getTeststep(){return this.teststep;}

public void JJMessage.setTeststep(JJTestcase teststep){this.teststep=teststep;}

public JJVersion JJMessage.getVersioning(){return this.versioning;}

public void JJMessage.setVersioning(JJVersion versioning){this.versioning=versioning;}

}
