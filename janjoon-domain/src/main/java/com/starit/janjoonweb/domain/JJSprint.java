package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJSprintSEQ")
public class JJSprint extends JJAbstractEntity {

	private Integer ordering;

	@ManyToOne
	private JJProject project;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JJBuild> builds = new HashSet<JJBuild>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JJTask> tasks = new HashSet<JJTask>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JJTask> obstacles = new HashSet<JJTask>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "JJSprintLinkJJContact", joinColumns = { @javax.persistence.JoinColumn(name = "Sprint_ID", referencedColumnName = "id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "Contact_ID", referencedColumnName = "id") })
	private Set<JJContact> contacts = new HashSet<JJContact>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sprint")
	private Set<JJMessage> messages = new HashSet<JJMessage>();
}
