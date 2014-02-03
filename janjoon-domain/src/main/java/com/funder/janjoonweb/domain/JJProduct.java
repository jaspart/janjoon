package com.funder.janjoonweb.domain;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJProductSEQ")
public class JJProduct extends JJAbstractEntity {

    @NotNull
    @Size(max = 25)
    private String extname;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private Set<JJVersion> versions = new HashSet<JJVersion>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private Set<JJChapter> chapters = new HashSet<JJChapter>();

    /**
     */
//    @NotNull
    @ManyToOne
    private JJContact manager;
}
