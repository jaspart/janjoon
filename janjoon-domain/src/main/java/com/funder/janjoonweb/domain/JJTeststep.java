package com.funder.janjoonweb.domain;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJTeststepSEQ")
public class JJTeststep extends JJAbstractEntity {

    private Integer ordering;

    @Size(max = 100)
    private String actionstep;

    @Size(max = 100)
    private String resultat;

    private Boolean passed;

    @ManyToOne
    private JJTestcase testcase;

    @ManyToOne
    private JJRequirement requirement;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "teststep")
    private Set<JJMessage> messages = new HashSet<JJMessage>();
}
