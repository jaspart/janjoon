// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBuild;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect JJBuild_Roo_Jpa_Entity {
    
    declare @type: JJBuild: @Entity;
    
    @Id
    @SequenceGenerator(name = "jJBuildGen", sequenceName = "JJBuildSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "jJBuildGen")
    @Column(name = "id")
    private Long JJBuild.id;
    
    @Version
    @Column(name = "version")
    private Integer JJBuild.version;
    
    public Long JJBuild.getId() {
        return this.id;
    }
    
    public void JJBuild.setId(Long id) {
        this.id = id;
    }
    
    public Integer JJBuild.getVersion() {
        return this.version;
    }
    
    public void JJBuild.setVersion(Integer version) {
        this.version = version;
    }
    
}
