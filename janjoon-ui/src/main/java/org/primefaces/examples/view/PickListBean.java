package org.primefaces.examples.view;  
  
import java.util.ArrayList;
import java.util.List;

import org.primefaces.examples.domain.Player;
import org.primefaces.model.DualListModel;
//import org.primefaces.event.TransferEvent;  
  
public class PickListBean {  
  
    private DualListModel<Player> players;  
      
    private DualListModel<String> cities;  
  
    public PickListBean() {  
          
        //Cities  
        List<String> citiesSource = new ArrayList<String>();  
        List<String> citiesTarget = new ArrayList<String>();  
          
        citiesSource.add("Istanbul");  
        citiesSource.add("Ankara");  
        citiesSource.add("Izmir");  
        citiesSource.add("Antalya");  
        citiesSource.add("Bursa");  
          
        cities = new DualListModel<String>(citiesSource, citiesTarget);  
    }  
      
    public DualListModel<Player> getPlayers() {  
        return players;  
    }  
    public void setPlayers(DualListModel<Player> players) {  
        this.players = players;  
    }  
      
    public DualListModel<String> getCities() {  
        return cities;    
}  
    public void setCities(DualListModel<String> cities) {  
        this.cities = cities;  
    }  
      
/*    public void onTransfer(TransferEvent event) {  
        StringBuilder builder = new StringBuilder();  
        for(Object item : event.getItems()) {  
            builder.append(((Player) item).getName()).append("<br />");  
        }  
          
        FacesMessage msg = new FacesMessage();  
        msg.setSeverity(FacesMessage.SEVERITY_INFO);  
        msg.setSummary("Items Transferred");  
        msg.setDetail(builder.toString());  
          
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  */
}
