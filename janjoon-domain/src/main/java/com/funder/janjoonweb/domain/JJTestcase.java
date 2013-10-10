package com.funder.janjoonweb.domain;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJTestcaseSEQ")
public class JJTestcase extends JJAbstractEntity {

    private Integer ordering;

    @Size(max = 100)
    private String resultat;

    @ManyToOne
    private JJCategory category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testcase")
    private Set<JJSoftware> softwares = new HashSet<JJSoftware>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testcase")
    private Set<JJHardware> hardwares = new HashSet<JJHardware>();

   
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testcase")
    private Set<JJTeststep> teststeps = new HashSet<JJTeststep>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<JJTask> tasks = new HashSet<JJTask>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<JJRequirement> requirements = new HashSet<JJRequirement>();

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testcase")
    private Set<JJMessage> messages = new HashSet<JJMessage>();

    /**
     */
    @ManyToOne
    private JJChapter chapter;
}
