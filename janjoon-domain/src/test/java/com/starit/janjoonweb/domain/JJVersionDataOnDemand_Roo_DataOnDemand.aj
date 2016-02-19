// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProductDataOnDemand;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.JJVersionDataOnDemand;
import com.starit.janjoonweb.domain.JJVersionRepository;
import com.starit.janjoonweb.domain.JJVersionService;
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

privileged aspect JJVersionDataOnDemand_Roo_DataOnDemand{

declare @type:JJVersionDataOnDemand:@Component;

private Random JJVersionDataOnDemand.rnd=new SecureRandom();

private List<JJVersion>JJVersionDataOnDemand.data;

@Autowired JJContactDataOnDemand JJVersionDataOnDemand.jJContactDataOnDemand;

@Autowired JJProductDataOnDemand JJVersionDataOnDemand.jJProductDataOnDemand;

@Autowired JJVersionService JJVersionDataOnDemand.jJVersionService;

@Autowired JJVersionRepository JJVersionDataOnDemand.jJVersionRepository;

public JJVersion JJVersionDataOnDemand.getNewTransientJJVersion(int index){JJVersion obj=new JJVersion();setCreationDate(obj,index);setDescription(obj,index);setEnabled(obj,index);setName(obj,index);setProduct(obj,index);setUpdatedDate(obj,index);return obj;}

public void JJVersionDataOnDemand.setCreationDate(JJVersion obj,int index){Date creationDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setCreationDate(creationDate);}

public void JJVersionDataOnDemand.setDescription(JJVersion obj,int index){String description="description_"+index;obj.setDescription(description);}

public void JJVersionDataOnDemand.setEnabled(JJVersion obj,int index){Boolean enabled=Boolean.TRUE;obj.setEnabled(enabled);}

public void JJVersionDataOnDemand.setName(JJVersion obj,int index){String name="name_"+index;if(name.length()>100){name=name.substring(0,100);}obj.setName(name);}

public void JJVersionDataOnDemand.setProduct(JJVersion obj,int index){JJProduct product=jJProductDataOnDemand.getRandomJJProduct();obj.setProduct(product);}

public void JJVersionDataOnDemand.setUpdatedDate(JJVersion obj,int index){Date updatedDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setUpdatedDate(updatedDate);}

public JJVersion JJVersionDataOnDemand.getSpecificJJVersion(int index){init();if(index<0){index=0;}if(index>(data.size()-1)){index=data.size()-1;}JJVersion obj=data.get(index);Long id=obj.getId();return jJVersionService.findJJVersion(id);}

public JJVersion JJVersionDataOnDemand.getRandomJJVersion(){init();JJVersion obj=data.get(rnd.nextInt(data.size()));Long id=obj.getId();return jJVersionService.findJJVersion(id);}

public boolean JJVersionDataOnDemand.modifyJJVersion(JJVersion obj){return false;}

public void JJVersionDataOnDemand.init(){int from=0;int to=10;data=jJVersionService.findJJVersionEntries(from,to);if(data==null){throw new IllegalStateException("Find entries implementation for 'JJVersion' illegally returned null");}if(!data.isEmpty()){return;}

data=new ArrayList<JJVersion>();for(int i=0;i<10;i++){JJVersion obj=getNewTransientJJVersion(i);try{jJVersionService.saveJJVersion(obj);}catch(final ConstraintViolationException e){final StringBuilder msg=new StringBuilder();for(Iterator<ConstraintViolation<?>>iter=e.getConstraintViolations().iterator();iter.hasNext();){final ConstraintViolation<?>cv=iter.next();msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");}throw new IllegalStateException(msg.toString(),e);}jJVersionRepository.flush();data.add(obj);}}

}
