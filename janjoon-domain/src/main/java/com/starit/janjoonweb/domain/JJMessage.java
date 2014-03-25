package com.starit.janjoonweb.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJMessageSEQ")
public class JJMessage extends JJAbstractEntity {

	@NotNull
	@Size(max = 250)
	private String message;

	@ManyToOne
	private JJBug bug;

	@ManyToOne
	private JJBuild build;

	@ManyToOne
	private JJChapter chapter;

	@ManyToOne
	private JJContact contact;

	@ManyToOne
	private JJCriticity criticity;

	@ManyToOne
	private JJImportance importance;

	@ManyToOne
	private JJProduct product;

	@ManyToOne
	private JJProject project;

	@ManyToOne
	private JJRequirement requirement;

	@ManyToOne
	private JJSprint sprint;

	@ManyToOne
	private JJStatus status;

	@ManyToOne
	private JJTask task;

	@ManyToOne
	private JJTestcase testcase;

	@ManyToOne
	private JJVersion versioning;

}
