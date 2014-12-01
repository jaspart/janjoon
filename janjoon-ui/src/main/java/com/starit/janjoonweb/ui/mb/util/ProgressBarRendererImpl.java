package com.starit.janjoonweb.ui.mb.util;
import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.progressbar.*;

public class ProgressBarRendererImpl extends ProgressBarRenderer {
	
	
	
	public ProgressBarRendererImpl() {
		System.err.println("ProgressBarRendererImpl");
	}

	@Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ProgressBar progressBar = (ProgressBar) component;

        encodeMarkup(context, progressBar);
        
        if(!progressBar.isDisplayOnly()) {
            encodeScript(context, progressBar);
        }
    }

    protected void encodeMarkup(FacesContext context, ProgressBar progressBar) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        int value = progressBar.getValue();
        String labelTemplate = progressBar.getLabelTemplate();
        String style = progressBar.getStyle();
        String styleClass = progressBar.getStyleClass();
        styleClass = styleClass == null ? ProgressBar.CONTAINER_CLASS : ProgressBar.CONTAINER_CLASS + " " + styleClass;
        
        if(progressBar.isDisabled()) {
            styleClass = styleClass + " ui-state-disabled";
        }
        
        writer.startElement("div", progressBar);
        writer.writeAttribute("id", progressBar.getClientId(context), "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if(style != null) {
            writer.writeAttribute("style", style, "style");
        }
        
        //value
        writer.startElement("div", progressBar);
        writer.writeAttribute("class", ProgressBar.VALUE_CLASS, null);
        if(value != 0) {
            writer.writeAttribute("style", "display:block;width:" + value + "%", style);
        }else
        {
        	writer.writeAttribute("style", "display:block;width:" + 0 + "%", style);
        }
        writer.endElement("div");
        
        //label
        writer.startElement("div", progressBar);
        writer.writeAttribute("class", ProgressBar.LABEL_CLASS, null);
        if(labelTemplate != null) {
            writer.writeAttribute("style", "display:block", style);
            writer.write(labelTemplate.replaceAll("\\{value\\}", String.valueOf(value)));
        }
        writer.endElement("div");
        
        writer.endElement("div");
    }
	
	

}
