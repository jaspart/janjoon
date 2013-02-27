package org.primefaces.examples.view;  
  
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;  
import org.primefaces.event.ResizeEvent;  

public class ResizeBean {

    public void onResize(ResizeEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Image resized", "Width:" + event.getWidth() + ",Height:" + event.getHeight());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}