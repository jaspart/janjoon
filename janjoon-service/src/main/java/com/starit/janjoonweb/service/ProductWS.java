package com.starit.janjoonweb.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJCompanyService;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.service.entity.Product;

@Component
@Path("/product")
public class ProductWS {
      @Autowired
      private JJProductService jJProductService;
      @Autowired
      private JJCompanyService jJCompanyService;
      @Autowired
      private JJContactService jJContactService;
     
    public void setjJProductService(JJProductService jJProductService) {
  		this.jJProductService = jJProductService;
  	}
  	public void setjJCompanyService(JJCompanyService jJCompanyService) {
  		this.jJCompanyService = jJCompanyService;
  	}
  	public void setjJContactService(JJContactService jJContactService) {
  		this.jJContactService = jJContactService;
  	}
      @GET
      @Path("/listeproduit")
      @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
      public List<Product> getProduct(){
    	  return Product.getProductListFromJJProductList( jJProductService.getAdminListProducts());
      }
}
