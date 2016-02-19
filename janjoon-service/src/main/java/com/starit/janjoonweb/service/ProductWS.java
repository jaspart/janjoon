package com.starit.janjoonweb.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.service.entity.Product;

@Component("productWS")
@Path("/product")
public class ProductWS {
	@Autowired
	private JJProductService jJProductService;

	// @Autowired
	// private JJCompanyService jJCompanyService;
	// @Autowired
	// private JJContactService jJContactService;

	public void setjJProductService(JJProductService jJProductService) {
		this.jJProductService = jJProductService;
	}

	// public void setjJCompanyService(JJCompanyService jJCompanyService) {
	// this.jJCompanyService = jJCompanyService;
	// }
	//
	// public void setjJContactService(JJContactService jJContactService) {
	// this.jJContactService = jJContactService;
	// }

	@GET
	@Path("/listeproduit")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProduct() {
		return Product.getProductListFromJJProductList(
				jJProductService.getAdminListProducts());
	}
}
