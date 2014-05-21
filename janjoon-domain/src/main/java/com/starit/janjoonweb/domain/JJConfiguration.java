package com.starit.janjoonweb.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJConfigurationSEQ")
public class JJConfiguration extends JJAbstractEntity {

	@NotNull
	@Size(max = 100)
	private String param;

	@NotNull
	@Size(max = 100)
	private String val;

}
