package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@RooJpaEntity(sequenceName = "JJBugSEQ")
public class JJBug {
	
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
	
	@ManyToMany(mappedBy="bugs",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JJContact> contacts = new HashSet<JJContact>();

	@ManyToOne
	private JJContact assignedTos;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bug")
	private Set<JJMessage> messages = new HashSet<JJMessage>();
	
	public JJProduct getProduct()
	{
		if(this.getRequirement() != null && this.getRequirement().getProduct() != null)
			return this.getRequirement().getProduct();
		else if(this.getVersioning() != null)
			return this.getVersioning().getProduct();
		else if(this.getBuild() != null && this.getBuild().getVersion() != null)
			return this.getBuild().getVersion().getProduct();		
		else
			return null;
	}
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof JJBug) && (getId() != null) ? getId()
				.equals(((JJBug) object).getId()) : (object == this);
	}
	
	@Override
	public int hashCode() {
		if (this.getId() != null)
			return this.getId().hashCode();
		else
			return super.hashCode();
	}

}
