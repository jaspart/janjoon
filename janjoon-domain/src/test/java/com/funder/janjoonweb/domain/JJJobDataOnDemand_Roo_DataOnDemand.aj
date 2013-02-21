// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJContactDataOnDemand;
import com.funder.janjoonweb.domain.JJJob;
import com.funder.janjoonweb.domain.JJJobDataOnDemand;
import com.funder.janjoonweb.domain.JJJobRepository;
import com.funder.janjoonweb.domain.JJJobService;
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

privileged aspect JJJobDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJJobDataOnDemand: @Component;
    
    private Random JJJobDataOnDemand.rnd = new SecureRandom();
    
    private List<JJJob> JJJobDataOnDemand.data;
    
    @Autowired
    JJContactDataOnDemand JJJobDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJJobService JJJobDataOnDemand.jJJobService;
    
    @Autowired
    JJJobRepository JJJobDataOnDemand.jJJobRepository;
    
    public JJJob JJJobDataOnDemand.getNewTransientJJJob(int index) {
        JJJob obj = new JJJob();
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setName(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJJobDataOnDemand.setCreationDate(JJJob obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJJobDataOnDemand.setDescription(JJJob obj, int index) {
        String description = "description_" + index;
        if (description.length() > 250) {
            description = description.substring(0, 250);
        }
        obj.setDescription(description);
    }
    
    public void JJJobDataOnDemand.setEnabled(JJJob obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJJobDataOnDemand.setName(JJJob obj, int index) {
        String name = "name_" + index;
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        obj.setName(name);
    }
    
    public void JJJobDataOnDemand.setUpdatedDate(JJJob obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJJob JJJobDataOnDemand.getSpecificJJJob(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJJob obj = data.get(index);
        Long id = obj.getId();
        return jJJobService.findJJJob(id);
    }
    
    public JJJob JJJobDataOnDemand.getRandomJJJob() {
        init();
        JJJob obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJJobService.findJJJob(id);
    }
    
    public boolean JJJobDataOnDemand.modifyJJJob(JJJob obj) {
        return false;
    }
    
    public void JJJobDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJJobService.findJJJobEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJJob' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJJob>();
        for (int i = 0; i < 10; i++) {
            JJJob obj = getNewTransientJJJob(i);
            try {
                jJJobService.saveJJJob(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            jJJobRepository.flush();
            data.add(obj);
        }
    }
    
}
