package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJStatusSEQ")
public class JJStatus extends JJAbstractEntity {

	@NotNull
	@Size(max = 25)
	private String objet;

	private Integer levelStatus;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "status")
	private Set<JJMessage> messages = new HashSet<JJMessage>();
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof JJStatus) && (getId() != null) ? getId()
				.equals(((JJStatus) object).getId()) : (object == this);
	}
}
