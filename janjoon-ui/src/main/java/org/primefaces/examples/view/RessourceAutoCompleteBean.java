package org.primefaces.examples.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent; 
import org.primefaces.examples.domain.Player;

@ManagedBean
@ApplicationScoped
public class RessourceAutoCompleteBean {

        private Player selectedPlayer;
        
        private List<Player> players;
        
        private List<Player> selectedPlayers;  
        
        private List<String> selectedTexts;  
        
        public RessourceAutoCompleteBean() {
        	players = PlayerConverter.playerDB;  
        }

        public Player getSelectedPlayer() {
                return selectedPlayer;
        }
        public void setSelectedPlayer(Player selectedPlayer) {
                this.selectedPlayer = selectedPlayer;
        }

        
        public List<String> complete(String query) {
                List<String> results = new ArrayList<String>();
                
                for (int i = 0; i < 10; i++) {
                        results.add(query + i);
                }
                
                return results;
        }
        
        public List<Player> completePlayer(String query) {
                List<Player> suggestions = new ArrayList<Player>();
                
                for(Player p : players) {
                        if(p.getName().startsWith(query))
                                suggestions.add(p);
                }
                
                return suggestions;
        }
        
        public void handleSelect(SelectEvent event) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected:" + event.getObject().toString(), null);
                
                FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        public List<Player> getSelectedPlayers() {  
            return selectedPlayers;  
        }  
      
        public void setSelectedPlayers(List<Player> selectedPlayers) {  
            this.selectedPlayers = selectedPlayers;  
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