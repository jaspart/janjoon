package com.funder.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJContactSEQ")
public class JJContact extends JJAbstractEntity {

    @NotNull
    @Size(max = 25)
    private String password;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")
    private String email;

    private Integer ldap;

    @NotNull
    @Size(max = 25)
    private String firstname;

    @NotNull
    @Size(max = 25)
    private String lastname;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateofbirth;

    @Size(max = 25)
    private String picture;

    @ManyToOne
    private JJJob job;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<JJRight> rights = new HashSet<JJRight>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<JJProduct> products = new HashSet<JJProduct>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<JJProject> projects = new HashSet<JJProject>();
}
