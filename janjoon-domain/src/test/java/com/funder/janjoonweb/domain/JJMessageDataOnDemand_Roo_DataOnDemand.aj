// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJBuildDataOnDemand;
import com.funder.janjoonweb.domain.JJContactDataOnDemand;
import com.funder.janjoonweb.domain.JJCriticityDataOnDemand;
import com.funder.janjoonweb.domain.JJImportanceDataOnDemand;
import com.funder.janjoonweb.domain.JJMessage;
import com.funder.janjoonweb.domain.JJMessageDataOnDemand;
import com.funder.janjoonweb.domain.JJMessageRepository;
import com.funder.janjoonweb.domain.JJMessageService;
import com.funder.janjoonweb.domain.JJSprintDataOnDemand;
import com.funder.janjoonweb.domain.JJStatusDataOnDemand;
import com.funder.janjoonweb.domain.JJTestcaseDataOnDemand;
import com.funder.janjoonweb.domain.JJTeststepDataOnDemand;
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

privileged aspect JJMessageDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJMessageDataOnDemand: @Component;
    
    private Random JJMessageDataOnDemand.rnd = new SecureRandom();
    
    private List<JJMessage> JJMessageDataOnDemand.data;
    
    @Autowired
    JJBuildDataOnDemand JJMessageDataOnDemand.jJBuildDataOnDemand;
    
    @Autowired
    JJContactDataOnDemand JJMessageDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJCriticityDataOnDemand JJMessageDataOnDemand.jJCriticityDataOnDemand;
    
    @Autowired
    JJImportanceDataOnDemand JJMessageDataOnDemand.jJImportanceDataOnDemand;
    
    @Autowired
    JJSprintDataOnDemand JJMessageDataOnDemand.jJSprintDataOnDemand;
    
    @Autowired
    JJStatusDataOnDemand JJMessageDataOnDemand.jJStatusDataOnDemand;
    
    @Autowired
    JJTestcaseDataOnDemand JJMessageDataOnDemand.jJTestcaseDataOnDemand;
    
    @Autowired
    JJTeststepDataOnDemand JJMessageDataOnDemand.jJTeststepDataOnDemand;
    
    @Autowired
    JJMessageService JJMessageDataOnDemand.jJMessageService;
    
    @Autowired
    JJMessageRepository JJMessageDataOnDemand.jJMessageRepository;
    
    public JJMessage JJMessageDataOnDemand.getNewTransientJJMessage(int index) {
        JJMessage obj = new JJMessage();
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setMessage(obj, index);
        setName(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJMessageDataOnDemand.setCreationDate(JJMessage obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJMessageDataOnDemand.setDescription(JJMessage obj, int index) {
        String description = "description_" + index;
        if (description.length() > 500) {
            description = description.substring(0, 500);
        }
        obj.setDescription(description);
    }
    
    public void JJMessageDataOnDemand.setEnabled(JJMessage obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJMessageDataOnDemand.setMessage(JJMessage obj, int index) {
        String message = "message_" + index;
        if (message.length() > 250) {
            message = message.substring(0, 250);
        }
        obj.setMessage(message);
    }
    
    public void JJMessageDataOnDemand.setName(JJMessage obj, int index) {
        String name = "name_" + index;
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        obj.setName(name);
    }
    
    public void JJMessageDataOnDemand.setUpdatedDate(JJMessage obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJMessage JJMessageDataOnDemand.getSpecificJJMessage(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJMessage obj = data.get(index);
        Long id = obj.getId();
        return jJMessageService.findJJMessage(id);
    }
    
    public JJMessage JJMessageDataOnDemand.getRandomJJMessage() {
        init();
        JJMessage obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJMessageService.findJJMessage(id);
    }
    
    public boolean JJMessageDataOnDemand.modifyJJMessage(JJMessage obj) {
        return false;
    }
    
    public void JJMessageDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJMessageService.findJJMessageEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJMessage' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJMessage>();
        for (int i = 0; i < 10; i++) {
            JJMessage obj = getNewTransientJJMessage(i);
            try {
                jJMessageService.saveJJMessage(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJMessageRepository.flush();
            data.add(obj);
        }
    }
    
}
