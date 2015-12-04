package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.extensions.model.layout.LayoutOptions;
import org.springframework.beans.factory.annotation.Configurable;

@ManagedBean(name = "chartBean")
@ViewScoped
@Configurable
public class ChartBean implements Serializable {

	private static final long serialVersionUID = 20120925L;	
	private String state;
	private LayoutOptions layoutOptions;

	@PostConstruct
	protected void initialize() {

		// options for all panes (center and west)
		LayoutOptions panes = new LayoutOptions();
		
		layoutOptions = new LayoutOptions();

		// options for all panes
		panes = new LayoutOptions();
		panes.addOption("slidable", false);
		panes.addOption("resizeWhileDragging", true);
		panes.addOption("resizable", true);
		panes.addOption("closable", false);
		layoutOptions.setPanesOptions(panes);

		// options for east pane
		LayoutOptions pane = new LayoutOptions();
		pane.addOption("resizable", true);
		pane.addOption("closable", false);
		pane.addOption("size", "33%");
		layoutOptions.setNorthOptions(pane);
		layoutOptions.setSouthOptions(pane);
		

	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public LayoutOptions getLayoutOptions() {
		return layoutOptions;
	}

	public void setLayoutOptions(LayoutOptions layoutOptions) {
		this.layoutOptions = layoutOptions;
	}
	
	
}
