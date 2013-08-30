// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJContactDataOnDemand;
import com.funder.janjoonweb.domain.JJRequirementDataOnDemand;
import com.funder.janjoonweb.domain.JJTestcaseDataOnDemand;
import com.funder.janjoonweb.domain.JJTeststep;
import com.funder.janjoonweb.domain.JJTeststepDataOnDemand;
import com.funder.janjoonweb.domain.JJTeststepRepository;
import com.funder.janjoonweb.domain.JJTeststepService;
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

privileged aspect JJTeststepDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJTeststepDataOnDemand: @Component;
    
    private Random JJTeststepDataOnDemand.rnd = new SecureRandom();
    
    private List<JJTeststep> JJTeststepDataOnDemand.data;
    
    @Autowired
    JJContactDataOnDemand JJTeststepDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJRequirementDataOnDemand JJTeststepDataOnDemand.jJRequirementDataOnDemand;
    
    @Autowired
    JJTestcaseDataOnDemand JJTeststepDataOnDemand.jJTestcaseDataOnDemand;
    
    @Autowired
    JJTeststepService JJTeststepDataOnDemand.jJTeststepService;
    
    @Autowired
    JJTeststepRepository JJTeststepDataOnDemand.jJTeststepRepository;
    
    public JJTeststep JJTeststepDataOnDemand.getNewTransientJJTeststep(int index) {
        JJTeststep obj = new JJTeststep();
        setActionstep(obj, index);
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setName(obj, index);
        setOrdering(obj, index);
        setPassed(obj, index);
        setResultat(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJTeststepDataOnDemand.setActionstep(JJTeststep obj, int index) {
        String actionstep = "actionstep_" + index;
        if (actionstep.length() > 100) {
            actionstep = actionstep.substring(0, 100);
        }
        obj.setActionstep(actionstep);
    }
    
    public void JJTeststepDataOnDemand.setCreationDate(JJTeststep obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJTeststepDataOnDemand.setDescription(JJTeststep obj, int index) {
        String description = "description_" + index;
        if (description.length() > 500) {
            description = description.substring(0, 500);
        }
        obj.setDescription(description);
    }
    
    public void JJTeststepDataOnDemand.setEnabled(JJTeststep obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJTeststepDataOnDemand.setName(JJTeststep obj, int index) {
        String name = "name_" + index;
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        obj.setName(name);
    }
    
    public void JJTeststepDataOnDemand.setOrdering(JJTeststep obj, int index) {
        Integer ordering = new Integer(index);
        obj.setOrdering(ordering);
    }
    
    public void JJTeststepDataOnDemand.setPassed(JJTeststep obj, int index) {
        Boolean passed = Boolean.TRUE;
        obj.setPassed(passed);
    }
    
    public void JJTeststepDataOnDemand.setResultat(JJTeststep obj, int index) {
        String resultat = "resultat_" + index;
        if (resultat.length() > 100) {
            resultat = resultat.substring(0, 100);
        }
        obj.setResultat(resultat);
    }
    
    public void JJTeststepDataOnDemand.setUpdatedDate(JJTeststep obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJTeststep JJTeststepDataOnDemand.getSpecificJJTeststep(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJTeststep obj = data.get(index);
        Long id = obj.getId();
        return jJTeststepService.findJJTeststep(id);
    }
    
    public JJTeststep JJTeststepDataOnDemand.getRandomJJTeststep() {
        init();
        JJTeststep obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJTeststepService.findJJTeststep(id);
    }
    
    public boolean JJTeststepDataOnDemand.modifyJJTeststep(JJTeststep obj) {
        return false;
    }
    
    public void JJTeststepDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJTeststepService.findJJTeststepEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJTeststep' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJTeststep>();
        for (int i = 0; i < 10; i++) {
            JJTeststep obj = getNewTransientJJTeststep(i);
            try {
                jJTeststepService.saveJJTeststep(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJTeststepRepository.flush();
            data.add(obj);
        }
    }
    
}
