package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date soldDate;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date warrantyDate;

	private Integer workloadSold;
	private Integer workloadRisk;
	private Integer workloadWarranty;

	private Integer consumed;

	@ManyToOne
	private JJVersion versioning;

	@ManyToOne
	private JJBuild build;

	@ManyToOne
	private JJBug bug;

	@ManyToOne
	private JJRequirement requirement;

	@ManyToOne
	private JJTestcase testcase;

	@ManyToOne
	private JJSprint sprint;

	@ManyToOne
	private JJContact assignedTo;

	@ManyToOne
	private JJStatus status;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "task")
	private Set<JJMessage> messages = new HashSet<JJMessage>();

	private Boolean completed;

	@ManyToOne
	private JJTask parent;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "parent")
	private Set<JJTask> tasks = new HashSet<JJTask>();

	@ManyToMany(mappedBy = "beforeTasks", fetch = FetchType.EAGER)
	private Set<JJTask> afterTasks = new HashSet<JJTask>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "JJTaskLink", joinColumns = { @javax.persistence.JoinColumn(name = "AfterTask_ID", referencedColumnName = "id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "BeforeTask_ID", referencedColumnName = "id") })
	private Set<JJTask> beforeTasks = new HashSet<JJTask>();

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJTask) && (getId() != null) ? getId()
				.equals(((JJTask) object).getId()) : (object == this);
	}

}
