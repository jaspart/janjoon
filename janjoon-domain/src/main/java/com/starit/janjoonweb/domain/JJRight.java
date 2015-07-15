package com.starit.janjoonweb.domain;

import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJRightSEQ")
@Table(indexes = { @Index(unique = false, columnList = "profile") })
public class JJRight {

	@NotNull
	@Size(max = 25)
	private String objet;

	private Boolean r;

	private Boolean w;

	private Boolean x;

	@ManyToOne
	private JJCategory category;

	@NotNull
	@ManyToOne
	private JJProfile profile;

	private Boolean enabled;

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJRight) && (getId() != null) ? getId()
				.equals(((JJRight) object).getId()) : (object == this);
	}
}
