package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Lob;
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

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJProductSEQ")
public class JJProduct {
	
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

	@NotNull
	@Size(max = 100)
	private String extname;
	
	@Lob
	private byte[] logo;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
	private Set<JJVersion> versions = new HashSet<JJVersion>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
	private Set<JJMessage> messages = new HashSet<JJMessage>();

	@NotNull
	@ManyToOne
	private JJContact manager;

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJProduct) && (getId() != null) ? getId()
				.equals(((JJProduct) object).getId()) : (object == this);
	}
}
