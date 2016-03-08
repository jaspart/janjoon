// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBuildDataOnDemand;
import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJTestcaseDataOnDemand;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTestcaseexecutionDataOnDemand;
import com.starit.janjoonweb.domain.JJTestcaseexecutionRepository;
import com.starit.janjoonweb.domain.JJTestcaseexecutionService;
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

privileged aspect JJTestcaseexecutionDataOnDemand_Roo_DataOnDemand{

declare @type:JJTestcaseexecutionDataOnDemand:@Component;

private Random JJTestcaseexecutionDataOnDemand.rnd=new SecureRandom();

private List<JJTestcaseexecution>JJTestcaseexecutionDataOnDemand.data;

@Autowired JJBuildDataOnDemand JJTestcaseexecutionDataOnDemand.jJBuildDataOnDemand;

@Autowired JJContactDataOnDemand JJTestcaseexecutionDataOnDemand.jJContactDataOnDemand;

@Autowired JJTestcaseDataOnDemand JJTestcaseexecutionDataOnDemand.jJTestcaseDataOnDemand;

@Autowired JJTestcaseexecutionService JJTestcaseexecutionDataOnDemand.jJTestcaseexecutionService;

@Autowired JJTestcaseexecutionRepository JJTestcaseexecutionDataOnDemand.jJTestcaseexecutionRepository;

public JJTestcaseexecution JJTestcaseexecutionDataOnDemand.getNewTransientJJTestcaseexecution(int index){JJTestcaseexecution obj=new JJTestcaseexecution();setCreationDate(obj,index);setDescription(obj,index);setEnabled(obj,index);setName(obj,index);setPassed(obj,index);setUpdatedDate(obj,index);return obj;}

public void JJTestcaseexecutionDataOnDemand.setCreationDate(JJTestcaseexecution obj,int index){Date creationDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setCreationDate(creationDate);}

public void JJTestcaseexecutionDataOnDemand.setDescription(JJTestcaseexecution obj,int index){String description="description_"+index;obj.setDescription(description);}

public void JJTestcaseexecutionDataOnDemand.setEnabled(JJTestcaseexecution obj,int index){Boolean enabled=Boolean.TRUE;obj.setEnabled(enabled);}

public void JJTestcaseexecutionDataOnDemand.setName(JJTestcaseexecution obj,int index){String name="name_"+index;if(name.length()>100){name=name.substring(0,100);}obj.setName(name);}

public void JJTestcaseexecutionDataOnDemand.setPassed(JJTestcaseexecution obj,int index){Boolean passed=Boolean.TRUE;obj.setPassed(passed);}

public void JJTestcaseexecutionDataOnDemand.setUpdatedDate(JJTestcaseexecution obj,int index){Date updatedDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setUpdatedDate(updatedDate);}

public JJTestcaseexecution JJTestcaseexecutionDataOnDemand.getSpecificJJTestcaseexecution(int index){init();if(index<0){index=0;}if(index>(data.size()-1)){index=data.size()-1;}JJTestcaseexecution obj=data.get(index);Long id=obj.getId();return jJTestcaseexecutionService.findJJTestcaseexecution(id);}

public JJTestcaseexecution JJTestcaseexecutionDataOnDemand.getRandomJJTestcaseexecution(){init();JJTestcaseexecution obj=data.get(rnd.nextInt(data.size()));Long id=obj.getId();return jJTestcaseexecutionService.findJJTestcaseexecution(id);}

public boolean JJTestcaseexecutionDataOnDemand.modifyJJTestcaseexecution(JJTestcaseexecution obj){return false;}

public void JJTestcaseexecutionDataOnDemand.init(){int from=0;int to=10;data=jJTestcaseexecutionService.findJJTestcaseexecutionEntries(from,to);if(data==null){throw new IllegalStateException("Find entries implementation for 'JJTestcaseexecution' illegally returned null");}if(!data.isEmpty()){return;}

data=new ArrayList<JJTestcaseexecution>();for(int i=0;i<10;i++){JJTestcaseexecution obj=getNewTransientJJTestcaseexecution(i);try{jJTestcaseexecutionService.saveJJTestcaseexecution(obj);}catch(final ConstraintViolationException e){final StringBuilder msg=new StringBuilder();for(Iterator<ConstraintViolation<?>>iter=e.getConstraintViolations().iterator();iter.hasNext();){final ConstraintViolation<?>cv=iter.next();msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");}throw new IllegalStateException(msg.toString(),e);}jJTestcaseexecutionRepository.flush();data.add(obj);}}

}
