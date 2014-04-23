// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBugDataOnDemand;
import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJRequirementDataOnDemand;
import com.starit.janjoonweb.domain.JJSprintDataOnDemand;
import com.starit.janjoonweb.domain.JJStatusDataOnDemand;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskDataOnDemand;
import com.starit.janjoonweb.domain.JJTaskRepository;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcaseDataOnDemand;
import com.starit.janjoonweb.domain.JJVersionDataOnDemand;
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

privileged aspect JJTaskDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJTaskDataOnDemand: @Component;
    
    private Random JJTaskDataOnDemand.rnd = new SecureRandom();
    
    private List<JJTask> JJTaskDataOnDemand.data;
    
    @Autowired
    JJContactDataOnDemand JJTaskDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJBugDataOnDemand JJTaskDataOnDemand.jJBugDataOnDemand;
    
    @Autowired
    JJRequirementDataOnDemand JJTaskDataOnDemand.jJRequirementDataOnDemand;
    
    @Autowired
    JJSprintDataOnDemand JJTaskDataOnDemand.jJSprintDataOnDemand;
    
    @Autowired
    JJStatusDataOnDemand JJTaskDataOnDemand.jJStatusDataOnDemand;
    
    @Autowired
    JJTestcaseDataOnDemand JJTaskDataOnDemand.jJTestcaseDataOnDemand;
    
    @Autowired
    JJVersionDataOnDemand JJTaskDataOnDemand.jJVersionDataOnDemand;
    
    @Autowired
    JJTaskService JJTaskDataOnDemand.jJTaskService;
    
    @Autowired
    JJTaskRepository JJTaskDataOnDemand.jJTaskRepository;
    
    public JJTask JJTaskDataOnDemand.getNewTransientJJTask(int index) {
        JJTask obj = new JJTask();
        setCompleted(obj, index);
        setConsumed(obj, index);
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setEndDatePlanned(obj, index);
        setEndDateReal(obj, index);
        setEndDateRevised(obj, index);
        setName(obj, index);
        setStartDatePlanned(obj, index);
        setStartDateReal(obj, index);
        setStartDateRevised(obj, index);
        setUpdatedDate(obj, index);
        setWorkloadPlanned(obj, index);
        setWorkloadReal(obj, index);
        setWorkloadRevised(obj, index);
        return obj;
    }
    
    public void JJTaskDataOnDemand.setCompleted(JJTask obj, int index) {
        Boolean completed = Boolean.TRUE;
        obj.setCompleted(completed);
    }
    
    public void JJTaskDataOnDemand.setConsumed(JJTask obj, int index) {
        Integer consumed = new Integer(index);
        obj.setConsumed(consumed);
    }
    
    public void JJTaskDataOnDemand.setCreationDate(JJTask obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJTaskDataOnDemand.setDescription(JJTask obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void JJTaskDataOnDemand.setEnabled(JJTask obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJTaskDataOnDemand.setEndDatePlanned(JJTask obj, int index) {
        Date endDatePlanned = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setEndDatePlanned(endDatePlanned);
    }
    
    public void JJTaskDataOnDemand.setEndDateReal(JJTask obj, int index) {
        Date endDateReal = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setEndDateReal(endDateReal);
    }
    
    public void JJTaskDataOnDemand.setEndDateRevised(JJTask obj, int index) {
        Date endDateRevised = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setEndDateRevised(endDateRevised);
    }
    
    public void JJTaskDataOnDemand.setName(JJTask obj, int index) {
        String name = "name_" + index;
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }
        obj.setName(name);
    }
    
    public void JJTaskDataOnDemand.setStartDatePlanned(JJTask obj, int index) {
        Date startDatePlanned = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setStartDatePlanned(startDatePlanned);
    }
    
    public void JJTaskDataOnDemand.setStartDateReal(JJTask obj, int index) {
        Date startDateReal = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setStartDateReal(startDateReal);
    }
    
    public void JJTaskDataOnDemand.setStartDateRevised(JJTask obj, int index) {
        Date startDateRevised = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setStartDateRevised(startDateRevised);
    }
    
    public void JJTaskDataOnDemand.setUpdatedDate(JJTask obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public void JJTaskDataOnDemand.setWorkloadPlanned(JJTask obj, int index) {
        Integer workloadPlanned = new Integer(index);
        obj.setWorkloadPlanned(workloadPlanned);
    }
    
    public void JJTaskDataOnDemand.setWorkloadReal(JJTask obj, int index) {
        Integer workloadReal = new Integer(index);
        obj.setWorkloadReal(workloadReal);
    }
    
    public void JJTaskDataOnDemand.setWorkloadRevised(JJTask obj, int index) {
        Integer workloadRevised = new Integer(index);
        obj.setWorkloadRevised(workloadRevised);
    }
    
    public JJTask JJTaskDataOnDemand.getSpecificJJTask(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJTask obj = data.get(index);
        Long id = obj.getId();
        return jJTaskService.findJJTask(id);
    }
    
    public JJTask JJTaskDataOnDemand.getRandomJJTask() {
        init();
        JJTask obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJTaskService.findJJTask(id);
    }
    
    public boolean JJTaskDataOnDemand.modifyJJTask(JJTask obj) {
        return false;
    }
    
    public void JJTaskDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJTaskService.findJJTaskEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJTask' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJTask>();
        for (int i = 0; i < 10; i++) {
            JJTask obj = getNewTransientJJTask(i);
            try {
                jJTaskService.saveJJTask(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJTaskRepository.flush();
            data.add(obj);
        }
    }
    
}
