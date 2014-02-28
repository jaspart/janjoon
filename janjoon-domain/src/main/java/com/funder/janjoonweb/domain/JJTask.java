package com.funder.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJTaskSEQ")
public class JJTask extends JJAbstractEntity {

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date startDatePlanned;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date endDatePlanned;

	private Integer workloadPlanned;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date startDateRevised;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date endDateRevised;

	private Integer workloadRevised;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date startDateReal;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date endDateReal;

	private Integer workloadReal;

	private Integer consumed;

	@ManyToOne
	private JJVersion versioning;

	@ManyToOne
	private JJBug bug;

	@ManyToOne
	private JJRequirement requirement;

	@ManyToOne
	private JJTestcase testcase;

	@ManyToOne
	private JJContact assignedTo;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JJMessage> messages = new HashSet<JJMessage>();

	private Boolean isCompleted;

}
