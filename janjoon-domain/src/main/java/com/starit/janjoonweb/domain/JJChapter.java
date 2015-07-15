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
@RooJpaEntity(sequenceName = "JJChapterSEQ")
@Table(indexes = { @Index(unique = false, columnList = "project,category") })
public class JJChapter {

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
	private JJProject project;

	@ManyToOne
	private JJCategory category;

	@ManyToOne
	private JJChapter parent;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<JJChapter> chapters = new HashSet<JJChapter>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chapter")
	private Set<JJRequirement> requirements = new HashSet<JJRequirement>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chapter")
	private Set<JJMessage> messages = new HashSet<JJMessage>();

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJChapter) && (getId() != null) ? getId()
				.equals(((JJChapter) object).getId()) : (object == this);
	}

}
