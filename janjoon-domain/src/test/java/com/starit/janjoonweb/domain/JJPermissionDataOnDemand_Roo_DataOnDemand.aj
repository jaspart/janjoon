// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJPermission;
import com.starit.janjoonweb.domain.JJPermissionDataOnDemand;
import com.starit.janjoonweb.domain.JJPermissionRepository;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProductDataOnDemand;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJProfileDataOnDemand;
import com.starit.janjoonweb.domain.JJProjectDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Component;

privileged aspect JJPermissionDataOnDemand_Roo_DataOnDemand{

declare @type:JJPermissionDataOnDemand:@Component;

private Random JJPermissionDataOnDemand.rnd=new SecureRandom();

private List<JJPermission>JJPermissionDataOnDemand.data;

@Autowired JJContactDataOnDemand JJPermissionDataOnDemand.jJContactDataOnDemand;

@Autowired JJProductDataOnDemand JJPermissionDataOnDemand.jJProductDataOnDemand;

@Autowired JJProfileDataOnDemand JJPermissionDataOnDemand.jJProfileDataOnDemand;

@Autowired JJProjectDataOnDemand JJPermissionDataOnDemand.jJProjectDataOnDemand;

@Autowired JJPermissionService JJPermissionDataOnDemand.jJPermissionService;

@Autowired JJPermissionRepository JJPermissionDataOnDemand.jJPermissionRepository;

public JJPermission JJPermissionDataOnDemand.getNewTransientJJPermission(int index){JJPermission obj=new JJPermission();setContact(obj,index);setEnabled(obj,index);setProfile(obj,index);return obj;}

public void JJPermissionDataOnDemand.setContact(JJPermission obj,int index){JJContact contact=jJContactDataOnDemand.getRandomJJContact();obj.setContact(contact);}

public void JJPermissionDataOnDemand.setEnabled(JJPermission obj,int index){Boolean enabled=Boolean.TRUE;obj.setEnabled(enabled);}

public void JJPermissionDataOnDemand.setProfile(JJPermission obj,int index){JJProfile profile=jJProfileDataOnDemand.getRandomJJProfile();obj.setProfile(profile);}

public JJPermission JJPermissionDataOnDemand.getSpecificJJPermission(int index){init();if(index<0){index=0;}if(index>(data.size()-1)){index=data.size()-1;}JJPermission obj=data.get(index);Long id=obj.getId();return jJPermissionService.findJJPermission(id);}

public JJPermission JJPermissionDataOnDemand.getRandomJJPermission(){init();JJPermission obj=data.get(rnd.nextInt(data.size()));Long id=obj.getId();return jJPermissionService.findJJPermission(id);}

public boolean JJPermissionDataOnDemand.modifyJJPermission(JJPermission obj){return false;}

public void JJPermissionDataOnDemand.init(){int from=0;int to=10;data=jJPermissionService.findJJPermissionEntries(from,to);if(data==null){throw new IllegalStateException("Find entries implementation for 'JJPermission' illegally returned null");}if(!data.isEmpty()){return;}

data=new ArrayList<JJPermission>();for(int i=0;i<10;i++){JJPermission obj=getNewTransientJJPermission(i);try{jJPermissionService.saveJJPermission(obj);}catch(final ConstraintViolationException e){final StringBuilder msg=new StringBuilder();for(Iterator<ConstraintViolation<?>>iter=e.getConstraintViolations().iterator();iter.hasNext();){final ConstraintViolation<?>cv=iter.next();msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");}throw new IllegalStateException(msg.toString(),e);}jJPermissionRepository.flush();data.add(obj);}}

}
