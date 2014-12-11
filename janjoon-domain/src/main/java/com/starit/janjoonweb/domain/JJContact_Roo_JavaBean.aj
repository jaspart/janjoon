// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJJob;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJPermission;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJVersion;
import java.util.Date;
import java.util.Set;

privileged aspect JJContact_Roo_JavaBean {
    
    public String JJContact.getName() {
        return this.name;
    }
    
    public void JJContact.setName(String name) {
        this.name = name;
    }
    
    public String JJContact.getDescription() {
        return this.description;
    }
    
    public void JJContact.setDescription(String description) {
        this.description = description;
    }
    
    public Date JJContact.getCreationDate() {
        return this.creationDate;
    }
    
    public void JJContact.setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public JJContact JJContact.getCreatedBy() {
        return this.createdBy;
    }
    
    public void JJContact.setCreatedBy(JJContact createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date JJContact.getUpdatedDate() {
        return this.updatedDate;
    }
    
    public void JJContact.setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public JJContact JJContact.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void JJContact.setUpdatedBy(JJContact updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Boolean JJContact.getEnabled() {
        return this.enabled;
    }
    
    public void JJContact.setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public String JJContact.getPassword() {
        return this.password;
    }
    
    public void JJContact.setPassword(String password) {
        this.password = password;
    }
    
    public String JJContact.getEmail() {
        return this.email;
    }
    
    public void JJContact.setEmail(String email) {
        this.email = email;
    }
    
    public Integer JJContact.getLdap() {
        return this.ldap;
    }
    
    public void JJContact.setLdap(Integer ldap) {
        this.ldap = ldap;
    }
    
    public Integer JJContact.getPriceSold() {
        return this.priceSold;
    }
    
    public void JJContact.setPriceSold(Integer priceSold) {
        this.priceSold = priceSold;
    }
    
    public Integer JJContact.getPriceReal() {
        return this.priceReal;
    }
    
    public void JJContact.setPriceReal(Integer priceReal) {
        this.priceReal = priceReal;
    }
    
    public String JJContact.getCurrency() {
        return this.currency;
    }
    
    public void JJContact.setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String JJContact.getFirstname() {
        return this.firstname;
    }
    
    public void JJContact.setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public Date JJContact.getDateofbirth() {
        return this.dateofbirth;
    }
    
    public void JJContact.setDateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }
    
    public String JJContact.getPicture() {
        return this.picture;
    }
    
    public void JJContact.setPicture(String picture) {
        this.picture = picture;
    }
    
    public JJJob JJContact.getJob() {
        return this.job;
    }
    
    public void JJContact.setJob(JJJob job) {
        this.job = job;
    }
    
    public Boolean JJContact.getAccountNonExpired() {
        return this.accountNonExpired;
    }
    
    public void JJContact.setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }
    
    public Boolean JJContact.getCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }
    
    public void JJContact.setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
    
    public Boolean JJContact.getAccountNonLocked() {
        return this.accountNonLocked;
    }
    
    public void JJContact.setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    
    public JJProject JJContact.getLastProject() {
        return this.lastProject;
    }
    
    public void JJContact.setLastProject(JJProject lastProject) {
        this.lastProject = lastProject;
    }
    
    public JJProduct JJContact.getLastProduct() {
        return this.lastProduct;
    }
    
    public void JJContact.setLastProduct(JJProduct lastProduct) {
        this.lastProduct = lastProduct;
    }
    
    public JJVersion JJContact.getLastVersion() {
        return this.lastVersion;
    }
    
    public void JJContact.setLastVersion(JJVersion lastVersion) {
        this.lastVersion = lastVersion;
    }
    
    public JJContact JJContact.getManager() {
        return this.manager;
    }
    
    public void JJContact.setManager(JJContact manager) {
        this.manager = manager;
    }
    
    public JJCompany JJContact.getCompany() {
        return this.company;
    }
    
    public void JJContact.setCompany(JJCompany company) {
        this.company = company;
    }
    
    public String JJContact.getCalendar() {
        return this.calendar;
    }
    
    public void JJContact.setCalendar(String calendar) {
        this.calendar = calendar;
    }
    
    public String JJContact.getPreference() {
        return this.preference;
    }
    
    public void JJContact.setPreference(String preference) {
        this.preference = preference;
    }
    
    public Set<JJSprint> JJContact.getSprints() {
        return this.sprints;
    }
    
    public void JJContact.setSprints(Set<JJSprint> sprints) {
        this.sprints = sprints;
    }
    
    public Set<JJCategory> JJContact.getCategories() {
        return this.categories;
    }
    
    public void JJContact.setCategories(Set<JJCategory> categories) {
        this.categories = categories;
    }
    
    public Set<JJPermission> JJContact.getPermissions() {
        return this.permissions;
    }
    
    public void JJContact.setPermissions(Set<JJPermission> permissions) {
        this.permissions = permissions;
    }
    
    public Set<JJMessage> JJContact.getMessages() {
        return this.messages;
    }
    
    public void JJContact.setMessages(Set<JJMessage> messages) {
        this.messages = messages;
    }
    
}
