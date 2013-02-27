package org.primefaces.examples.view;  
  
import java.io.Serializable;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.UUID;  
  
import org.primefaces.examples.domain.Requirement;  
  
public class PlanBean implements Serializable {  
      
    /** PlanBean serial Version U.I. */
	private static final long serialVersionUID = 3296553097815888083L;

	private final static String[] colors;  
    
    private final static String[] manufacturers;  
      
    private List<Requirement> requirements;  
      
    static {  
        colors = new String[10];  
        colors[0] = "Black";
        colors[1] = "White";
        colors[2] = "Green";
        colors[3] = "Red";
        colors[4] = "Blue";
        colors[5] = "Orange";
        colors[6] = "Silver";
        colors[7] = "Yellow";
        colors[8] = "Brown";
        colors[9] = "Maroon";

        manufacturers = new String[10];
        manufacturers[0] = "Mercedes";
        manufacturers[1] = "BMW";
        manufacturers[2] = "Volvo";
        manufacturers[3] = "Audi";
        manufacturers[4] = "Renault";
        manufacturers[5] = "Opel";
        manufacturers[6] = "Volkswagen";
        manufacturers[7] = "Chrysler";
        manufacturers[8] = "Ferrari";
        manufacturers[9] = "Ford";
    }
    
    public PlanBean() {
        requirements = new ArrayList<Requirement>();
        
        populateRandomCars(requirements, 50);
        
        System.out.println("list requirements = "+requirements.size());
    }
    
    private void populateRandomCars(List<Requirement> list, int size) {
        for(int i = 0 ; i < size ; i++)
            list.add(new Requirement(getRandomModel(), getRandomYear(), getRandomManufacturer(), getRandomColor()));
        System.out.println("list size = "+list.size());
    }
    
    public List<Requirement> getCars() {
        return requirements;
    }
    
    private int getRandomYear() {
        return (int) (Math.random() * 50 + 1960);
    }
    
    private String getRandomColor() {
        return colors[(int) (Math.random() * 10)];
    }
    
    private String getRandomManufacturer() {
        return manufacturers[(int) (Math.random() * 10)];
    }
    
    private String getRandomModel() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}