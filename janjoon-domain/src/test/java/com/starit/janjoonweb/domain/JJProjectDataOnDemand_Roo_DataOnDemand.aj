// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJProjectDataOnDemand;
import com.starit.janjoonweb.domain.JJProjectRepository;
import com.starit.janjoonweb.domain.JJProjectService;
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

privileged aspect JJProjectDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJProjectDataOnDemand: @Component;
    
    private Random JJProjectDataOnDemand.rnd = new SecureRandom();
    
    private List<JJProject> JJProjectDataOnDemand.data;
    
    @Autowired
    JJContactDataOnDemand JJProjectDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJProjectService JJProjectDataOnDemand.jJProjectService;
    
    @Autowired
    JJProjectRepository JJProjectDataOnDemand.jJProjectRepository;
    
    public JJProject JJProjectDataOnDemand.getNewTransientJJProject(int index) {
        JJProject obj = new JJProject();
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setManager(obj, index);
        setName(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJProjectDataOnDemand.setCreationDate(JJProject obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJProjectDataOnDemand.setDescription(JJProject obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void JJProjectDataOnDemand.setEnabled(JJProject obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJProjectDataOnDemand.setManager(JJProject obj, int index) {
        JJContact manager = jJContactDataOnDemand.getRandomJJContact();
        obj.setManager(manager);
    }
    
    public void JJProjectDataOnDemand.setName(JJProject obj, int index) {
        String name = "name_" + index;
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }
        obj.setName(name);
    }
    
    public void JJProjectDataOnDemand.setUpdatedDate(JJProject obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJProject JJProjectDataOnDemand.getSpecificJJProject(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJProject obj = data.get(index);
        Long id = obj.getId();
        return jJProjectService.findJJProject(id);
    }
    
    public JJProject JJProjectDataOnDemand.getRandomJJProject() {
        init();
        JJProject obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJProjectService.findJJProject(id);
    }
    
    public boolean JJProjectDataOnDemand.modifyJJProject(JJProject obj) {
        return false;
    }
    
    public void JJProjectDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJProjectService.findJJProjectEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJProject' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJProject>();
        for (int i = 0; i < 10; i++) {
            JJProject obj = getNewTransientJJProject(i);
            try {
                jJProjectService.saveJJProject(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJProjectRepository.flush();
            data.add(obj);
        }
    }
    
}
