// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementDataOnDemand;
import com.starit.janjoonweb.domain.JJSprintDataOnDemand;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseDataOnDemand;
import com.starit.janjoonweb.domain.JJTestcaseRepository;
import com.starit.janjoonweb.domain.JJTestcaseService;
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

privileged aspect JJTestcaseDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJTestcaseDataOnDemand: @Component;
    
    private Random JJTestcaseDataOnDemand.rnd = new SecureRandom();
    
    private List<JJTestcase> JJTestcaseDataOnDemand.data;
    
    @Autowired
    JJContactDataOnDemand JJTestcaseDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJRequirementDataOnDemand JJTestcaseDataOnDemand.jJRequirementDataOnDemand;
    
    @Autowired
    JJSprintDataOnDemand JJTestcaseDataOnDemand.jJSprintDataOnDemand;
    
    @Autowired
    JJTestcaseService JJTestcaseDataOnDemand.jJTestcaseService;
    
    @Autowired
    JJTestcaseRepository JJTestcaseDataOnDemand.jJTestcaseRepository;
    
    public JJTestcase JJTestcaseDataOnDemand.getNewTransientJJTestcase(int index) {
        JJTestcase obj = new JJTestcase();
        setAllBuilds(obj, index);
        setAutomatic(obj, index);
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setName(obj, index);
        setOrdering(obj, index);
        setPricepoint(obj, index);
        setRegression(obj, index);
        setRequirement(obj, index);
        setUpdatedDate(obj, index);
        setWorkload(obj, index);
        return obj;
    }
    
    public void JJTestcaseDataOnDemand.setAllBuilds(JJTestcase obj, int index) {
        Boolean allBuilds = Boolean.TRUE;
        obj.setAllBuilds(allBuilds);
    }
    
    public void JJTestcaseDataOnDemand.setAutomatic(JJTestcase obj, int index) {
        Boolean automatic = Boolean.TRUE;
        obj.setAutomatic(automatic);
    }
    
    public void JJTestcaseDataOnDemand.setCreationDate(JJTestcase obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJTestcaseDataOnDemand.setDescription(JJTestcase obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void JJTestcaseDataOnDemand.setEnabled(JJTestcase obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJTestcaseDataOnDemand.setName(JJTestcase obj, int index) {
        String name = "name_" + index;
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }
        obj.setName(name);
    }
    
    public void JJTestcaseDataOnDemand.setOrdering(JJTestcase obj, int index) {
        Integer ordering = new Integer(index);
        obj.setOrdering(ordering);
    }
    
    public void JJTestcaseDataOnDemand.setPricepoint(JJTestcase obj, int index) {
        Integer pricepoint = new Integer(index);
        obj.setPricepoint(pricepoint);
    }
    
    public void JJTestcaseDataOnDemand.setRegression(JJTestcase obj, int index) {
        Boolean regression = Boolean.TRUE;
        obj.setRegression(regression);
    }
    
    public void JJTestcaseDataOnDemand.setRequirement(JJTestcase obj, int index) {
        JJRequirement requirement = jJRequirementDataOnDemand.getRandomJJRequirement();
        obj.setRequirement(requirement);
    }
    
    public void JJTestcaseDataOnDemand.setUpdatedDate(JJTestcase obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public void JJTestcaseDataOnDemand.setWorkload(JJTestcase obj, int index) {
        Integer workload = new Integer(index);
        obj.setWorkload(workload);
    }
    
    public JJTestcase JJTestcaseDataOnDemand.getSpecificJJTestcase(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJTestcase obj = data.get(index);
        Long id = obj.getId();
        return jJTestcaseService.findJJTestcase(id);
    }
    
    public JJTestcase JJTestcaseDataOnDemand.getRandomJJTestcase() {
        init();
        JJTestcase obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJTestcaseService.findJJTestcase(id);
    }
    
    public boolean JJTestcaseDataOnDemand.modifyJJTestcase(JJTestcase obj) {
        return false;
    }
    
    public void JJTestcaseDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJTestcaseService.findJJTestcaseEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJTestcase' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJTestcase>();
        for (int i = 0; i < 10; i++) {
            JJTestcase obj = getNewTransientJJTestcase(i);
            try {
                jJTestcaseService.saveJJTestcase(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJTestcaseRepository.flush();
            data.add(obj);
        }
    }
    
}
