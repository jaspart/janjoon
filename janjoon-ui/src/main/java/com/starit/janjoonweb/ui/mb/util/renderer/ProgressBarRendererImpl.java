package com.starit.janjoonweb.ui.mb.util.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.progressbar.ProgressBar;
import org.primefaces.component.progressbar.ProgressBarRenderer;

public class ProgressBarRendererImpl extends ProgressBarRenderer {

	@Override
	public void encodeEnd(final FacesContext context,
			final UIComponent component) throws IOException {
		final ProgressBar progressBar = (ProgressBar) component;

		encodeMarkup(context, progressBar);

		if (!progressBar.isDisplayOnly()) {
			encodeScript(context, progressBar);
		}
	}

	@Override
	protected void encodeMarkup(final FacesContext context,
			final ProgressBar progressBar) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final int value = progressBar.getValue();
		final String labelTemplate = progressBar.getLabelTemplate();
		final String style = progressBar.getStyle();
		String styleClass = progressBar.getStyleClass();
		styleClass = styleClass == null
				? ProgressBar.CONTAINER_CLASS
				: ProgressBar.CONTAINER_CLASS + " " + styleClass;

		if (progressBar.isDisabled()) {
			styleClass = styleClass + " ui-state-disabled";
		}

		writer.startElement("div", progressBar);
		writer.writeAttribute("id", progressBar.getClientId(context), "id");
		writer.writeAttribute("class", styleClass, "styleClass");
		if (style != null) {
			writer.writeAttribute("style", style, "style");
		}

		// value
		writer.startElement("div", progressBar);
		writer.writeAttribute("class", ProgressBar.VALUE_CLASS, null);
		if (value != 0) {
			writer.writeAttribute("style", "display:block;width:" + value + "%",
					style);
		} else {
			writer.writeAttribute("style", "display:block;width:" + 0 + "%",
					style);
		}
		writer.endElement("div");

		// label
		writer.startElement("div", progressBar);
		writer.writeAttribute("class", ProgressBar.LABEL_CLASS, null);
		if (labelTemplate != null) {
			writer.writeAttribute("style", "display:block", style);
			writer.write(labelTemplate.replaceAll("\\{value\\}",
					String.valueOf(value)));
		}
		writer.endElement("div");

		writer.endElement("div");
	}

}
