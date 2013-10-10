package com.funder.janjoonweb.domain;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJMessageSEQ")
public class JJMessage extends JJAbstractEntity {

    @NotNull
    @Size(max = 250)
    private String message;

    @ManyToOne
    private JJCriticity criticity;

    @ManyToOne
    private JJStatus status;

    @ManyToOne
    private JJImportance importance;

    @ManyToOne
    private JJSprint sprint;
   
    @ManyToOne
    private JJBuild build;

    @ManyToOne
    private JJTeststep teststep;

    @ManyToOne
    private JJTestcase testcase;
}
