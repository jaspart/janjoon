package com.starit.janjoonweb.ui.mb.webService;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.JJVersionService;

@Component
@Path("/build")
public class BuildWS {

	@Autowired
	private JJBuildService jJBuildService;
	
	@Autowired
	private JJProductService jJProductService;
	
	@Autowired
	private JJVersionService jJVersionService;
	
	@Autowired
	private JJContactService jJContactService;
	
	
	@Autowired 
	private BCryptPasswordEncoder encoder;
	

	public void setEncoder(BCryptPasswordEncoder encoder) {
		this.encoder = encoder;
	}

	public void setjJBuildService(JJBuildService jJBuildService) {
		this.jJBuildService = jJBuildService;
	}
	
	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	public void setjJProductService(JJProductService jJProductService) {
		this.jJProductService = jJProductService;
	}
//	@GET
//	@Path("/{param}")
//	@Produces(MediaType.APPLICATION_XML)
//	public JJBuild getMsg(@PathParam("param") String msg) {
//
//		return jJBuildService.getBuilds(null, null, true).get(0);
//
//	}

	@POST
	@Path("createBuild")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String createBuild(
			MultivaluedMap<String, String> personParams) {
		
		String login = personParams.getFirst("mail");
		String password = personParams.getFirst("password");		
		
		if(login != null && password != null)
		{
			JJContact contact = jJContactService.getContactByEmail(login, true);

			if (contact == null) {
				return "user not Found";
			}else if (!encoder.matches(password,contact.getPassword())){
				return "Wrong Password";
			}else 
			{
				String productName = personParams.getFirst("product");
				if(productName == null)
					return "Null Product  Name";
				else
				{
					JJProduct product=jJProductService.getJJProductWithName(productName);
					if(product == null)
						return "Product Not Found";
					else
					{
						String versionName = personParams.getFirst("version");
						if(versionName == null)
							return "Null Version  Name";
						else
						{
							JJVersion version=jJVersionService.getVersionByName(versionName, product);
							if(product == null)
								return "Version Not Found";
							else
							{
								String buildName = personParams.getFirst("build");
								if(buildName == null)
									return "Null Build  Name";
								else
								{
									if(jJBuildService.getBuildByName(version, buildName)!= null)
										return "Build "+buildName+" already exist for product="+productName+" and  version="+versionName;
									else
									{
										JJBuild b = new JJBuild();
										b.setName(buildName);		
										b.setEnabled(true);		
										b.setVersion(version);
										b.setDescription("Build for Version " + version.getName());
										b.setCreatedBy(contact);
										b.setCreationDate(new Date());
										jJBuildService.saveJJBuild(b);
										return "Build "+buildName+" has been created for product="+productName+" and  version="+versionName;
									}
								}
							}
							
						}
					}
					
				}
				
				
				
			}
		}else
		{
			return "null Credentials";
		}
		

	}

}
