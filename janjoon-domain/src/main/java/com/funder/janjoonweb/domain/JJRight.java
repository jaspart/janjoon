package com.funder.janjoonweb.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJRightSEQ")
public class JJRight {

    /**
     */
    @NotNull
    @ManyToOne
    private JJPermission permission;

    /**
     */
    @ManyToOne
    private JJProject project;

    /**
     */
    @ManyToOne
    private JJProduct product;

    /**
     */
    @ManyToOne
    private JJCategory category;

    /**
     */
    private Boolean r;

    /**
     */
    private Boolean w;

    /**
     */
    private Boolean x;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rights")
    private Set<JJProfile> profiles = new HashSet<JJProfile>();

    /**
     */
    private Boolean basic;
}
