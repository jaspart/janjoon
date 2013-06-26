// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJContactDataOnDemand;
import com.funder.janjoonweb.domain.JJSoftware;
import com.funder.janjoonweb.domain.JJSoftwareDataOnDemand;
import com.funder.janjoonweb.domain.JJSoftwareRepository;
import com.funder.janjoonweb.domain.JJSoftwareService;
import com.funder.janjoonweb.domain.JJTestcaseDataOnDemand;
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

privileged aspect JJSoftwareDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJSoftwareDataOnDemand: @Component;
    
    private Random JJSoftwareDataOnDemand.rnd = new SecureRandom();
    
    private List<JJSoftware> JJSoftwareDataOnDemand.data;
    
    @Autowired
    JJContactDataOnDemand JJSoftwareDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJTestcaseDataOnDemand JJSoftwareDataOnDemand.jJTestcaseDataOnDemand;
    
    @Autowired
    JJSoftwareService JJSoftwareDataOnDemand.jJSoftwareService;
    
    @Autowired
    JJSoftwareRepository JJSoftwareDataOnDemand.jJSoftwareRepository;
    
    public JJSoftware JJSoftwareDataOnDemand.getNewTransientJJSoftware(int index) {
        JJSoftware obj = new JJSoftware();
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setName(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJSoftwareDataOnDemand.setCreationDate(JJSoftware obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJSoftwareDataOnDemand.setDescription(JJSoftware obj, int index) {
        String description = "description_" + index;
        if (description.length() > 250) {
            description = description.substring(0, 250);
        }
        obj.setDescription(description);
    }
    
    public void JJSoftwareDataOnDemand.setEnabled(JJSoftware obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJSoftwareDataOnDemand.setName(JJSoftware obj, int index) {
        String name = "name_" + index;
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        obj.setName(name);
    }
    
    public void JJSoftwareDataOnDemand.setUpdatedDate(JJSoftware obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJSoftware JJSoftwareDataOnDemand.getSpecificJJSoftware(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJSoftware obj = data.get(index);
        Long id = obj.getId();
        return jJSoftwareService.findJJSoftware(id);
    }
    
    public JJSoftware JJSoftwareDataOnDemand.getRandomJJSoftware() {
        init();
        JJSoftware obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJSoftwareService.findJJSoftware(id);
    }
    
    public boolean JJSoftwareDataOnDemand.modifyJJSoftware(JJSoftware obj) {
        return false;
    }
    
    public void JJSoftwareDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJSoftwareService.findJJSoftwareEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJSoftware' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJSoftware>();
        for (int i = 0; i < 10; i++) {
            JJSoftware obj = getNewTransientJJSoftware(i);
            try {
                jJSoftwareService.saveJJSoftware(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJSoftwareRepository.flush();
            data.add(obj);
        }
    }
    
}
