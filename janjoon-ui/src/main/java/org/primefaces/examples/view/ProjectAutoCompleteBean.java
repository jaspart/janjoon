package org.primefaces.examples.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent; 
import org.primefaces.examples.domain.Project;

@ManagedBean
@ApplicationScoped
public class ProjectAutoCompleteBean {

        private Project selectedProject;
        
        private List<Project> projects;
        
        private List<Project> selectedProjects;  
        
        private List<String> selectedTexts;  
        
        public ProjectAutoCompleteBean() {
        	projects = ProjectConverter.projectDB;  
        }

        public Project getSelectedProject() {
                return selectedProject;
        }
        public void setSelectedProject(Project selectedProject) {
                this.selectedProject = selectedProject;
        }

        
        public List<String> complete(String query) {
                List<String> results = new ArrayList<String>();
                
                for (int i = 0; i < 10; i++) {
                        results.add(query + i);
                }
                
                return results;
        }
        
        public List<Project> completeProject(String query) {
                List<Project> suggestions = new ArrayList<Project>();
                
                for(Project p : projects) {
                        if(p.getName().startsWith(query))
                                suggestions.add(p);
                }
                
                return suggestions;
        }
        
        public void handleSelect(SelectEvent event) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected:" + event.getObject().toString(), null);
                
                FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        public List<Project> getSelectedProjects() {  
            return selectedProjects;  
        }  
      
        public void setSelectedProjects(List<Project> selectedProjects) {  
            this.selectedProjects = selectedProjects;  
        }  
        public List<String> getSelectedTexts() {  
            return selectedTexts;  
        }  
        public void setSelectedTexts(List<String> selectedTexts) {  
            this.selectedTexts = selectedTexts;  
        }  
      
        public void handleUnselect(UnselectEvent event) {  
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected:" + event.getObject().toString(), null);  
              
            FacesContext.getCurrentInstance().addMessage(null, message);  
        }  
}