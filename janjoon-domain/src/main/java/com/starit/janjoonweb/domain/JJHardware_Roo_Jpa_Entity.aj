// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJHardware;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

privileged aspect JJHardware_Roo_Jpa_Entity {
    
    declare @type: JJHardware: @Entity;
    
    @Id
    @SequenceGenerator(name = "jJHardwareGen", sequenceName = "JJHardwareSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "jJHardwareGen")
    @Column(name = "id")
    private Long JJHardware.id;
    
    @Version
    @Column(name = "version")
    private Integer JJHardware.version;
    
    public Long JJHardware.getId() {
        return this.id;
    }
    
    public void JJHardware.setId(Long id) {
        this.id = id;
    }
    
    public Integer JJHardware.getVersion() {
        return this.version;
    }
    
    public void JJHardware.setVersion(Integer version) {
        this.version = version;
    }
    
}
