package com.starit.janjoonweb.ui.mb;


import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJMessage.class, beanName = "jJMessageBean")
public class JJMessageBean {

	JJMessage jJmessage;
	
	public JJMessage getjJmessage() {
		return jJmessage;
	}
	public void setjJmessage(JJMessage jJmessage) {
		this.jJmessage = jJmessage;
	}
	public void save (JJMessage mes)
	{		
		String message = "";
        if (mes.getId() != null) {
            jJMessageService.updateJJMessage(mes);
            message = "message_successfully_updated";
        } else {
            jJMessageService.saveJJMessage(mes);
            message = "message_successfully_created";
        }
        
        FacesMessage facesMessage = MessageFactory.getMessage(message, "JJMessage");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();       
	}
	
	public void createMessage(ActionEvent e)
	{
		List<JJCriticity> criticity=jJCriticityService.findAllJJCriticitys();
		
		if(e.getComponent().getId().contains("submit1"))
		{
			System.out.println("info");
			int i=0;
			while(i<criticity.size())
			{
				if(criticity.get(i).getName().equalsIgnoreCase("Info"))
				{
					jJmessage.setCriticity(criticity.get(i));
					i=criticity.size();
				}
				i++;
			}
			
		}
		else
		{
			int i=0;
			while(i<criticity.size())
			{
				if(criticity.get(i).getName().equalsIgnoreCase("Alert"))
				{
					jJmessage.setCriticity(criticity.get(i));
					i=criticity.size();
				}
				i++;
			}
			System.out.println("alert");
		}
		jJmessage.setCreationDate(new Date());
		jJmessage.setDescription(jJmessage.getName()+" : "+jJmessage.getMessage());
		save(jJmessage);
		jJmessage=new JJMessage();
	}
	public void onRowSelect(SelectEvent event) {  
		
		setJJMessage_((JJMessage) event.getObject());       
    }  
}
