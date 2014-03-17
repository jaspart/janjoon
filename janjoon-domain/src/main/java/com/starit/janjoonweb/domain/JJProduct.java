package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJProductSEQ")
public class JJProduct extends JJAbstractEntity {

	@NotNull
	@Size(max = 100)
	private String extname;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "product")
	private Set<JJVersion> versions = new HashSet<JJVersion>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
	private Set<JJChapter> chapters = new HashSet<JJChapter>();

	@NotNull
	@ManyToOne
	private JJContact manager;

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJProduct) && (getId() != null) ? getId()
				.equals(((JJProduct) object).getId()) : (object == this);
	}
}
