package com.starit.janjoonweb.domain;

import java.util.Date;

import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity(sequenceName = "JJAuditLogSEQ")
@Table(indexes = { @Index(unique = false, columnList = "objet") })
public class JJAuditLog {

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date auditLogDate;

	@ManyToOne
	private JJContact contact;

	private String objet;

	@Size(max = 50)
	private String keyName;

	@Lob
	private String keyValue;
}
