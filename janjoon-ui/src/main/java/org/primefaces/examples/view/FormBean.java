package org.primefaces.examples.view;  
  
import java.io.Serializable;  

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
  
@ManagedBean
@ViewScoped
public class FormBean implements Serializable {  
  
	/** Default */
	private static final long serialVersionUID = -8013640915390617136L;

	private boolean value1;  
  
    private boolean value2;  
  
    public boolean isValue1() {  
        return value1;  
    }  
  
    public void setValue1(boolean value1) {  
        this.value1 = value1;  
    }  
  
    public boolean isValue2() {  
        return value2;  
    }  
  
    public void setValue2(boolean value2) {  
        this.value2 = value2;  
    }  
  
    public void addMessage() {  
        String summary = value2 ? "Checked" : "Unchecked";  
  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));  
    }  
}  