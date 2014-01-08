// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJContactDataOnDemand;
import com.funder.janjoonweb.domain.JJImportance;
import com.funder.janjoonweb.domain.JJImportanceDataOnDemand;
import com.funder.janjoonweb.domain.JJImportanceRepository;
import com.funder.janjoonweb.domain.JJImportanceService;
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

privileged aspect JJImportanceDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJImportanceDataOnDemand: @Component;
    
    private Random JJImportanceDataOnDemand.rnd = new SecureRandom();
    
    private List<JJImportance> JJImportanceDataOnDemand.data;
    
    @Autowired
    JJContactDataOnDemand JJImportanceDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJImportanceService JJImportanceDataOnDemand.jJImportanceService;
    
    @Autowired
    JJImportanceRepository JJImportanceDataOnDemand.jJImportanceRepository;
    
    public JJImportance JJImportanceDataOnDemand.getNewTransientJJImportance(int index) {
        JJImportance obj = new JJImportance();
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setImportanceLevel(obj, index);
        setName(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJImportanceDataOnDemand.setCreationDate(JJImportance obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJImportanceDataOnDemand.setDescription(JJImportance obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void JJImportanceDataOnDemand.setEnabled(JJImportance obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJImportanceDataOnDemand.setImportanceLevel(JJImportance obj, int index) {
        Integer importanceLevel = new Integer(index);
        obj.setImportanceLevel(importanceLevel);
    }
    
    public void JJImportanceDataOnDemand.setName(JJImportance obj, int index) {
        String name = "name_" + index;
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        obj.setName(name);
    }
    
    public void JJImportanceDataOnDemand.setUpdatedDate(JJImportance obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJImportance JJImportanceDataOnDemand.getSpecificJJImportance(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJImportance obj = data.get(index);
        Long id = obj.getId();
        return jJImportanceService.findJJImportance(id);
    }
    
    public JJImportance JJImportanceDataOnDemand.getRandomJJImportance() {
        init();
        JJImportance obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJImportanceService.findJJImportance(id);
    }
    
    public boolean JJImportanceDataOnDemand.modifyJJImportance(JJImportance obj) {
        return false;
    }
    
    public void JJImportanceDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJImportanceService.findJJImportanceEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJImportance' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJImportance>();
        for (int i = 0; i < 10; i++) {
            JJImportance obj = getNewTransientJJImportance(i);
            try {
                jJImportanceService.saveJJImportance(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJImportanceRepository.flush();
            data.add(obj);
        }
    }
    
}
