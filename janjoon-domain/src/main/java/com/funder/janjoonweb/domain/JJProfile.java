package com.funder.janjoonweb.domain;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJProfileSEQ")
public class JJProfile {

    /**
     */
	@JoinTable(name = "JJRightJJProfile", joinColumns = { @javax.persistence.JoinColumn(name = "Right_ID", referencedColumnName = "id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "Profile_ID", referencedColumnName = "id") })
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<JJRight> rights = new HashSet<JJRight>();
}
