// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJStatusDataOnDemand;
import com.starit.janjoonweb.domain.JJWorkflow;
import com.starit.janjoonweb.domain.JJWorkflowDataOnDemand;
import com.starit.janjoonweb.domain.JJWorkflowRepository;
import com.starit.janjoonweb.domain.JJWorkflowService;
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

privileged aspect JJWorkflowDataOnDemand_Roo_DataOnDemand{

declare @type:JJWorkflowDataOnDemand:@Component;

private Random JJWorkflowDataOnDemand.rnd=new SecureRandom();

private List<JJWorkflow>JJWorkflowDataOnDemand.data;

@Autowired JJContactDataOnDemand JJWorkflowDataOnDemand.jJContactDataOnDemand;

@Autowired JJStatusDataOnDemand JJWorkflowDataOnDemand.jJStatusDataOnDemand;

@Autowired JJWorkflowService JJWorkflowDataOnDemand.jJWorkflowService;

@Autowired JJWorkflowRepository JJWorkflowDataOnDemand.jJWorkflowRepository;

public JJWorkflow JJWorkflowDataOnDemand.getNewTransientJJWorkflow(int index){JJWorkflow obj=new JJWorkflow();setActionWorkflow(obj,index);setCreationDate(obj,index);setDescription(obj,index);setEnabled(obj,index);setEvent(obj,index);setName(obj,index);setObjet(obj,index);setUpdatedDate(obj,index);return obj;}

public void JJWorkflowDataOnDemand.setActionWorkflow(JJWorkflow obj,int index){String actionWorkflow="actionWorkflow_"+index;obj.setActionWorkflow(actionWorkflow);}

public void JJWorkflowDataOnDemand.setCreationDate(JJWorkflow obj,int index){Date creationDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setCreationDate(creationDate);}

public void JJWorkflowDataOnDemand.setDescription(JJWorkflow obj,int index){String description="description_"+index;obj.setDescription(description);}

public void JJWorkflowDataOnDemand.setEnabled(JJWorkflow obj,int index){Boolean enabled=Boolean.TRUE;obj.setEnabled(enabled);}

public void JJWorkflowDataOnDemand.setEvent(JJWorkflow obj,int index){String event="event_"+index;obj.setEvent(event);}

public void JJWorkflowDataOnDemand.setName(JJWorkflow obj,int index){String name="name_"+index;if(name.length()>100){name=name.substring(0,100);}obj.setName(name);}

public void JJWorkflowDataOnDemand.setObjet(JJWorkflow obj,int index){String objet="objet_"+index;obj.setObjet(objet);}

public void JJWorkflowDataOnDemand.setUpdatedDate(JJWorkflow obj,int index){Date updatedDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setUpdatedDate(updatedDate);}

public JJWorkflow JJWorkflowDataOnDemand.getSpecificJJWorkflow(int index){init();if(index<0){index=0;}if(index>(data.size()-1)){index=data.size()-1;}JJWorkflow obj=data.get(index);Long id=obj.getId();return jJWorkflowService.findJJWorkflow(id);}

public JJWorkflow JJWorkflowDataOnDemand.getRandomJJWorkflow(){init();JJWorkflow obj=data.get(rnd.nextInt(data.size()));Long id=obj.getId();return jJWorkflowService.findJJWorkflow(id);}

public boolean JJWorkflowDataOnDemand.modifyJJWorkflow(JJWorkflow obj){return false;}

public void JJWorkflowDataOnDemand.init(){int from=0;int to=10;data=jJWorkflowService.findJJWorkflowEntries(from,to);if(data==null){throw new IllegalStateException("Find entries implementation for 'JJWorkflow' illegally returned null");}if(!data.isEmpty()){return;}

data=new ArrayList<JJWorkflow>();for(int i=0;i<10;i++){JJWorkflow obj=getNewTransientJJWorkflow(i);try{jJWorkflowService.saveJJWorkflow(obj);}catch(final ConstraintViolationException e){final StringBuilder msg=new StringBuilder();for(Iterator<ConstraintViolation<?>>iter=e.getConstraintViolations().iterator();iter.hasNext();){final ConstraintViolation<?>cv=iter.next();msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");}throw new IllegalStateException(msg.toString(),e);}jJWorkflowRepository.flush();data.add(obj);}}

}
