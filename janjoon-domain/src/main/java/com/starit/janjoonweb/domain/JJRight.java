package com.starit.janjoonweb.domain;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJRightSEQ")
public class JJRight {

    @NotNull
    @Size(max = 25)
    private String objet;

    @ManyToOne
    private JJCategory category;

    private Boolean r;

    private Boolean w;

    private Boolean x;

    @ManyToOne
    private JJProfile profile;
}
