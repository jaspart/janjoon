package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Lob;
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
@RooJpaEntity(sequenceName = "JJTeststepSEQ")
@Table(indexes = { @Index(unique = true, columnList = "testcase") })
public class JJTeststep {

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
	@Lob
	private String actionstep;

	@NotNull
	@Lob
	private String resultstep;

	@ManyToOne
	private JJTestcase testcase;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "teststep")
	private Set<JJBug> bugs = new HashSet<JJBug>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "teststep")
	private Set<JJMessage> messages = new HashSet<JJMessage>();

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJTeststep) && (getId() != null) ? getId()
				.equals(((JJTeststep) object).getId()) : (object == this);
	}
}
