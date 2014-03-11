package com.starit.janjoonweb.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJCriticitySEQ")
public class JJCriticity extends JJAbstractEntity {

    private Integer criticityLevel;
}
