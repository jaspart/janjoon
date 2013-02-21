package com.funder.janjoonweb.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJStatusSEQ")
public class JJStatus extends JJAbstractEntity {

    private Integer statusLevel;
}
