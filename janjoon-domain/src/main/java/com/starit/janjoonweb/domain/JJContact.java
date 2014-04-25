package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
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
	@Size(max = 35)
	private String password;

	@NotNull
	@Pattern(regexp = "[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})")
	@Column(unique=true)
	private String email;

	private Integer ldap;

	@NotNull
	@Size(max = 100)
	private String firstname;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date dateofbirth;

	@Size(max = 25)
	private String picture;

	@ManyToOne
	private JJJob job;

	private Boolean accountNonExpired;

	private Boolean credentialsNonExpired;

	private Boolean accountNonLocked;

	@ManyToOne
	private JJProject lastProject;

	@ManyToOne
	private JJProduct lastProduct;

	@ManyToOne
	private JJVersion lastVersion;

	@ManyToOne
	private JJContact manager;

	@ManyToOne
	private JJCompany company;

	@Lob
	private String calendar;

	@ManyToMany(mappedBy = "contacts", fetch = FetchType.EAGER)
	private Set<JJSprint> sprints = new HashSet<JJSprint>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "contact")
	private Set<JJPermission> permissions = new HashSet<JJPermission>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "contact")
	private Set<JJMessage> messages = new HashSet<JJMessage>();

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJContact) && (getId() != null) ? getId()
				.equals(((JJContact) object).getId()) : (object == this);
	}
}
