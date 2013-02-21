package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.reference.JJRelationship;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJRequirementSEQ")
public class JJRequirement extends JJAbstractEntity {

    @ManyToOne
    private JJProject project;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date endDate;

    @ManyToOne
    private JJChapter chapter;

    @ManyToOne
    private JJVersion jjversion;

    @ManyToOne
    private JJCategory category;

    @ManyToOne
    private JJCriticity criticity;

    @ManyToOne
    private JJImportance importance;

    @ManyToOne
    private JJStatus status;

    @Enumerated
    private JJRelationship relation;

    @ManyToOne
    private com.funder.janjoonweb.domain.JJRequirement requirementUp;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requirementUp")
    private Set<com.funder.janjoonweb.domain.JJRequirement> requirements = new HashSet<com.funder.janjoonweb.domain.JJRequirement>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requirement")
    private Set<JJBug> bugs = new HashSet<JJBug>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requirement")
    private Set<JJTask> tasks = new HashSet<JJTask>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<JJContact> assignedTos = new HashSet<JJContact>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<JJMessage> messages = new HashSet<JJMessage>();
}
