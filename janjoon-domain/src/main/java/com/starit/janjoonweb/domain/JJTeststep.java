package com.starit.janjoonweb.domain;

import javax.persistence.ManyToOne;
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
	private String actionstep;

	@NotNull
	@Size(max = 100)
	private String resultstep;

	private Boolean passed;

	@ManyToOne
	private JJTestcase testcase;
}
