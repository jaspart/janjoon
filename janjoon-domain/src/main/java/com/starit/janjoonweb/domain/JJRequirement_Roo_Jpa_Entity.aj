// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJRequirement;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect JJRequirement_Roo_Jpa_Entity {
    
    declare @type: JJRequirement: @Entity;
    
    @Id
    @SequenceGenerator(name = "jJRequirementGen", sequenceName = "JJRequirementSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "jJRequirementGen")
    @Column(name = "id")
    private Long JJRequirement.id;
    
    @Version
    @Column(name = "version")
    private Integer JJRequirement.version;
    
    public Long JJRequirement.getId() {
        return this.id;
    }
    
    public void JJRequirement.setId(Long id) {
        this.id = id;
    }
    
    public Integer JJRequirement.getVersion() {
        return this.version;
    }
    
    public void JJRequirement.setVersion(Integer version) {
        this.version = version;
    }
    
}
