package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

import com.starit.janjoonweb.domain.reference.JJRelationship;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJRequirementSEQ")
public class JJRequirement {
	
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

	private Integer numero;

	@NotNull
	@ManyToOne
	private JJProject project;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date endDate;

	@ManyToOne
	private JJChapter chapter;

	@ManyToOne
	private JJVersion versioning;

	@ManyToOne
	private JJProduct product;

	@ManyToOne
	private JJCategory category;

	@ManyToOne
	private JJCriticity criticity;

	@ManyToOne
	private JJImportance importance;

	@ManyToOne
	private JJStatus status;

	@ManyToOne
	private JJSprint sprint;

	@Size(max = 100)
	private String impact;

	@Lob
	private String note;

	private Boolean operation;

	private Boolean finalState;

	private Boolean completion;

	@ManyToOne
	private JJContact assignedTo;

	@Enumerated
	private JJRelationship relation;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requirement")
	private Set<JJBug> bugs = new HashSet<JJBug>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requirement")
	private Set<JJTask> tasks = new HashSet<JJTask>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requirement")
	private Set<JJMessage> messages = new HashSet<JJMessage>();

	@ManyToMany(mappedBy = "requirementLinkUp", fetch = FetchType.LAZY)
	private Set<JJRequirement> requirementLinkDown = new HashSet<JJRequirement>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "JJRequirementLink", joinColumns = { @javax.persistence.JoinColumn(name = "ReqLinkDOWN_ID", referencedColumnName = "id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "ReqLinkUP_ID", referencedColumnName = "id") })
	private Set<JJRequirement> requirementLinkUp = new HashSet<JJRequirement>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requirement")
	private Set<JJTestcase> testcases = new HashSet<JJTestcase>();
	
	@ManyToMany(mappedBy="requirements",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JJContact> contacts = new HashSet<JJContact>();

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJRequirement) && (getId() != null) ? getId()
				.equals(((JJRequirement) object).getId()) : (object == this);
	}
	
	@Override
	public int hashCode() {
		if (this.getId() != null)
			return this.getId().hashCode();
		else
			return super.hashCode();
	}

}
