// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJCompanyDataOnDemand;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactDataOnDemand;
import com.starit.janjoonweb.domain.JJContactRepository;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJJobDataOnDemand;
import com.starit.janjoonweb.domain.JJProductDataOnDemand;
import com.starit.janjoonweb.domain.JJProjectDataOnDemand;
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

privileged aspect JJContactDataOnDemand_Roo_DataOnDemand {
    
    declare @type: JJContactDataOnDemand: @Component;
    
    private Random JJContactDataOnDemand.rnd = new SecureRandom();
    
    private List<JJContact> JJContactDataOnDemand.data;
    
    @Autowired
    JJCompanyDataOnDemand JJContactDataOnDemand.jJCompanyDataOnDemand;
    
    @Autowired
    JJJobDataOnDemand JJContactDataOnDemand.jJJobDataOnDemand;
    
    @Autowired
    JJProductDataOnDemand JJContactDataOnDemand.jJProductDataOnDemand;
    
    @Autowired
    JJProjectDataOnDemand JJContactDataOnDemand.jJProjectDataOnDemand;
    
    @Autowired
    JJVersionDataOnDemand JJContactDataOnDemand.jJVersionDataOnDemand;
    
    @Autowired
    JJContactService JJContactDataOnDemand.jJContactService;
    
    @Autowired
    JJContactRepository JJContactDataOnDemand.jJContactRepository;
    
    public JJContact JJContactDataOnDemand.getNewTransientJJContact(int index) {
        JJContact obj = new JJContact();
        setAccountNonExpired(obj, index);
        setAccountNonLocked(obj, index);
        setCalendar(obj, index);
        setCreatedBy(obj, index);
        setCreationDate(obj, index);
        setCredentialsNonExpired(obj, index);
        setDateofbirth(obj, index);
        setDescription(obj, index);
        setEmail(obj, index);
        setEnabled(obj, index);
        setFirstname(obj, index);
        setLdap(obj, index);
        setManager(obj, index);
        setName(obj, index);
        setPassword(obj, index);
        setPicture(obj, index);
        setPreference(obj, index);
        setUpdatedBy(obj, index);
        setUpdatedDate(obj, index);
        return obj;
    }
    
    public void JJContactDataOnDemand.setAccountNonExpired(JJContact obj, int index) {
        Boolean accountNonExpired = Boolean.TRUE;
        obj.setAccountNonExpired(accountNonExpired);
    }
    
    public void JJContactDataOnDemand.setAccountNonLocked(JJContact obj, int index) {
        Boolean accountNonLocked = Boolean.TRUE;
        obj.setAccountNonLocked(accountNonLocked);
    }
    
    public void JJContactDataOnDemand.setCalendar(JJContact obj, int index) {
        String calendar = "calendar_" + index;
        obj.setCalendar(calendar);
    }
    
    public void JJContactDataOnDemand.setCreatedBy(JJContact obj, int index) {
        JJContact createdBy = obj;
        obj.setCreatedBy(createdBy);
    }
    
    public void JJContactDataOnDemand.setCreationDate(JJContact obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void JJContactDataOnDemand.setCredentialsNonExpired(JJContact obj, int index) {
        Boolean credentialsNonExpired = Boolean.TRUE;
        obj.setCredentialsNonExpired(credentialsNonExpired);
    }
    
    public void JJContactDataOnDemand.setDateofbirth(JJContact obj, int index) {
        Date dateofbirth = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateofbirth(dateofbirth);
    }
    
    public void JJContactDataOnDemand.setDescription(JJContact obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void JJContactDataOnDemand.setEmail(JJContact obj, int index) {
        String email = "foo" + index + "@bar.com";
        obj.setEmail(email);
    }
    
    public void JJContactDataOnDemand.setEnabled(JJContact obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void JJContactDataOnDemand.setFirstname(JJContact obj, int index) {
        String firstname = "firstname_" + index;
        if (firstname.length() > 100) {
            firstname = firstname.substring(0, 100);
        }
        obj.setFirstname(firstname);
    }
    
    public void JJContactDataOnDemand.setLdap(JJContact obj, int index) {
        Integer ldap = new Integer(index);
        obj.setLdap(ldap);
    }
    
    public void JJContactDataOnDemand.setManager(JJContact obj, int index) {
        JJContact manager = obj;
        obj.setManager(manager);
    }
    
    public void JJContactDataOnDemand.setName(JJContact obj, int index) {
        String name = "name_" + index;
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }
        obj.setName(name);
    }
    
    public void JJContactDataOnDemand.setPassword(JJContact obj, int index) {
        String password = "password_" + index;
        if (password.length() > 35) {
            password = password.substring(0, 35);
        }
        obj.setPassword(password);
    }
    
    public void JJContactDataOnDemand.setPicture(JJContact obj, int index) {
        String picture = "picture_" + index;
        if (picture.length() > 25) {
            picture = picture.substring(0, 25);
        }
        obj.setPicture(picture);
    }
    
    public void JJContactDataOnDemand.setPreference(JJContact obj, int index) {
        String preference = "preference_" + index;
        obj.setPreference(preference);
    }
    
    public void JJContactDataOnDemand.setUpdatedBy(JJContact obj, int index) {
        JJContact updatedBy = obj;
        obj.setUpdatedBy(updatedBy);
    }
    
    public void JJContactDataOnDemand.setUpdatedDate(JJContact obj, int index) {
        Date updatedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdatedDate(updatedDate);
    }
    
    public JJContact JJContactDataOnDemand.getSpecificJJContact(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        JJContact obj = data.get(index);
        Long id = obj.getId();
        return jJContactService.findJJContact(id);
    }
    
    public JJContact JJContactDataOnDemand.getRandomJJContact() {
        init();
        JJContact obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return jJContactService.findJJContact(id);
    }
    
    public boolean JJContactDataOnDemand.modifyJJContact(JJContact obj) {
        return false;
    }
    
    public void JJContactDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = jJContactService.findJJContactEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'JJContact' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<JJContact>();
        for (int i = 0; i < 10; i++) {
            JJContact obj = getNewTransientJJContact(i);
            try {
                jJContactService.saveJJContact(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            jJContactRepository.flush();
            data.add(obj);
        }
    }
    
}
