package org.primefaces.examples.view;  
          
import javax.faces.event.ActionEvent;  
import javax.faces.context.FacesContext;  
  
@SuppressWarnings("unused")
public class ButtonBean {  
      
    public void save() {  
        addMessage("Data saved");  
    }  
      
    public void update() {  
        addMessage("Data updated");  
    }  
      
    public void delete() {  
        addMessage("Data deleted");  
    }  
      
    public void addMessage(String summary) {  
    }  
}  