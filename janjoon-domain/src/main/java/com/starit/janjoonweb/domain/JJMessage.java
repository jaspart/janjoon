package com.starit.janjoonweb.domain;

import java.util.Date;

import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJMessageSEQ")
public class JJMessage {
	
	@NotNull
	@Size(max = 100)
	private String name;

	@NotNull
	@Lob
	private String description;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date creationDate;

	@ManyToOne
	private JJContact createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date updatedDate;

	@ManyToOne
	private JJContact updatedBy;

	private Boolean enabled;

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
	private JJTestcase teststep;

	@ManyToOne
	private JJVersion versioning;

}
