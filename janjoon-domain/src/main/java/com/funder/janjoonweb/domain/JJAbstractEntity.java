package com.funder.janjoonweb.domain;

import java.util.Date;

import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
public abstract class JJAbstractEntity {

    @NotNull
    @Size(max = 25)
    private String name;

    @NotNull
    @Lob
    private String description;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date creationDate;

    @ManyToOne
    private JJContact createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date updatedDate;

    @ManyToOne
    private JJContact updatedBy;

    private Boolean enabled;
}
