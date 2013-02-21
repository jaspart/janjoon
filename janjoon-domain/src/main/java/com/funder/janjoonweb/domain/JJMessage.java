package com.funder.janjoonweb.domain;

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
}
