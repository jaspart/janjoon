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
@RooJpaEntity(sequenceName = "JJChapterSEQ")
public class JJChapter extends JJAbstractEntity {

    @ManyToOne
    private JJProject project;

    @ManyToOne
    private JJCategory category;

    @ManyToOne
    private JJProduct product;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chapter")
    private Set<JJRequirement> requirements = new HashSet<JJRequirement>();

    @ManyToOne
    private JJChapter parent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<JJChapter> chapters = new HashSet<JJChapter>();

    private Integer ordering;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chapter")
    private Set<JJTestcase> testcases = new HashSet<JJTestcase>();
}
