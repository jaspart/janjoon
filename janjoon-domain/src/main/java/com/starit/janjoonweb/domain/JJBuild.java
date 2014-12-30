package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJBuildSEQ")
@XmlRootElement
public class JJBuild {
	
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


	@ManyToOne
	private JJVersion version;

	@ManyToOne
	private JJPhase phase;

	@ManyToOne
	private JJTestcase testcase;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "build")
	private Set<JJMessage> messages = new HashSet<JJMessage>();	

	@XmlAttribute
    public String getName() {
        return this.name;
    }
    
    @XmlAttribute
    public String getDescription() {
        return this.description;
    }
    
    @XmlAttribute
    public Date getCreationDate() {
        return this.creationDate;
    }
    @XmlAttribute
    public Boolean getEnabled() {
        return this.enabled;
    }
    
	@Override
	public boolean equals(Object object) {
		return (object instanceof JJBuild) && (getId() != null) ? getId()
				.equals(((JJBuild) object).getId()) : (object == this);
	}
}
