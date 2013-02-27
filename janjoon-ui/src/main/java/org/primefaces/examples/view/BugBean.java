package org.primefaces.examples.view;  
  
import java.io.Serializable;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.UUID;  

import javax.faces.context.FacesContext;  

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
  
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.examples.domain.Bug;  
  
@ManagedBean
@ViewScoped
public class BugBean implements Serializable {  
	
    /** TableBean serial number */
	private static final long serialVersionUID = 8374175889652002317L;

	private final static String[] colors;  
    
    private final static String[] manufacturers;  
    
    private final static String[] years; 
    
    private Bug selectedBug; 
  
    static {  
        colors = new String[10];  
        colors[0] = "Bug Creation";  
        colors[1] = "Bug Modification";  
        colors[2] = "Bug Navigation";  
        colors[3] = "Bug Types";
        colors[4] = "";
        colors[5] = "";
        colors[6] = "";
        colors[7] = "";
        colors[8] = "";
        colors[9] = "";
  
        manufacturers = new String[10];  
        manufacturers[0] = "Business Bug Creation";  
        manufacturers[1] = "Functional Bug Creation";  
        manufacturers[2] = "Techincal Bug Creation";  
        manufacturers[3] = "Bug Modification";  
        manufacturers[4] = "Bug Navigation to Business";  
        manufacturers[5] = "Bug Navigation to Technical";  
        manufacturers[6] = "Bug Type Management";  
        manufacturers[7] = "Bug Document Organisation";  
        manufacturers[8] = "";
        manufacturers[9] = "";
        
        years = new String[10];  
        years[0] = "Bug Type Organisation";  
        years[1] = "Database insertion";  
        years[2] = "Bug Edition Display";  
        years[3] = "Bug Qccessibility";  
        years[4] = "Bug Modificqtion call";  
        years[5] = "ManagedBean for requirement";  
        years[6] = "Dynamic Action for Bug";  
        years[7] = "Link ManyToMany between Bug";  
        years[8] = "Select Display for Navigation";  
        years[9] = "Add search in the box"; 
    }  
  
    private SelectItem[] manufacturerOptions;  
  
    private List<Bug> filteredBugs;  
  
    private List<Bug> carsSmall;  
  
    public BugBean() {  
        carsSmall = new ArrayList<Bug>();  
  
        populateBugs(carsSmall, 9);  
        manufacturerOptions = createFilterOptions(manufacturers);  
    }  
  
    private void populateBugs(List<Bug> list, int size) {  
        for(int i = 0 ; i < size ; i++)  
            list.add(new Bug(getModel(i), getYear(i), getManufacturer(i), getColor(i)));  
    }
  
    public List<Bug> getFilteredBugs() {  
        return filteredBugs;  
    }  
  
    public void setFilteredBugs(List<Bug> filteredBugs) {  
        this.filteredBugs = filteredBugs;  
    }  
  
    public List<Bug> getBugsSmall() {  
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
    
    public Bug getSelectedBug() {  
        return selectedBug;  
    }  

    public void setSelectedBug(Bug selectedBug) {  
        this.selectedBug = selectedBug;  
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
    
    public void onEdit(RowEditEvent event) {  
        FacesMessage msg = new FacesMessage("Car Edited", ((Bug) event.getObject()).getModel());  
  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
      
    public void onCancel(RowEditEvent event) {  
        FacesMessage msg = new FacesMessage("Car Cancelled", ((Bug) event.getObject()).getModel());  
  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
}  