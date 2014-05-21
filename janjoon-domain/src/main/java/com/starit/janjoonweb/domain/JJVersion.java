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
@RooJpaEntity(sequenceName = "JJVersionSEQ")
public class JJVersion extends JJAbstractEntity {

	@NotNull
	@ManyToOne
	private JJProduct product;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "versioning")
	private Set<JJTask> tasks = new HashSet<JJTask>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "versioning")
	private Set<JJBuild> builds = new HashSet<JJBuild>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "versioning")
	private Set<JJMessage> messages = new HashSet<JJMessage>();

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJVersion) && (getId() != null) ? getId()
				.equals(((JJVersion) object).getId()) : (object == this);
	}

}
