package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJProjectSEQ")
public class JJProject extends JJAbstractEntity {

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project")
	private Set<JJChapter> chapters = new HashSet<JJChapter>();

	@NotNull
	@ManyToOne
	private JJContact manager;
	
	@Override
	public boolean equals(Object object) {
        return (object instanceof JJProject) && (getId() != null) 
             ? getId().equals(((JJProject) object).getId()) 
             : (object == this);
    }
}
