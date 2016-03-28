package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJRiskSEQ")
public class JJRisk {

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
	private JJProject project;

	@ManyToOne
	private JJProduct product;

	@ManyToOne
	private JJStatus status;

	@Max(100)
	@Min(0)
	private Integer probability;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "JJRequirementLinkJJRisk", joinColumns = {
			@javax.persistence.JoinColumn(name = "Risk_ID", referencedColumnName = "id")}, inverseJoinColumns = {
					@javax.persistence.JoinColumn(name = "Requirement_ID", referencedColumnName = "id")})
	private Set<JJRequirement> requirements = new HashSet<JJRequirement>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "JJBugLinkJJRisk", joinColumns = {
			@javax.persistence.JoinColumn(name = "Risk_ID", referencedColumnName = "id")}, inverseJoinColumns = {
					@javax.persistence.JoinColumn(name = "Bug_ID", referencedColumnName = "id")})
	private Set<JJBug> bugs = new HashSet<JJBug>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "JJTestcaseLinkJJRisk", joinColumns = {
			@javax.persistence.JoinColumn(name = "Risk_ID", referencedColumnName = "id")}, inverseJoinColumns = {
					@javax.persistence.JoinColumn(name = "Testcase_ID", referencedColumnName = "id")})
	private Set<JJTestcase> testcases = new HashSet<JJTestcase>();

}
