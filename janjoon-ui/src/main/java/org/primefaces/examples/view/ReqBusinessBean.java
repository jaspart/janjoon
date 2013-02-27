package org.primefaces.examples.view;  
  
import java.io.Serializable;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.UUID;  

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
  
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.examples.domain.Requirement;  
  
@ManagedBean
@ViewScoped
public class ReqBusinessBean implements Serializable {  
	
    /** TableBean serial number */
	private static final long serialVersionUID = 8374175889652002317L;

	private final static String[] colors;  
    
    private final static String[] manufacturers;  
    
    private final static String[] years; 
    
    private Requirement selectedRequirement; 
  
    static {  
        colors = new String[10];  
        colors[0] = "Requirement Creation";  
        colors[1] = "Requirement Modification";  
        colors[2] = "Requirement Navigation";  
        colors[3] = "Requirement Types";
        colors[4] = "";
        colors[5] = "";
        colors[6] = "";
        colors[7] = "";
        colors[8] = "";
        colors[9] = "";
  
        manufacturers = new String[10];  
        manufacturers[0] = "Business Requirement Creation";  
        manufacturers[1] = "Functional Requirement Creation";  
        manufacturers[2] = "Techincal Requirement Creation";  
        manufacturers[3] = "Requirement Modification";  
        manufacturers[4] = "Requirement Navigation to Business";  
        manufacturers[5] = "Requirement Navigation to Technical";  
        manufacturers[6] = "Requirement Type Management";  
        manufacturers[7] = "Requirement Document Organisation";  
        manufacturers[8] = "";
        manufacturers[9] = "";
        
        years = new String[10];  
        years[0] = "Requirement Type Organisation";  
        years[1] = "Database insertion";  
        years[2] = "Requirement Edition Display";  
        years[3] = "Requirement Qccessibility";  
        years[4] = "Requirement Modificqtion call";  
        years[5] = "ManagedBean for requirement";  
        years[6] = "Dynamic Action for Requirement";  
        years[7] = "Link ManyToMany between Requirement";  
        years[8] = "Select Display for Navigation";  
        years[9] = "Add search in the box"; 
    }  
  
    private SelectItem[] manufacturerOptions;  
  
    private List<Requirement> filteredRequirements;  
  
    private List<Requirement> carsSmall;  
  
    public ReqBusinessBean() {  
        carsSmall = new ArrayList<Requirement>();  
  
        populateRequirements(carsSmall, 9);  
        manufacturerOptions = createFilterOptions(manufacturers);  
    }  
  
    private void populateRequirements(List<Requirement> list, int size) {  
        for(int i = 0 ; i < size ; i++)  
            list.add(new Requirement(getModel(i), getYear(i), getManufacturer(i), getColor(i)));  
    }
  
    public List<Requirement> getFilteredRequirements() {  
        return filteredRequirements;  
    }  
  
    public void setFilteredRequirements(List<Requirement> filteredRequirements) {  
        this.filteredRequirements = filteredRequirements;  
    }  
  
    public List<Requirement> getRequirementsSmall() {  
        return carsSmall;  
    }  
  
    private String getYear(int i) {  
        return years[i];  
    }
  
    private String getColor(int i) {  
        return colors[i];  
    }
  
    private String getManufacturer(int i) {  
        return manufacturers[i];  
    }
  
    private String getModel(int i) {  
        return UUID.randomUUID().toString().substring(0, 8);  
    }
    
    public Requirement getSelectedRequirement() {  
        return selectedRequirement;  
    }  

    public void setSelectedRequirement(Requirement selectedRequirement) {  
        this.selectedRequirement = selectedRequirement;  
    }  
    
    private SelectItem[] createFilterOptions(String[] data)  {  
        SelectItem[] options = new SelectItem[data.length + 1];  
  
        options[0] = new SelectItem("", "Select");  
        for(int i = 0; i < data.length; i++) {  
            options[i + 1] = new SelectItem(data[i], data[i]);  
        }  
  
        return options;  
    }  
  
    public SelectItem[] getManufacturerOptions() {  
        return manufacturerOptions;  
    }

    public void onRowSelect(SelectEvent event) {
        // your code here...
    }

    public void onRowUnselect(UnselectEvent event) {
        // your code here...
    } 

}  