// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJImputation;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect JJImputation_Roo_Jpa_Entity {
    
    declare @type: JJImputation: @Entity;
    
    @Id
    @SequenceGenerator(name = "jJImputationGen", sequenceName = "JJImputationSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "jJImputationGen")
    @Column(name = "id")
    private Long JJImputation.id;
    
    @Version
    @Column(name = "version")
    private Integer JJImputation.version;
    
    public Long JJImputation.getId() {
        return this.id;
    }
    
    public void JJImputation.setId(Long id) {
        this.id = id;
    }
    
    public Integer JJImputation.getVersion() {
        return this.version;
    }
    
    public void JJImputation.setVersion(Integer version) {
        this.version = version;
    }
    
}