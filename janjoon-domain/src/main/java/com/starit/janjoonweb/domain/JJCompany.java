package com.starit.janjoonweb.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJCompanySEQ")
public class JJCompany extends JJAbstractEntity {

	@Size(max = 200)
	private String logo;

	@Size(max = 200)
	private String banner;

	@Lob
	private String calendar;

	private Integer priceload;

	private Integer pricepoint;

	@Size(max = 3)
	private String currency;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
	private Set<JJContact> contacts = new HashSet<JJContact>();
}
