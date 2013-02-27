package org.primefaces.examples.view;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.primefaces.examples.domain.Project;

@ManagedBean
@ApplicationScoped
public class ProjectConverter implements Converter {

	public static List<Project> projectDB;

    static {  
        projectDB = new ArrayList<Project>();  
  
        projectDB.add(new Project("Messi", 10, "messi.jpg", "CF"));  
        projectDB.add(new Project("Bojan", 9, "bojan.jpg", "CF"));  
        projectDB.add(new Project("Iniesta", 8, "iniesta.jpg", "CM"));  
        projectDB.add(new Project("Villa", 7, "villa.jpg", "CF"));  
        projectDB.add(new Project("Xavi", 6, "xavi.jpg", "CM"));  
        projectDB.add(new Project("Puyol", 5, "puyol.jpg", "CB"));  
        projectDB.add(new Project("Afellay", 20, "afellay.jpg", "AMC"));  
        projectDB.add(new Project("Abidal", 22, "abidal.jpg", "LB"));  
        projectDB.add(new Project("Alves", 2, "alves.jpg", "RB"));  
        projectDB.add(new Project("Pique", 3, "pique.jpg", "CB"));  
        projectDB.add(new Project("Keita", 15, "keita.jpg", "DM"));  
        projectDB.add(new Project("Adriano", 21, "adriano.jpg", "LB"));  
        projectDB.add(new Project("Valdes", 1, "valdes.jpg", "GK"));  
    }  

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                int number = Integer.parseInt(submittedValue);

                for (Project p : projectDB) {
                    if (p.getNumber() == number) {
                        return p;
                    }
                }
                
            } catch(NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Project"));
            }
        }

        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return null;
        } else {
            return String.valueOf(((Project) value).getNumber());
        }
    }
}