// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJAuditLogDataOnDemand;
import com.starit.janjoonweb.domain.JJAuditLogRepository;
import com.starit.janjoonweb.domain.JJAuditLogService;
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

privileged aspect JJAuditLogDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJAuditLogDataOnDemand: @Component;
    
    private Random JJAuditLogDataOnDemand.rnd = new SecureRandom();
    
    private List<JJAuditLog> JJAuditLogDataOnDemand.data;
    
    @Autowired
    JJContactDataOnDemand JJAuditLogDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJAuditLogService JJAuditLogDataOnDemand.jJAuditLogService;
    
    @Autowired
    JJAuditLogRepository JJAuditLogDataOnDemand.jJAuditLogRepository;
    
    public JJAuditLog JJAuditLogDataOnDemand.getNewTransientJJAuditLog(int index) {
        JJAuditLog obj = new JJAuditLog();
        setAuditLogDate(obj, index);
        setKeyName(obj, index);
        setKeyValue(obj, index);
        setObjet(obj, index);
        return obj;
    }
    
    public void JJAuditLogDataOnDemand.setAuditLogDate(JJAuditLog obj, int index) {
        Date auditLogDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setAuditLogDate(auditLogDate);
    }
    
    public void JJAuditLogDataOnDemand.setKeyName(JJAuditLog obj, int index) {
        String keyName = "keyName_" + index;
        if (keyName.length() > 50) {
            keyName = keyName.substring(0, 50);
        }
        obj.setKeyName(keyName);
    }
    
    public void JJAuditLogDataOnDemand.setKeyValue(JJAuditLog obj, int index) {
        String keyValue = "keyValue_" + index;
        obj.setKeyValue(keyValue);
    }
    
    public void JJAuditLogDataOnDemand.setObjet(JJAuditLog obj, int index) {
        String objet = "objet_" + index;
        obj.setObjet(objet);
    }
    
    public JJAuditLog JJAuditLogDataOnDemand.getSpecificJJAuditLog(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJAuditLog obj = data.get(index);
        Long id = obj.getId();
        return jJAuditLogService.findJJAuditLog(id);
    }
    
    public JJAuditLog JJAuditLogDataOnDemand.getRandomJJAuditLog() {
        init();
        JJAuditLog obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJAuditLogService.findJJAuditLog(id);
    }
    
    public boolean JJAuditLogDataOnDemand.modifyJJAuditLog(JJAuditLog obj) {
        return false;
    }
    
    public void JJAuditLogDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJAuditLogService.findJJAuditLogEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJAuditLog' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJAuditLog>();
        for (int i = 0; i < 10; i++) {
            JJAuditLog obj = getNewTransientJJAuditLog(i);
            try {
                jJAuditLogService.saveJJAuditLog(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJAuditLogRepository.flush();
            data.add(obj);
        }
    }
    
}
