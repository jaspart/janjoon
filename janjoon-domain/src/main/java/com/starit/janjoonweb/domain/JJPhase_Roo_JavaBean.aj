// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPhase;
import java.util.Date;

privileged aspect JJPhase_Roo_JavaBean{

public String JJPhase.getName(){return this.name;}

public void JJPhase.setName(String name){this.name=name;}

public String JJPhase.getDescription(){return this.description;}

public void JJPhase.setDescription(String description){this.description=description;}

public Date JJPhase.getCreationDate(){return this.creationDate;}

public void JJPhase.setCreationDate(Date creationDate){this.creationDate=creationDate;}

public JJContact JJPhase.getCreatedBy(){return this.createdBy;}

public void JJPhase.setCreatedBy(JJContact createdBy){this.createdBy=createdBy;}

public Date JJPhase.getUpdatedDate(){return this.updatedDate;}

public void JJPhase.setUpdatedDate(Date updatedDate){this.updatedDate=updatedDate;}

public JJContact JJPhase.getUpdatedBy(){return this.updatedBy;}

public void JJPhase.setUpdatedBy(JJContact updatedBy){this.updatedBy=updatedBy;}

public Boolean JJPhase.getEnabled(){return this.enabled;}

public void JJPhase.setEnabled(Boolean enabled){this.enabled=enabled;}

public Integer JJPhase.getLevelPhase(){return this.levelPhase;}

public void JJPhase.setLevelPhase(Integer levelPhase){this.levelPhase=levelPhase;}

}
