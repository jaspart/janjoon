package org.primefaces.examples.view;  

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
  
@ManagedBean
@ApplicationScoped
public class InplaceBean {  
  
    private String text = "New step";  
  
    public String getText() {  
        return text;  
    }  
  
    public void setText(String text) {  
        this.text = text;  
    }  
} 