package com.funder.janjoonweb.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJTeststepSEQ")
public class JJTeststep extends JJAbstractEntity {

    private Integer place;

    @Size(max = 100)
    private String actionstep;

    @Size(max = 100)
    private String resultat;

    private Boolean passed;

    @ManyToOne
    private JJTestcase testcase;

    @ManyToOne
    private JJRequirement requirement;
}
