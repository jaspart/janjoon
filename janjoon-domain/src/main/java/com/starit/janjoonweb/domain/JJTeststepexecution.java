package com.starit.janjoonweb.domain;

import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJTeststepexecutionSEQ")
public class JJTeststepexecution extends JJAbstractEntity {

	@ManyToOne
	private JJBuild build;

	@ManyToOne
	private JJTeststep teststep;

	private Boolean passed;
}
