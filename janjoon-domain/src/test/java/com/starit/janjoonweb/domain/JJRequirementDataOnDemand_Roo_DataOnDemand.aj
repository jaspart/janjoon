// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJCategoryDataOnDemand;
import com.starit.janjoonweb.domain.JJChapterDataOnDemand;
import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJCriticityDataOnDemand;
import com.starit.janjoonweb.domain.JJFlowStepDataOnDemand;
import com.starit.janjoonweb.domain.JJImportanceDataOnDemand;
import com.starit.janjoonweb.domain.JJProductDataOnDemand;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJProjectDataOnDemand;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementDataOnDemand;
import com.starit.janjoonweb.domain.JJRequirementRepository;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJSprintDataOnDemand;
import com.starit.janjoonweb.domain.JJStatusDataOnDemand;
import com.starit.janjoonweb.domain.JJVersionDataOnDemand;
import com.starit.janjoonweb.domain.reference.JJRelationship;
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

privileged aspect JJRequirementDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJRequirementDataOnDemand: @Component;
    
    private Random JJRequirementDataOnDemand.rnd = new SecureRandom();
    
    private List<JJRequirement> JJRequirementDataOnDemand.data;
    
    @Autowired
    JJContactDataOnDemand JJRequirementDataOnDemand.jJContactDataOnDemand;
    
    @Autowired
    JJCategoryDataOnDemand JJRequirementDataOnDemand.jJCategoryDataOnDemand;
    
    @Autowired
    JJChapterDataOnDemand JJRequirementDataOnDemand.jJChapterDataOnDemand;
    
    @Autowired
    JJCriticityDataOnDemand JJRequirementDataOnDemand.jJCriticityDataOnDemand;
    
    @Autowired
    JJFlowStepDataOnDemand JJRequirementDataOnDemand.jJFlowStepDataOnDemand;
    
    @Autowired
    JJImportanceDataOnDemand JJRequirementDataOnDemand.jJImportanceDataOnDemand;
    
    @Autowired
    JJProductDataOnDemand JJRequirementDataOnDemand.jJProductDataOnDemand;
    
    @Autowired
    JJProjectDataOnDemand JJRequirementDataOnDemand.jJProjectDataOnDemand;
    
    @Autowired
    JJSprintDataOnDemand JJRequirementDataOnDemand.jJSprintDataOnDemand;
    
    @Autowired
    JJStatusDataOnDemand JJRequirementDataOnDemand.jJStatusDataOnDemand;
    
    @Autowired
    JJVersionDataOnDemand JJRequirementDataOnDemand.jJVersionDataOnDemand;
    
    @Autowired
    JJRequirementService JJRequirementDataOnDemand.jJRequirementService;
    
    @Autowired
    JJRequirementRepository JJRequirementDataOnDemand.jJRequirementRepository;
    
    public JJRequirement JJRequirementDataOnDemand.getNewTransientJJRequirement(int index) {
        JJRequirement obj = new JJRequirement();
        setCompletion(obj, index);
        setCreationDate(obj, index);
        setDescription(obj, index);
        setEnabled(obj, index);
        setEndDate(obj, index);
        setFinalState(obj, index);
        setImpact(obj, index);
        setName(obj, index);
        setNote(obj, index);
        setNumero(obj, index);
        setOperation(obj, index);
        setOrdering(obj, index);
        setProject(obj, index);
        setRelation(obj, index);
        setStartDate(obj, index);
        setUpdatedDate(obj, index);
        setWorkload(obj, index);
        return obj;
    }
    
    public void JJRequirementDataOnDemand.setCompletion(JJRequirement obj, int index) {
        Boolean completion = Boolean.TRUE;
        obj.setCompletion(completion);
    }
    
    public void JJRequirementDataOnDemand.setCreationDate(JJRequirement obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJRequirementDataOnDemand.setDescription(JJRequirement obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void JJRequirementDataOnDemand.setEnabled(JJRequirement obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJRequirementDataOnDemand.setEndDate(JJRequirement obj, int index) {
        Date endDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setEndDate(endDate);
    }
    
    public void JJRequirementDataOnDemand.setFinalState(JJRequirement obj, int index) {
        Boolean finalState = Boolean.TRUE;
        obj.setFinalState(finalState);
    }
    
    public void JJRequirementDataOnDemand.setImpact(JJRequirement obj, int index) {
        String impact = "impact_" + index;
        if (impact.length() > 100) {
            impact = impact.substring(0, 100);
        }
        obj.setImpact(impact);
    }
    
    public void JJRequirementDataOnDemand.setName(JJRequirement obj, int index) {
        String name = "name_" + index;
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }
        obj.setName(name);
    }
    
    public void JJRequirementDataOnDemand.setNote(JJRequirement obj, int index) {
        String note = "note_" + index;
        obj.setNote(note);
    }
    
    public void JJRequirementDataOnDemand.setNumero(JJRequirement obj, int index) {
        Integer numero = new Integer(index);
        obj.setNumero(numero);
    }
    
    public void JJRequirementDataOnDemand.setOperation(JJRequirement obj, int index) {
        Boolean operation = Boolean.TRUE;
        obj.setOperation(operation);
    }
    
    public void JJRequirementDataOnDemand.setOrdering(JJRequirement obj, int index) {
        Integer ordering = new Integer(index);
        obj.setOrdering(ordering);
    }
    
    public void JJRequirementDataOnDemand.setProject(JJRequirement obj, int index) {
        JJProject project = jJProjectDataOnDemand.getRandomJJProject();
        obj.setProject(project);
    }
    
    public void JJRequirementDataOnDemand.setRelation(JJRequirement obj, int index) {
        JJRelationship relation = JJRelationship.class.getEnumConstants()[0];
        obj.setRelation(relation);
    }
    
    public void JJRequirementDataOnDemand.setStartDate(JJRequirement obj, int index) {
        Date startDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setStartDate(startDate);
    }
    
    public void JJRequirementDataOnDemand.setUpdatedDate(JJRequirement obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public void JJRequirementDataOnDemand.setWorkload(JJRequirement obj, int index) {
        Integer workload = new Integer(index);
        obj.setWorkload(workload);
    }
    
    public JJRequirement JJRequirementDataOnDemand.getSpecificJJRequirement(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJRequirement obj = data.get(index);
        Long id = obj.getId();
        return jJRequirementService.findJJRequirement(id);
    }
    
    public JJRequirement JJRequirementDataOnDemand.getRandomJJRequirement() {
        init();
        JJRequirement obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJRequirementService.findJJRequirement(id);
    }
    
    public boolean JJRequirementDataOnDemand.modifyJJRequirement(JJRequirement obj) {
        return false;
    }
    
    public void JJRequirementDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJRequirementService.findJJRequirementEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJRequirement' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJRequirement>();
        for (int i = 0; i < 10; i++) {
            JJRequirement obj = getNewTransientJJRequirement(i);
            try {
                jJRequirementService.saveJJRequirement(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJRequirementRepository.flush();
            data.add(obj);
        }
    }
    
}
