// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBugDataOnDemand;
import com.starit.janjoonweb.domain.JJBuildDataOnDemand;
import com.starit.janjoonweb.domain.JJChapterDataOnDemand;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJCompanyDataOnDemand;
import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJCriticityDataOnDemand;
import com.starit.janjoonweb.domain.JJImportanceDataOnDemand;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJMessageDataOnDemand;
import com.starit.janjoonweb.domain.JJMessageRepository;
import com.starit.janjoonweb.domain.JJMessageService;
import com.starit.janjoonweb.domain.JJProductDataOnDemand;
import com.starit.janjoonweb.domain.JJProjectDataOnDemand;
import com.starit.janjoonweb.domain.JJRequirementDataOnDemand;
import com.starit.janjoonweb.domain.JJSprintDataOnDemand;
import com.starit.janjoonweb.domain.JJStatusDataOnDemand;
import com.starit.janjoonweb.domain.JJTaskDataOnDemand;
import com.starit.janjoonweb.domain.JJTestcaseDataOnDemand;
import com.starit.janjoonweb.domain.JJVersionDataOnDemand;
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

privileged aspect JJMessageDataOnDemand_Roo_DataOnDemand{

declare @type:JJMessageDataOnDemand:@Component;

private Random JJMessageDataOnDemand.rnd=new SecureRandom();

private List<JJMessage>JJMessageDataOnDemand.data;

@Autowired JJBugDataOnDemand JJMessageDataOnDemand.jJBugDataOnDemand;

@Autowired JJBuildDataOnDemand JJMessageDataOnDemand.jJBuildDataOnDemand;

@Autowired JJChapterDataOnDemand JJMessageDataOnDemand.jJChapterDataOnDemand;

@Autowired JJCompanyDataOnDemand JJMessageDataOnDemand.jJCompanyDataOnDemand;

@Autowired JJContactDataOnDemand JJMessageDataOnDemand.jJContactDataOnDemand;

@Autowired JJCriticityDataOnDemand JJMessageDataOnDemand.jJCriticityDataOnDemand;

@Autowired JJImportanceDataOnDemand JJMessageDataOnDemand.jJImportanceDataOnDemand;

@Autowired JJProductDataOnDemand JJMessageDataOnDemand.jJProductDataOnDemand;

@Autowired JJProjectDataOnDemand JJMessageDataOnDemand.jJProjectDataOnDemand;

@Autowired JJRequirementDataOnDemand JJMessageDataOnDemand.jJRequirementDataOnDemand;

@Autowired JJSprintDataOnDemand JJMessageDataOnDemand.jJSprintDataOnDemand;

@Autowired JJStatusDataOnDemand JJMessageDataOnDemand.jJStatusDataOnDemand;

@Autowired JJTaskDataOnDemand JJMessageDataOnDemand.jJTaskDataOnDemand;

@Autowired JJTestcaseDataOnDemand JJMessageDataOnDemand.jJTestcaseDataOnDemand;

@Autowired JJVersionDataOnDemand JJMessageDataOnDemand.jJVersionDataOnDemand;

@Autowired JJMessageService JJMessageDataOnDemand.jJMessageService;

@Autowired JJMessageRepository JJMessageDataOnDemand.jJMessageRepository;

public JJMessage JJMessageDataOnDemand.getNewTransientJJMessage(int index){JJMessage obj=new JJMessage();setCompany(obj,index);setCreationDate(obj,index);setDescription(obj,index);setEnabled(obj,index);setMessage(obj,index);setName(obj,index);setUpdatedDate(obj,index);return obj;}

public void JJMessageDataOnDemand.setCompany(JJMessage obj,int index){JJCompany company=jJCompanyDataOnDemand.getRandomJJCompany();obj.setCompany(company);}

public void JJMessageDataOnDemand.setCreationDate(JJMessage obj,int index){Date creationDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setCreationDate(creationDate);}

public void JJMessageDataOnDemand.setDescription(JJMessage obj,int index){String description="description_"+index;obj.setDescription(description);}

public void JJMessageDataOnDemand.setEnabled(JJMessage obj,int index){Boolean enabled=Boolean.TRUE;obj.setEnabled(enabled);}

public void JJMessageDataOnDemand.setMessage(JJMessage obj,int index){String message="message_"+index;if(message.length()>250){message=message.substring(0,250);}obj.setMessage(message);}

public void JJMessageDataOnDemand.setName(JJMessage obj,int index){String name="name_"+index;if(name.length()>100){name=name.substring(0,100);}obj.setName(name);}

public void JJMessageDataOnDemand.setUpdatedDate(JJMessage obj,int index){Date updatedDate=new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)+new Double(Math.random()*1000).intValue()).getTime();obj.setUpdatedDate(updatedDate);}

public JJMessage JJMessageDataOnDemand.getSpecificJJMessage(int index){init();if(index<0){index=0;}if(index>(data.size()-1)){index=data.size()-1;}JJMessage obj=data.get(index);Long id=obj.getId();return jJMessageService.findJJMessage(id);}

public JJMessage JJMessageDataOnDemand.getRandomJJMessage(){init();JJMessage obj=data.get(rnd.nextInt(data.size()));Long id=obj.getId();return jJMessageService.findJJMessage(id);}

public boolean JJMessageDataOnDemand.modifyJJMessage(JJMessage obj){return false;}

public void JJMessageDataOnDemand.init(){int from=0;int to=10;data=jJMessageService.findJJMessageEntries(from,to);if(data==null){throw new IllegalStateException("Find entries implementation for 'JJMessage' illegally returned null");}if(!data.isEmpty()){return;}

data=new ArrayList<JJMessage>();for(int i=0;i<10;i++){JJMessage obj=getNewTransientJJMessage(i);try{jJMessageService.saveJJMessage(obj);}catch(final ConstraintViolationException e){final StringBuilder msg=new StringBuilder();for(Iterator<ConstraintViolation<?>>iter=e.getConstraintViolations().iterator();iter.hasNext();){final ConstraintViolation<?>cv=iter.next();msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");}throw new IllegalStateException(msg.toString(),e);}jJMessageRepository.flush();data.add(obj);}}

}
