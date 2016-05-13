// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugDataOnDemand;
import com.starit.janjoonweb.domain.JJBugRepository;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJBuildDataOnDemand;
import com.starit.janjoonweb.domain.JJCategoryDataOnDemand;
import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJCriticityDataOnDemand;
import com.starit.janjoonweb.domain.JJImportanceDataOnDemand;
import com.starit.janjoonweb.domain.JJProjectDataOnDemand;
import com.starit.janjoonweb.domain.JJRequirementDataOnDemand;
import com.starit.janjoonweb.domain.JJSprintDataOnDemand;
import com.starit.janjoonweb.domain.JJStatusDataOnDemand;
import com.starit.janjoonweb.domain.JJTeststepDataOnDemand;
import com.starit.janjoonweb.domain.JJVersionDataOnDemand;
import com.starit.janjoonweb.domain.reference.JJRelationship;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Component;

privileged aspect JJBugDataOnDemand_Roo_DataOnDemand{

declare @type:JJBugDataOnDemand:@Component;

private Random JJBugDataOnDemand.rnd=new SecureRandom();

private List<JJBug>JJBugDataOnDemand.data;

@Autowired JJContactDataOnDemand JJBugDataOnDemand.jJContactDataOnDemand;

@Autowired JJBuildDataOnDemand JJBugDataOnDemand.jJBuildDataOnDemand;

@Autowired JJCategoryDataOnDemand JJBugDataOnDemand.jJCategoryDataOnDemand;

@Autowired JJCriticityDataOnDemand JJBugDataOnDemand.jJCriticityDataOnDemand;

@Autowired JJImportanceDataOnDemand JJBugDataOnDemand.jJImportanceDataOnDemand;

@Autowired JJProjectDataOnDemand JJBugDataOnDemand.jJProjectDataOnDemand;

@Autowired JJRequirementDataOnDemand JJBugDataOnDemand.jJRequirementDataOnDemand;

@Autowired JJSprintDataOnDemand JJBugDataOnDemand.jJSprintDataOnDemand;

@Autowired JJStatusDataOnDemand JJBugDataOnDemand.jJStatusDataOnDemand;

@Autowired JJTeststepDataOnDemand JJBugDataOnDemand.jJTeststepDataOnDemand;

@Autowired JJVersionDataOnDemand JJBugDataOnDemand.jJVersionDataOnDemand;

@Autowired JJBugService JJBugDataOnDemand.jJBugService;

@Autowired JJBugRepository JJBugDataOnDemand.jJBugRepository;

public JJBug JJBugDataOnDemand.getNewTransientJJBug(int index){JJBug obj=new JJBug();setBugUp(obj,index);setCreationDate(obj,index);setDescription(obj,index);setEnabled(obj,index);setName(obj,index);setRelation(obj,index);setUpdatedDate(obj,index);setWorkload(obj,index);return obj;}

public void JJBugDataOnDemand.setBugUp(JJBug obj,int index){JJBug bugUp=obj;obj.setBugUp(bugUp);}

public void JJBugDataOnDemand.setCreationDate(JJBug obj,int index){Date creationDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setCreationDate(creationDate);}

public void JJBugDataOnDemand.setDescription(JJBug obj,int index){String description="description_"+index;obj.setDescription(description);}

public void JJBugDataOnDemand.setEnabled(JJBug obj,int index){Boolean enabled=Boolean.TRUE;obj.setEnabled(enabled);}

public void JJBugDataOnDemand.setName(JJBug obj,int index){String name="name_"+index;if(name.length()>100){name=name.substring(0,100);}obj.setName(name);}

public void JJBugDataOnDemand.setRelation(JJBug obj,int index){JJRelationship relation=JJRelationship.class.getEnumConstants()[0];obj.setRelation(relation);}

public void JJBugDataOnDemand.setUpdatedDate(JJBug obj,int index){Date updatedDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setUpdatedDate(updatedDate);}

public void JJBugDataOnDemand.setWorkload(JJBug obj,int index){Integer workload=new Integer(index);obj.setWorkload(workload);}

public JJBug JJBugDataOnDemand.getSpecificJJBug(int index){init();if(index<0){index=0;}if(index>(data.size()-1)){index=data.size()-1;}JJBug obj=data.get(index);Long id=obj.getId();return jJBugService.findJJBug(id);}

public JJBug JJBugDataOnDemand.getRandomJJBug(){init();JJBug obj=data.get(rnd.nextInt(data.size()));Long id=obj.getId();return jJBugService.findJJBug(id);}

public boolean JJBugDataOnDemand.modifyJJBug(JJBug obj){return false;}

public void JJBugDataOnDemand.init(){int from=0;int to=10;data=jJBugService.findJJBugEntries(from,to);if(data==null){throw new IllegalStateException("Find entries implementation for 'JJBug' illegally returned null");}if(!data.isEmpty()){return;}

data=new ArrayList<JJBug>();for(int i=0;i<10;i++){JJBug obj=getNewTransientJJBug(i);try{jJBugService.saveJJBug(obj);}catch(final ConstraintViolationException e){final StringBuilder msg=new StringBuilder();for(Iterator<ConstraintViolation<?>>iter=e.getConstraintViolations().iterator();iter.hasNext();){final ConstraintViolation<?>cv=iter.next();msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");}throw new IllegalStateException(msg.toString(),e);}jJBugRepository.flush();data.add(obj);}}

}
