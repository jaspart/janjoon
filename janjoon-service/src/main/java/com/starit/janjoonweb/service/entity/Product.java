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

import com.starit.janjoonweb.domain.JJProduct;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

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
	private Boolean enabled;
	@XmlElement
	private String extname;
	@XmlElement
	private String manager;

	public Product(JJProduct prod) {

		this.name = prod.getName();
		this.description = prod.getDescription();
		this.creationDate = prod.getCreationDate();
		if (prod.getCreatedBy() != null)
			this.createdBy = prod.getCreatedBy().getName();
		else
			this.createdBy = "null";
		this.enabled = prod.getEnabled();
		this.extname = prod.getExtname();
		this.manager = prod.getManager().getName();
		this.id = prod.getId();

	}

	public Product() {
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getExtname() {
		return extname;
	}

	public void setExtname(String extname) {
		this.extname = extname;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	@Override
	public boolean equals(Object object) {
		return (object instanceof Product) && (getId() != null)
				? getId().equals(((Product) object).getId())
				: (object == this);
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description="
				+ description + ", creationDate=" + creationDate
				+ ", createdBy=" + createdBy + ", enabled=" + enabled
				+ ", extname=" + extname + ", manager=" + manager + "]";
	}

	public static Response getProductListFromJJProductList(
			List<JJProduct> jJProducts) {
		List<Product> products = new ArrayList<Product>();
		GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(
				products) {
		};
		// Response response = Response.ok(entity).build();
		for (JJProduct prod : jJProducts) {
			products.add(new Product(prod));
		}
		// GenericEntity<List<Product>> entity = new
		// GenericEntity<List<Product>>(products) {};
		return Response.ok(entity).build();
	}

}
