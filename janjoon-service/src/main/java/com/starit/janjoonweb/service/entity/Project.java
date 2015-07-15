package com.starit.janjoonweb.service.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.starit.janjoonweb.domain.JJProject;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {
	@XmlElement
	private Long id;
	@XmlElement
	private String name;
	@XmlElement
	private String description;
	@XmlElement
	private Date creationDate;
	@XmlElement
	private String createdBy;
	@XmlElement
	private Date updatedDate;
	@XmlElement
	private String updatedBy;
	@XmlElement
	private Boolean enabled;
	@XmlElement
	private Date startDate;
	@XmlElement
	private Date endDate;
	@XmlElement
	private String manager;

	public Project(JJProject projet) {
		this.id = projet.getId();
		this.name = projet.getName();
		if (projet.getCreatedBy() != null)
			this.createdBy = projet.getCreatedBy().getName();
		else
			this.createdBy = "null";

		this.creationDate = projet.getCreationDate();
		this.description = projet.getDescription();
		this.enabled = projet.getEnabled();
		this.endDate = projet.getEndDate();
		this.startDate = projet.getStartDate();

		if (projet.getManager() != null)
			this.manager = projet.getManager().getName();
		else
			this.manager = "null";
		if (projet.getUpdatedBy() != null)
			this.updatedBy = projet.getUpdatedBy().getName();
		else
			this.updatedBy = "null";
		this.updatedDate = projet.getUpdatedDate();
	}

	public Project() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public boolean equals(Object object) {
		return (object instanceof Project) && (getId() != null) ? getId()
				.equals(((Project) object).getId()) : (object == this);
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", description="
				+ description + ", creationDate=" + creationDate
				+ ", createdBy=" + createdBy + ", enabled=" + enabled
				+ ", updatedBy=" + updatedBy + ", manager=" + manager
				+ ", endDate=" + endDate + ", startDate=" + startDate
				+ ", updatedBy=" + updatedBy + ", ]";
	}

	public static Response getListProjectFromJJProject(List<JJProject> jJProject) {
		List<Project> projets = new ArrayList<Project>();
		GenericEntity<List<Project>> entity = new GenericEntity<List<Project>>(
				projets) {
		};
		for (JJProject projet : jJProject) {
			projets.add(new Project(projet));
		}

		return Response.ok(entity).build();

	}

}
