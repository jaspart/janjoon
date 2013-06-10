// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJContactDataOnDemand;
import com.funder.janjoonweb.domain.JJProjectDataOnDemand;
import com.funder.janjoonweb.domain.JJSprint;
import com.funder.janjoonweb.domain.JJSprintDataOnDemand;
import com.funder.janjoonweb.domain.JJSprintRepository;
import com.funder.janjoonweb.domain.JJSprintService;
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

privileged aspect JJSprintDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJSprintDataOnDemand: @Component;
    
    private Random JJSprintDataOnDemand.rnd = new SecureRandom();
    
    private List<JJSprint> JJSprintDataOnDemand.data;
    
    @Autowired
    private JJContactDataOnDemand JJSprintDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    private JJProjectDataOnDemand JJSprintDataOnDemand.jJProjectDataOnDemand;
    
    @Autowired
    JJSprintService JJSprintDataOnDemand.jJSprintService;
    
    @Autowired
    JJSprintRepository JJSprintDataOnDemand.jJSprintRepository;
    
    public JJSprint JJSprintDataOnDemand.getNewTransientJJSprint(int index) {
        JJSprint obj = new JJSprint();
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setName(obj, index);
        setNumero(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJSprintDataOnDemand.setCreationDate(JJSprint obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJSprintDataOnDemand.setDescription(JJSprint obj, int index) {
        String description = "description_" + index;
        if (description.length() > 250) {
            description = description.substring(0, 250);
        }
        obj.setDescription(description);
    }
    
    public void JJSprintDataOnDemand.setEnabled(JJSprint obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJSprintDataOnDemand.setName(JJSprint obj, int index) {
        String name = "name_" + index;
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        obj.setName(name);
    }
    
    public void JJSprintDataOnDemand.setNumero(JJSprint obj, int index) {
        Integer numero = new Integer(index);
        obj.setNumero(numero);
    }
    
    public void JJSprintDataOnDemand.setUpdatedDate(JJSprint obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJSprint JJSprintDataOnDemand.getSpecificJJSprint(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJSprint obj = data.get(index);
        Long id = obj.getId();
        return jJSprintService.findJJSprint(id);
    }
    
    public JJSprint JJSprintDataOnDemand.getRandomJJSprint() {
        init();
        JJSprint obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJSprintService.findJJSprint(id);
    }
    
    public boolean JJSprintDataOnDemand.modifyJJSprint(JJSprint obj) {
        return false;
    }
    
    public void JJSprintDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJSprintService.findJJSprintEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJSprint' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJSprint>();
        for (int i = 0; i < 10; i++) {
            JJSprint obj = getNewTransientJJSprint(i);
            try {
                jJSprintService.saveJJSprint(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            jJSprintRepository.flush();
            data.add(obj);
        }
    }
    
}
