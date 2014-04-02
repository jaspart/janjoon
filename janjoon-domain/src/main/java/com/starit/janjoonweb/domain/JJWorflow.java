package com.starit.janjoonweb.domain;

import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJWorflowSEQ")
public class JJWorflow extends JJAbstractEntity {

	@ManyToOne
	private JJStatus source;

	@ManyToOne
	private JJStatus target;

	private String actionworkflow;

	@ManyToOne
	private JJContact actor;
}
