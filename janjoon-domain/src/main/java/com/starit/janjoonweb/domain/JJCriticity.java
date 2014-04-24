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
@RooJpaEntity(sequenceName = "JJCriticitySEQ")
public class JJCriticity extends JJAbstractEntity {

	@NotNull
	@Size(max = 25)
	private String objet;

	private Integer levelCriticity;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "criticity")
	private Set<JJMessage> messages = new HashSet<JJMessage>();
}
