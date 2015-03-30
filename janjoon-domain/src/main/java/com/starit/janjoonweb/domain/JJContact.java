package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJContactSEQ")
@XmlRootElement
public class JJContact  {
	
	@NotNull
	@Size(max = 100)
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

	@NotNull
	@Size(max = 100)
	private String password;

	@NotNull
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	@Column(unique = true)
	private String email;

	private Integer ldap;

	private Integer priceSold;
	private Integer priceReal;

	@Size(max = 3)
	private String currency;

	@NotNull
	@Size(max = 100)
	private String firstname;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date dateofbirth;

	@Lob
	private byte[] picture;

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

	@NotNull
	@ManyToOne
	private JJCompany company;

	@Lob
	private String calendar;

	@Lob
	private String preference;

	@ManyToMany(mappedBy = "contacts", fetch = FetchType.LAZY)
	private Set<JJSprint> sprints = new HashSet<JJSprint>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "JJCategoryLinkJJContact", joinColumns = { @javax.persistence.JoinColumn(name = "Contact_ID", referencedColumnName = "id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "Category_ID", referencedColumnName = "id") })	
	private Set<JJCategory> categories = new HashSet<JJCategory>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "JJRequirementLinkJJContact", joinColumns = { @javax.persistence.JoinColumn(name = "Contact_ID", referencedColumnName = "id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "Requirement_ID", referencedColumnName = "id") })	
	private Set<JJRequirement> requirements = new HashSet<JJRequirement>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "JJBugLinkJJContact", joinColumns = { @javax.persistence.JoinColumn(name = "Contact_ID", referencedColumnName = "id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "Bug_ID", referencedColumnName = "id") })	
	private Set<JJBug> bugs = new HashSet<JJBug>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "JJTestcaseLinkJJContact", joinColumns = { @javax.persistence.JoinColumn(name = "Contact_ID", referencedColumnName = "id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "Testcase_ID", referencedColumnName = "id") })	
	private Set<JJTestcase> testcases = new HashSet<JJTestcase>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contact")
	private Set<JJPermission> permissions = new HashSet<JJPermission>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contact")
	private Set<JJMessage> messages = new HashSet<JJMessage>();

	@Override
	public boolean equals(Object object) {
		return (object instanceof JJContact) && (getId() != null) ? getId()
				.equals(((JJContact) object).getId()) : (object == this);
	}
}
