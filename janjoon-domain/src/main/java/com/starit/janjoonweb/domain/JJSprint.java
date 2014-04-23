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
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJSprintSEQ")
public class JJSprint extends JJAbstractEntity {

	private Integer ordering;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date startDate;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date endDate;

	@ManyToOne
	private JJProject project;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JJBuild> builds = new HashSet<JJBuild>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy = "sprint")
	private Set<JJTask> tasks = new HashSet<JJTask>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JJTask> obstacles = new HashSet<JJTask>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "JJSprintLinkJJContact", joinColumns = { @javax.persistence.JoinColumn(name = "Sprint_ID", referencedColumnName = "id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "Contact_ID", referencedColumnName = "id") })
	private Set<JJContact> contacts = new HashSet<JJContact>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sprint")
	private Set<JJMessage> messages = new HashSet<JJMessage>();
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof JJSprint) && (getId() != null) ? getId()
				.equals(((JJSprint) object).getId()) : (object == this);
	}
}
