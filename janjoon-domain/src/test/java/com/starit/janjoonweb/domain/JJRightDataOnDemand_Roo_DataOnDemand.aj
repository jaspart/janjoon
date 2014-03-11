// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJCategoryDataOnDemand;
import com.starit.janjoonweb.domain.JJProfileDataOnDemand;
import com.starit.janjoonweb.domain.JJRight;
import com.starit.janjoonweb.domain.JJRightDataOnDemand;
import com.starit.janjoonweb.domain.JJRightRepository;
import com.starit.janjoonweb.domain.JJRightService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect JJRightDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJRightDataOnDemand: @Component;
    
    private Random JJRightDataOnDemand.rnd = new SecureRandom();
    
    private List<JJRight> JJRightDataOnDemand.data;
    
    @Autowired
    JJCategoryDataOnDemand JJRightDataOnDemand.jJCategoryDataOnDemand;
    
    @Autowired
    JJProfileDataOnDemand JJRightDataOnDemand.jJProfileDataOnDemand;
    
    @Autowired
    JJRightService JJRightDataOnDemand.jJRightService;
    
    @Autowired
    JJRightRepository JJRightDataOnDemand.jJRightRepository;
    
    public JJRight JJRightDataOnDemand.getNewTransientJJRight(int index) {
        JJRight obj = new JJRight();
        setObjet(obj, index);
        setR(obj, index);
        setW(obj, index);
        setX(obj, index);
        return obj;
    }
    
    public void JJRightDataOnDemand.setObjet(JJRight obj, int index) {
        String objet = "objet_" + index;
        if (objet.length() > 25) {
            objet = objet.substring(0, 25);
        }
        obj.setObjet(objet);
    }
    
    public void JJRightDataOnDemand.setR(JJRight obj, int index) {
        Boolean r = Boolean.TRUE;
        obj.setR(r);
    }
    
    public void JJRightDataOnDemand.setW(JJRight obj, int index) {
        Boolean w = Boolean.TRUE;
        obj.setW(w);
    }
    
    public void JJRightDataOnDemand.setX(JJRight obj, int index) {
        Boolean x = Boolean.TRUE;
        obj.setX(x);
    }
    
    public JJRight JJRightDataOnDemand.getSpecificJJRight(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJRight obj = data.get(index);
        Long id = obj.getId();
        return jJRightService.findJJRight(id);
    }
    
    public JJRight JJRightDataOnDemand.getRandomJJRight() {
        init();
        JJRight obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJRightService.findJJRight(id);
    }
    
    public boolean JJRightDataOnDemand.modifyJJRight(JJRight obj) {
        return false;
    }
    
    public void JJRightDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJRightService.findJJRightEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJRight' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJRight>();
        for (int i = 0; i < 10; i++) {
            JJRight obj = getNewTransientJJRight(i);
            try {
                jJRightService.saveJJRight(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJRightRepository.flush();
            data.add(obj);
        }
    }
    
}