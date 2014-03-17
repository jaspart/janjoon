package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJTeststepSEQ")
public class JJTeststep extends JJAbstractEntity {

	private Integer ordering;

	@NotNull
	@Size(max = 100)
	private String action;

	@NotNull
	@Size(max = 100)
	private String resultat;

	private Boolean passed;

	@ManyToOne
	private JJTestcase testcase;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "teststep")
	private Set<JJMessage> messages = new HashSet<JJMessage>();
}
