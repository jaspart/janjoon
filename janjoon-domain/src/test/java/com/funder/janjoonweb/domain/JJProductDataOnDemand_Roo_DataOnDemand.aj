// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJContactDataOnDemand;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProductDataOnDemand;
import com.funder.janjoonweb.domain.JJProductRepository;
import com.funder.janjoonweb.domain.JJProductService;
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

privileged aspect JJProductDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJProductDataOnDemand: @Component;
    
    private Random JJProductDataOnDemand.rnd = new SecureRandom();
    
    private List<JJProduct> JJProductDataOnDemand.data;
    
    @Autowired
    JJContactDataOnDemand JJProductDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJProductService JJProductDataOnDemand.jJProductService;
    
    @Autowired
    JJProductRepository JJProductDataOnDemand.jJProductRepository;
    
    public JJProduct JJProductDataOnDemand.getNewTransientJJProduct(int index) {
        JJProduct obj = new JJProduct();
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setExtname(obj, index);
        setName(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJProductDataOnDemand.setCreationDate(JJProduct obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJProductDataOnDemand.setDescription(JJProduct obj, int index) {
        String description = "description_" + index;
        if (description.length() > 250) {
            description = description.substring(0, 250);
        }
        obj.setDescription(description);
    }
    
    public void JJProductDataOnDemand.setEnabled(JJProduct obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJProductDataOnDemand.setExtname(JJProduct obj, int index) {
        String extname = "extname_" + index;
        if (extname.length() > 25) {
            extname = extname.substring(0, 25);
        }
        obj.setExtname(extname);
    }
    
    public void JJProductDataOnDemand.setName(JJProduct obj, int index) {
        String name = "name_" + index;
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        obj.setName(name);
    }
    
    public void JJProductDataOnDemand.setUpdatedDate(JJProduct obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJProduct JJProductDataOnDemand.getSpecificJJProduct(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJProduct obj = data.get(index);
        Long id = obj.getId();
        return jJProductService.findJJProduct(id);
    }
    
    public JJProduct JJProductDataOnDemand.getRandomJJProduct() {
        init();
        JJProduct obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJProductService.findJJProduct(id);
    }
    
    public boolean JJProductDataOnDemand.modifyJJProduct(JJProduct obj) {
        return false;
    }
    
    public void JJProductDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJProductService.findJJProductEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJProduct' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJProduct>();
        for (int i = 0; i < 10; i++) {
            JJProduct obj = getNewTransientJJProduct(i);
            try {
                jJProductService.saveJJProduct(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJProductRepository.flush();
            data.add(obj);
        }
    }
    
}
