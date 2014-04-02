package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testcaseexecution")
	private Set<JJTeststepexecution> teststepexecutions = new HashSet<JJTeststepexecution>();
}
