// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJTestcase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect JJTestcase_Roo_Jpa_Entity {
    
    declare @type: JJTestcase: @Entity;
    
    @Id
    @SequenceGenerator(name = "jJTestcaseGen", sequenceName = "JJTestcaseSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "jJTestcaseGen")
    @Column(name = "id")
    private Long JJTestcase.id;
    
    @Version
    @Column(name = "version")
    private Integer JJTestcase.version;
    
    public Long JJTestcase.getId() {
        return this.id;
    }
    
    public void JJTestcase.setId(Long id) {
        this.id = id;
    }
    
    public Integer JJTestcase.getVersion() {
        return this.version;
    }
    
    public void JJTestcase.setVersion(Integer version) {
        this.version = version;
    }
    
}
