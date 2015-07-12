package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(indexes ={@Index(unique=true,columnList="name,version")})
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
	private JJStatus status;

	private Boolean allTestcases;	
	
	@ManyToMany(mappedBy="builds",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<JJTestcase> testcases = new HashSet<JJTestcase>();
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "JJbuildLinkJJTask", joinColumns = { @javax.persistence.JoinColumn(name = "Build_ID", referencedColumnName = "id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "Task_ID", referencedColumnName = "id") })	
	private Set<JJTask> tasks = new HashSet<JJTask>();
	
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
	
	@Override
	public int hashCode() {
		if (this.getId() != null)
			return this.getId().hashCode();
		else
			return super.hashCode();
	}
}
