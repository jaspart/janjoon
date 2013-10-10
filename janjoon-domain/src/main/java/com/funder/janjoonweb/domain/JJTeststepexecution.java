package com.funder.janjoonweb.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJTeststepexecutionSEQ")
public class JJTeststepexecution extends JJAbstractEntity {

    /**
     */
    @ManyToOne
    private JJBuild build;

    /**
     */
    @ManyToOne
    private JJTestcase buildTestcase;

    /**
     */
    private Boolean passed;
}
