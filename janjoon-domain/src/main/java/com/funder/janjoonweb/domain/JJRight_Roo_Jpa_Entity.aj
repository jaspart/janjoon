// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJRight;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect JJRight_Roo_Jpa_Entity {
    
    declare @type: JJRight: @Entity;
    
    @Id
    @SequenceGenerator(name = "jJRightGen", sequenceName = "JJRightSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "jJRightGen")
    @Column(name = "id")
    private Long JJRight.id;
    
    @Version
    @Column(name = "version")
    private Integer JJRight.version;
    
    public Long JJRight.getId() {
        return this.id;
    }
    
    public void JJRight.setId(Long id) {
        this.id = id;
    }
    
    public Integer JJRight.getVersion() {
        return this.version;
    }
    
    public void JJRight.setVersion(Integer version) {
        this.version = version;
    }
    
}
