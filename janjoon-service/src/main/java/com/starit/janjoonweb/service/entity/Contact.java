package com.starit.janjoonweb.service.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.starit.janjoonweb.domain.JJContact;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Contact {
    @XmlElement
    private Long id;
	@XmlElement
	private String name;
	@XmlElement
    private String description;
	@XmlElement
	private String password;
	@XmlElement
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	private String email;
	
	
	public Contact(JJContact cont) {
		this.id=cont.getId();
		this.name=cont.getName();
		this.description=cont.getDescription();
		this.password=cont.getPassword();
		this.email=cont.getEmail();
	}
	
	
	public Contact() {
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean equals(Object object) {
		return (object instanceof JJContact) && (getId() != null) ? getId()
				.equals(((Contact) object).getId()) : (object == this);
	}
	
	
	
	public static Response getListContactFromJJContact(List<JJContact> jJContact){
		List<Contact> contacts =new ArrayList<Contact>();
		GenericEntity<List<Contact>> entity = new GenericEntity<List<Contact>>(contacts) {};

		for(JJContact cont : jJContact){
			 contacts.add(new Contact(cont));
		}
		
		return Response.ok(entity).build();
	
	}
}
