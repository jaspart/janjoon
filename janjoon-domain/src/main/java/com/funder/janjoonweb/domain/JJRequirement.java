package com.funder.janjoonweb.domain;
import com.funder.janjoonweb.domain.reference.JJRelationship;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJRequirementSEQ")
public class JJRequirement extends JJAbstractEntity {

    private Integer numero;

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
    private JJProduct product;

    @ManyToOne
    private JJCategory category;

    @ManyToOne
    private JJCriticity criticity;

    @ManyToOne
    private JJImportance importance;

    @ManyToOne
    private JJStatus status;

    private Boolean isCompleted;

    @Size(max = 100)
    private String impact;

    @Enumerated
    private JJRelationship relation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requirement")
    private Set<JJBug> bugs = new HashSet<JJBug>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requirement")
    private Set<JJTask> tasks = new HashSet<JJTask>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<JJContact> assignedTos = new HashSet<JJContact>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<JJMessage> messages = new HashSet<JJMessage>();

    @Size(max = 250)
    private String note;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "requirement_requirement", 
          	joinColumns = { @JoinColumn(name = "requirement1_id") }, 
            inverseJoinColumns = { @JoinColumn(name = "requirement2_id") })
    private Set<JJRequirement> requirementsLink1 = new HashSet<JJRequirement>();

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requirementLink1")
    private Set<JJRequirement> requirementsLink2 = new HashSet<JJRequirement>();
}
