// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJContactDataOnDemand;
import com.funder.janjoonweb.domain.JJStatus;
import com.funder.janjoonweb.domain.JJStatusDataOnDemand;
import com.funder.janjoonweb.domain.JJStatusRepository;
import com.funder.janjoonweb.domain.JJStatusService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect JJStatusDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJStatusDataOnDemand: @Component;
    
    private Random JJStatusDataOnDemand.rnd = new SecureRandom();
    
    private List<JJStatus> JJStatusDataOnDemand.data;
    
    @Autowired
    private JJContactDataOnDemand JJStatusDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJStatusService JJStatusDataOnDemand.jJStatusService;
    
    @Autowired
    JJStatusRepository JJStatusDataOnDemand.jJStatusRepository;
    
    public JJStatus JJStatusDataOnDemand.getNewTransientJJStatus(int index) {
        JJStatus obj = new JJStatus();
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setName(obj, index);
        setStatusLevel(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJStatusDataOnDemand.setCreationDate(JJStatus obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJStatusDataOnDemand.setDescription(JJStatus obj, int index) {
        String description = "description_" + index;
        if (description.length() > 250) {
            description = description.substring(0, 250);
        }
        obj.setDescription(description);
    }
    
    public void JJStatusDataOnDemand.setEnabled(JJStatus obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJStatusDataOnDemand.setName(JJStatus obj, int index) {
        String name = "name_" + index;
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        obj.setName(name);
    }
    
    public void JJStatusDataOnDemand.setStatusLevel(JJStatus obj, int index) {
        Integer statusLevel = new Integer(index);
        obj.setStatusLevel(statusLevel);
    }
    
    public void JJStatusDataOnDemand.setUpdatedDate(JJStatus obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJStatus JJStatusDataOnDemand.getSpecificJJStatus(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJStatus obj = data.get(index);
        Long id = obj.getId();
        return jJStatusService.findJJStatus(id);
    }
    
    public JJStatus JJStatusDataOnDemand.getRandomJJStatus() {
        init();
        JJStatus obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJStatusService.findJJStatus(id);
    }
    
    public boolean JJStatusDataOnDemand.modifyJJStatus(JJStatus obj) {
        return false;
    }
    
    public void JJStatusDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJStatusService.findJJStatusEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJStatus' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJStatus>();
        for (int i = 0; i < 10; i++) {
            JJStatus obj = getNewTransientJJStatus(i);
            try {
                jJStatusService.saveJJStatus(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            jJStatusRepository.flush();
            data.add(obj);
        }
    }
    
}
