// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJTestcaseexecutionDataOnDemand;
import com.starit.janjoonweb.domain.JJTeststepDataOnDemand;
import com.starit.janjoonweb.domain.JJTeststepexecution;
import com.starit.janjoonweb.domain.JJTeststepexecutionDataOnDemand;
import com.starit.janjoonweb.domain.JJTeststepexecutionRepository;
import com.starit.janjoonweb.domain.JJTeststepexecutionService;
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

privileged aspect JJTeststepexecutionDataOnDemand_Roo_DataOnDemand{

declare @type:JJTeststepexecutionDataOnDemand:@Component;

private Random JJTeststepexecutionDataOnDemand.rnd=new SecureRandom();

private List<JJTeststepexecution>JJTeststepexecutionDataOnDemand.data;

@Autowired JJContactDataOnDemand JJTeststepexecutionDataOnDemand.jJContactDataOnDemand;

@Autowired JJTestcaseexecutionDataOnDemand JJTeststepexecutionDataOnDemand.jJTestcaseexecutionDataOnDemand;

@Autowired JJTeststepDataOnDemand JJTeststepexecutionDataOnDemand.jJTeststepDataOnDemand;

@Autowired JJTeststepexecutionService JJTeststepexecutionDataOnDemand.jJTeststepexecutionService;

@Autowired JJTeststepexecutionRepository JJTeststepexecutionDataOnDemand.jJTeststepexecutionRepository;

public JJTeststepexecution JJTeststepexecutionDataOnDemand.getNewTransientJJTeststepexecution(int index){JJTeststepexecution obj=new JJTeststepexecution();setCreationDate(obj,index);setDescription(obj,index);setEnabled(obj,index);setName(obj,index);setPassed(obj,index);setUpdatedDate(obj,index);return obj;}

public void JJTeststepexecutionDataOnDemand.setCreationDate(JJTeststepexecution obj,int index){Date creationDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setCreationDate(creationDate);}

public void JJTeststepexecutionDataOnDemand.setDescription(JJTeststepexecution obj,int index){String description="description_"+index;obj.setDescription(description);}

public void JJTeststepexecutionDataOnDemand.setEnabled(JJTeststepexecution obj,int index){Boolean enabled=Boolean.TRUE;obj.setEnabled(enabled);}

public void JJTeststepexecutionDataOnDemand.setName(JJTeststepexecution obj,int index){String name="name_"+index;if(name.length()>100){name=name.substring(0,100);}obj.setName(name);}

public void JJTeststepexecutionDataOnDemand.setPassed(JJTeststepexecution obj,int index){Boolean passed=Boolean.TRUE;obj.setPassed(passed);}

public void JJTeststepexecutionDataOnDemand.setUpdatedDate(JJTeststepexecution obj,int index){Date updatedDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setUpdatedDate(updatedDate);}

public JJTeststepexecution JJTeststepexecutionDataOnDemand.getSpecificJJTeststepexecution(int index){init();if(index<0){index=0;}if(index>(data.size()-1)){index=data.size()-1;}JJTeststepexecution obj=data.get(index);Long id=obj.getId();return jJTeststepexecutionService.findJJTeststepexecution(id);}

public JJTeststepexecution JJTeststepexecutionDataOnDemand.getRandomJJTeststepexecution(){init();JJTeststepexecution obj=data.get(rnd.nextInt(data.size()));Long id=obj.getId();return jJTeststepexecutionService.findJJTeststepexecution(id);}

public boolean JJTeststepexecutionDataOnDemand.modifyJJTeststepexecution(JJTeststepexecution obj){return false;}

public void JJTeststepexecutionDataOnDemand.init(){int from=0;int to=10;data=jJTeststepexecutionService.findJJTeststepexecutionEntries(from,to);if(data==null){throw new IllegalStateException("Find entries implementation for 'JJTeststepexecution' illegally returned null");}if(!data.isEmpty()){return;}

data=new ArrayList<JJTeststepexecution>();for(int i=0;i<10;i++){JJTeststepexecution obj=getNewTransientJJTeststepexecution(i);try{jJTeststepexecutionService.saveJJTeststepexecution(obj);}catch(final ConstraintViolationException e){final StringBuilder msg=new StringBuilder();for(Iterator<ConstraintViolation<?>>iter=e.getConstraintViolations().iterator();iter.hasNext();){final ConstraintViolation<?>cv=iter.next();msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");}throw new IllegalStateException(msg.toString(),e);}jJTeststepexecutionRepository.flush();data.add(obj);}}

}
