package com.starit.janjoonweb.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJPermissionSEQ")
public class JJPermission {

	@ManyToOne
	private JJProject project;

	@ManyToOne
	private JJProduct product;

	@NotNull
	@ManyToOne
	private JJContact contact;

	@NotNull
	@ManyToOne
	private JJProfile profile;

}
