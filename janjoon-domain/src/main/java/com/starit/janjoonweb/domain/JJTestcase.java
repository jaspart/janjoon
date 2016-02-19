package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@RooJpaEntity(sequenceName = "JJTestcaseSEQ")
@Table(indexes = {@Index(unique = true, columnList = "requirement")})
public class JJTestcase {

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

	private Integer ordering;

	@NotNull
	@ManyToOne
	private JJRequirement requirement;

	private Boolean allBuilds;

	@ManyToOne
	private JJSprint sprint;

	@ManyToOne
	private JJStatus status;

	private Integer workload;

	private Integer pricepoint;

	private Boolean automatic;

	private Boolean regression;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "JJBuildLinkJJTestcase", joinColumns = {
			@javax.persistence.JoinColumn(name = "Testcase_ID", referencedColumnName = "id")}, inverseJoinColumns = {
					@javax.persistence.JoinColumn(name = "Build_ID", referencedColumnName = "id")})
	private Set<JJBuild> builds = new HashSet<JJBuild>();

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

	@ManyToMany(mappedBy = "testcases", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JJContact> contacts = new HashSet<JJContact>();

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJTestcase) && (getId() != null)
				? getId().equals(((JJTestcase) object).getId())
				: (object == this);
	}

	@Override
	public int hashCode() {
		if (this.getId() != null)
			return this.getId().hashCode();
		else
			return super.hashCode();
	}

}
