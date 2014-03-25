package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import com.starit.janjoonweb.domain.reference.JJRelationship;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJBugSEQ")
public class JJBug extends JJAbstractEntity {

	@ManyToOne
	private JJProject project;

	@ManyToOne
	private JJVersion versioning;

	@ManyToOne
	private JJCategory category;

	@ManyToOne
	private JJCriticity criticity;

	@ManyToOne
	private JJImportance importance;

	@ManyToOne
	private JJStatus status;

	@ManyToOne
	private JJRequirement requirement;

	@ManyToOne
	private JJTeststep teststep;

	@Enumerated
	private JJRelationship relation;

	@ManyToOne
	private JJSprint sprint;

	@ManyToOne
	private JJBuild build;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bugUp")
	private Set<JJBug> bugs = new HashSet<JJBug>();

	@ManyToOne
	private JJBug bugUp;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bug")
	private Set<JJTask> tasks = new HashSet<JJTask>();

	@ManyToOne
	private JJContact assignedTos;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bug")
	private Set<JJMessage> messages = new HashSet<JJMessage>();

}
