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
@RooJpaEntity(sequenceName = "JJChapterSEQ")
public class JJChapter extends JJAbstractEntity {

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
