package com.starit.janjoonweb.ui.mb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

import com.starit.janjoonweb.domain.JJContactService;

@Named
@ApplicationScoped
public class ImageStreamer {

    
	@Autowired
	JJContactService jJContactService;

    public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.        	
    		for(Map.Entry<String, String> entry : context.getExternalContext().getRequestParameterMap().entrySet()) {
    			System.out.println("Displays ==== "+entry.getKey()+" : "+entry.getValue());
    		}
    		String contactId = context.getExternalContext().getRequestParameterMap().get("contactId");
    		System.out.println("Displays contactId : "+contactId);
    		if(contactId==null) {
    			return new DefaultStreamedContent(new ByteArrayInputStream(((LoginBean) LoginBean.findBean("loginBean")).getContact().getPicture()));
    		}
    		else
    		{
    			String company = context.getExternalContext().getRequestParameterMap().get("company");
                if(company == null)
               return new DefaultStreamedContent(new ByteArrayInputStream(jJContactService.findJJContact(Long.valueOf(contactId)).
       				getPicture()));
                else 
               	 return new DefaultStreamedContent(new ByteArrayInputStream(jJContactService.findJJContact(Long.valueOf(contactId)).
            				getCompany().getLogo()));
    		}
             
        }
    }

}