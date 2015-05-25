package com.starit.janjoonweb.service.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.starit.janjoonweb.domain.JJTask;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Task {
   
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
	private Boolean enabled;
	@XmlElement
	private Date startDatePlanned;
	@XmlElement
	private Date endDatePlanned;
	@XmlElement
	private Date startDateRevised;
	@XmlElement
	private Date endDateRevised;
	@XmlElement
	private Date startDateReal;
	@XmlElement
	private Date endDateReal;
	@XmlElement
	private String build;
	@XmlElement
	private String assignedTo;
	@XmlElement
	private String status;
	
	public Task(JJTask tache){
		this.id=tache.getId();
		this.name=tache.getName();
		this.description=tache.getDescription();
		
		if(tache.getAssignedTo() != null)
			this.assignedTo=tache.getAssignedTo().getName();
		else
			this.assignedTo="null";
		
		
		
		if(tache.getBuild() != null)
		   this.build=tache.getBuild().getName();
		else
			   this.build="null";
		
		if(tache.getCreatedBy() !=null)
			this.createdBy=tache.getCreatedBy().getName();
		else
			this.createdBy="null";
		this.creationDate=tache.getCreationDate();
		this.enabled=tache.getEnabled();
		this.endDatePlanned=tache.getEndDatePlanned();
		this.endDateReal=tache.getEndDateReal();
		this.endDateRevised=tache.getEndDateRevised();
		
		this.startDatePlanned=tache.getStartDatePlanned();
		this.startDateReal=tache.getStartDateReal();
		this.startDateRevised=tache.getEndDateRevised();
		
		if(tache.getStatus()!= null)
			this.status=tache.getStatus().getName();
		else
			this.status="null";
		
		
		
	}
	
	public Task() {
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
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Date getStartDatePlanned() {
		return startDatePlanned;
	}
	public void setStartDatePlanned(Date startDatePlanned) {
		this.startDatePlanned = startDatePlanned;
	}
	public Date getEndDatePlanned() {
		return endDatePlanned;
	}
	public void setEndDatePlanned(Date endDatePlanned) {
		this.endDatePlanned = endDatePlanned;
	}
	public Date getStartDateRevised() {
		return startDateRevised;
	}
	public void setStartDateRevised(Date startDateRevised) {
		this.startDateRevised = startDateRevised;
	}
	public Date getEndDateRevised() {
		return endDateRevised;
	}
	public void setEndDateRevised(Date endDateRevised) {
		this.endDateRevised = endDateRevised;
	}
	public Date getStartDateReal() {
		return startDateReal;
	}
	public void setStartDateReal(Date startDateReal) {
		this.startDateReal = startDateReal;
	}
	public Date getEndDateReal() {
		return endDateReal;
	}
	public void setEndDateReal(Date endDateReal) {
		this.endDateReal = endDateReal;
	}
	
	public String getBuild() {
		return build;
	}
	public void setBuild(String build) {
		this.build = build;
	}
	
	
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	@Override
	public boolean equals(Object object) {
		return (object instanceof JJTask) && (getId() != null) ? getId()
				.equals(((Task) object).getId()) : (object == this);
	}
	
	
	
	
	
	public static List<Task> getListTaskFrommJJTask(List<JJTask> jJTasks){
		List<Task> taches =new ArrayList<Task>();
		for (JJTask tache : jJTasks ){
			taches.add(new Task(tache));
		}
		return taches;
	}
}
