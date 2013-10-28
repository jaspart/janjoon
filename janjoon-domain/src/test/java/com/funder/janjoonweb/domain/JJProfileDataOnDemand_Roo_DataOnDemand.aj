// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJProfile;
import com.funder.janjoonweb.domain.JJProfileDataOnDemand;
import com.funder.janjoonweb.domain.JJProfileRepository;
import com.funder.janjoonweb.domain.JJProfileService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect JJProfileDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJProfileDataOnDemand: @Component;
    
    private Random JJProfileDataOnDemand.rnd = new SecureRandom();
    
    private List<JJProfile> JJProfileDataOnDemand.data;
    
    @Autowired
    JJProfileService JJProfileDataOnDemand.jJProfileService;
    
    @Autowired
    JJProfileRepository JJProfileDataOnDemand.jJProfileRepository;
    
    public JJProfile JJProfileDataOnDemand.getNewTransientJJProfile(int index) {
        JJProfile obj = new JJProfile();
        setName(obj, index);
        return obj;
    }
    
    public void JJProfileDataOnDemand.setName(JJProfile obj, int index) {
        String name = "name_" + index;
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        obj.setName(name);
    }
    
    public JJProfile JJProfileDataOnDemand.getSpecificJJProfile(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJProfile obj = data.get(index);
        Long id = obj.getId();
        return jJProfileService.findJJProfile(id);
    }
    
    public JJProfile JJProfileDataOnDemand.getRandomJJProfile() {
        init();
        JJProfile obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJProfileService.findJJProfile(id);
    }
    
    public boolean JJProfileDataOnDemand.modifyJJProfile(JJProfile obj) {
        return false;
    }
    
    public void JJProfileDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJProfileService.findJJProfileEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJProfile' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJProfile>();
        for (int i = 0; i < 10; i++) {
            JJProfile obj = getNewTransientJJProfile(i);
            try {
                jJProfileService.saveJJProfile(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJProfileRepository.flush();
            data.add(obj);
        }
    }
    
}
