// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationDataOnDemand;
import com.starit.janjoonweb.domain.JJConfigurationRepository;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContactDataOnDemand;
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

privileged aspect JJConfigurationDataOnDemand_Roo_DataOnDemand{

declare @type:JJConfigurationDataOnDemand:@Component;

private Random JJConfigurationDataOnDemand.rnd=new SecureRandom();

private List<JJConfiguration>JJConfigurationDataOnDemand.data;

@Autowired JJContactDataOnDemand JJConfigurationDataOnDemand.jJContactDataOnDemand;

@Autowired JJConfigurationService JJConfigurationDataOnDemand.jJConfigurationService;

@Autowired JJConfigurationRepository JJConfigurationDataOnDemand.jJConfigurationRepository;

public JJConfiguration JJConfigurationDataOnDemand.getNewTransientJJConfiguration(int index){JJConfiguration obj=new JJConfiguration();setCreationDate(obj,index);setDescription(obj,index);setEnabled(obj,index);setName(obj,index);setParam(obj,index);setUpdatedDate(obj,index);setVal(obj,index);return obj;}

public void JJConfigurationDataOnDemand.setCreationDate(JJConfiguration obj,int index){Date creationDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setCreationDate(creationDate);}

public void JJConfigurationDataOnDemand.setDescription(JJConfiguration obj,int index){String description="description_"+index;obj.setDescription(description);}

public void JJConfigurationDataOnDemand.setEnabled(JJConfiguration obj,int index){Boolean enabled=Boolean.TRUE;obj.setEnabled(enabled);}

public void JJConfigurationDataOnDemand.setName(JJConfiguration obj,int index){String name="name_"+index;if(name.length()>100){name=name.substring(0,100);}obj.setName(name);}

public void JJConfigurationDataOnDemand.setParam(JJConfiguration obj,int index){String param="param_"+index;if(param.length()>100){param=param.substring(0,100);}obj.setParam(param);}

public void JJConfigurationDataOnDemand.setUpdatedDate(JJConfiguration obj,int index){Date updatedDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setUpdatedDate(updatedDate);}

public void JJConfigurationDataOnDemand.setVal(JJConfiguration obj,int index){String val="val_"+index;obj.setVal(val);}

public JJConfiguration JJConfigurationDataOnDemand.getSpecificJJConfiguration(int index){init();if(index<0){index=0;}if(index>(data.size()-1)){index=data.size()-1;}JJConfiguration obj=data.get(index);Long id=obj.getId();return jJConfigurationService.findJJConfiguration(id);}

public JJConfiguration JJConfigurationDataOnDemand.getRandomJJConfiguration(){init();JJConfiguration obj=data.get(rnd.nextInt(data.size()));Long id=obj.getId();return jJConfigurationService.findJJConfiguration(id);}

public boolean JJConfigurationDataOnDemand.modifyJJConfiguration(JJConfiguration obj){return false;}

public void JJConfigurationDataOnDemand.init(){int from=0;int to=10;data=jJConfigurationService.findJJConfigurationEntries(from,to);if(data==null){throw new IllegalStateException("Find entries implementation for 'JJConfiguration' illegally returned null");}if(!data.isEmpty()){return;}

data=new ArrayList<JJConfiguration>();for(int i=0;i<10;i++){JJConfiguration obj=getNewTransientJJConfiguration(i);try{jJConfigurationService.saveJJConfiguration(obj);}catch(final ConstraintViolationException e){final StringBuilder msg=new StringBuilder();for(Iterator<ConstraintViolation<?>>iter=e.getConstraintViolations().iterator();iter.hasNext();){final ConstraintViolation<?>cv=iter.next();msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");}throw new IllegalStateException(msg.toString(),e);}jJConfigurationRepository.flush();data.add(obj);}}

}
