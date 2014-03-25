package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJTestcaseSEQ")
public class JJTestcase extends JJAbstractEntity {

	private Integer ordering;

	@NotNull
	@ManyToOne
	private JJRequirement requirement;

	@ManyToOne
	private JJSprint sprint;

	private Integer workload;

	private Integer pricepoint;

	private boolean automatic;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testcase")
	private Set<JJSoftware> softwares = new HashSet<JJSoftware>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testcase")
	private Set<JJHardware> hardwares = new HashSet<JJHardware>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testcase")
	private Set<JJTeststep> teststeps = new HashSet<JJTeststep>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testcase")
	private Set<JJTask> tasks = new HashSet<JJTask>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testcase")
	private Set<JJMessage> messages = new HashSet<JJMessage>();

}
