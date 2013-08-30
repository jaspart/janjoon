package com.funder.janjoonweb.domain;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJTestplanSEQ")
public class JJTestplan extends JJAbstractEntity {

    @ManyToOne
    private JJProject project;

    @ManyToOne
    private JJVersion jjversion;

    @ManyToOne
    private JJBuild build;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testplan")
    private Set<JJTestcase> testcases = new HashSet<JJTestcase>();

    /**
     */
    @ManyToOne
    private JJChapter chapter;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testplan")
    private Set<JJMessage> messages = new HashSet<JJMessage>();
}
