package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJVersionSEQ")
public class JJVersion extends JJAbstractEntity {

	@ManyToOne
	private JJProduct product;

	@ManyToOne
	private JJPhase phase;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "versioning")
	private Set<JJBuild> builds = new HashSet<JJBuild>();

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJVersion) && (getId() != null) ? getId()
				.equals(((JJVersion) object).getId()) : (object == this);
	}

}
