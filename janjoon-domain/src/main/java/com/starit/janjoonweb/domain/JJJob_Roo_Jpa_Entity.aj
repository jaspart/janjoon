// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJJob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect JJJob_Roo_Jpa_Entity {
    
    declare @type: JJJob: @Entity;
    
    @Id
    @SequenceGenerator(name = "jJJobGen", sequenceName = "JJJobSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "jJJobGen")
    @Column(name = "id")
    private Long JJJob.id;
    
    @Version
    @Column(name = "version")
    private Integer JJJob.version;
    
    public Long JJJob.getId() {
        return this.id;
    }
    
    public void JJJob.setId(Long id) {
        this.id = id;
    }
    
    public Integer JJJob.getVersion() {
        return this.version;
    }
    
    public void JJJob.setVersion(Integer version) {
        this.version = version;
    }
    
}
