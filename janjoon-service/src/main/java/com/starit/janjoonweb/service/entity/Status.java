package com.starit.janjoonweb.service.entity;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.starit.janjoonweb.domain.JJStatus;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Status {
	@XmlElement
	private String name;

	public Status(JJStatus status) {
		this.name = status.getName();
	}

	public Status() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Response getListContactFromJJStatut(List<JJStatus> jJStatus) {
		List<Status> status = new ArrayList<Status>();
		GenericEntity<List<Status>> entity = new GenericEntity<List<Status>>(
				status) {
		};

		for (JJStatus stat : jJStatus) {
			status.add(new Status(stat));
		}

		return Response.ok(entity).build();

	}

}
