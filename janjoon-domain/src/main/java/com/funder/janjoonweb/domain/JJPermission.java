package com.funder.janjoonweb.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJPermissionSEQ")
public class JJPermission {

	@NotNull
	@Size(max = 25)
	private String name;

	@ManyToOne
	private JJProject project;

	@ManyToOne
	private JJProduct product;

	@ManyToOne
	private JJContact contact;

	@ManyToOne
	private JJProfile profile;

}
