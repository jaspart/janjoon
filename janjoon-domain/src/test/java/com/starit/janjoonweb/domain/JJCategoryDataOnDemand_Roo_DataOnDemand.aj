// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryDataOnDemand;
import com.starit.janjoonweb.domain.JJCategoryRepository;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJCompanyDataOnDemand;
import com.starit.janjoonweb.domain.JJContactDataOnDemand;
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

privileged aspect JJCategoryDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJCategoryDataOnDemand: @Component;
    
    private Random JJCategoryDataOnDemand.rnd = new SecureRandom();
    
    private List<JJCategory> JJCategoryDataOnDemand.data;
    
    @Autowired
    JJCompanyDataOnDemand JJCategoryDataOnDemand.jJCompanyDataOnDemand;
    
    @Autowired
    JJContactDataOnDemand JJCategoryDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJCategoryService JJCategoryDataOnDemand.jJCategoryService;
    
    @Autowired
    JJCategoryRepository JJCategoryDataOnDemand.jJCategoryRepository;
    
    public JJCategory JJCategoryDataOnDemand.getNewTransientJJCategory(int index) {
        JJCategory obj = new JJCategory();
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setName(obj, index);
        setStage(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJCategoryDataOnDemand.setCreationDate(JJCategory obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJCategoryDataOnDemand.setDescription(JJCategory obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void JJCategoryDataOnDemand.setEnabled(JJCategory obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJCategoryDataOnDemand.setName(JJCategory obj, int index) {
        String name = "name_" + index;
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }
        obj.setName(name);
    }
    
    public void JJCategoryDataOnDemand.setStage(JJCategory obj, int index) {
        Integer stage = new Integer(index);
        obj.setStage(stage);
    }
    
    public void JJCategoryDataOnDemand.setUpdatedDate(JJCategory obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJCategory JJCategoryDataOnDemand.getSpecificJJCategory(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJCategory obj = data.get(index);
        Long id = obj.getId();
        return jJCategoryService.findJJCategory(id);
    }
    
    public JJCategory JJCategoryDataOnDemand.getRandomJJCategory() {
        init();
        JJCategory obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJCategoryService.findJJCategory(id);
    }
    
    public boolean JJCategoryDataOnDemand.modifyJJCategory(JJCategory obj) {
        return false;
    }
    
    public void JJCategoryDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJCategoryService.findJJCategoryEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJCategory' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJCategory>();
        for (int i = 0; i < 10; i++) {
            JJCategory obj = getNewTransientJJCategory(i);
            try {
                jJCategoryService.saveJJCategory(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJCategoryRepository.flush();
            data.add(obj);
        }
    }
    
}
