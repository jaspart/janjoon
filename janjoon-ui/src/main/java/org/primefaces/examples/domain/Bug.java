/*
 * Copyright 2009 Prime Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.examples.domain;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Bug implements Serializable {

	/** Requirement serial number */
	private static final long serialVersionUID = 2906141938919762757L;
	private String model;
	private String year;
	private String manufacturer;
	private String color;
    private int price;

    public Bug(String model, String year, String manufacturer, String color) {
		this.model = model;
		this.year = year;
		this.manufacturer = manufacturer;
		this.color = color;
	}

	public Bug(String model, String year, String manufacturer, String color, int price) {
		this.model = model;
		this.year = year;
		this.manufacturer = manufacturer;
		this.color = color;
        this.price = price;
	}

	public Bug(String randomModel, int randomYear, String randomManufacturer, String randomColor) {
		this.model = randomModel;
		this.year = Integer.toString(randomYear);
		this.manufacturer = randomManufacturer;
		this.color = randomColor;	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

     public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;

		if(!(obj instanceof Bug))
			return false;

		Bug compare = (Bug) obj;

		return compare.model.equals(this.model);
	}

	@Override
	public int hashCode() {
		int hash = 1;

	    return hash * 31 + model.hashCode();
	}

    public void displayCar() {  
        FacesMessage msg = new FacesMessage("Selected", "City:" + model + ", Suburb: " + color);  
  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
}