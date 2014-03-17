package com.starit.janjoonweb.domain;

import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJTestcaseexecutionSEQ")
public class JJTestcaseexecution extends JJAbstractEntity {

	@ManyToOne
	private JJBuild build;

	@ManyToOne
	private JJTestcase testcase;

	private Boolean passed;
}
