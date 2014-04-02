package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJCategorySEQ")
public class JJCategory extends JJAbstractEntity {

	private Integer stage;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category")
	private Set<JJChapter> chapters = new HashSet<JJChapter>();

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJCategory) && (getId() != null) ? getId()
				.equals(((JJCategory) object).getId()) : (object == this);
	}
}
