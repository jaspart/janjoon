package com.starit.janjoonweb.ui.mb.util;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

public class MyJsfAjaxTimeoutPhaseListener implements PhaseListener {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	static Logger				logger				= Logger.getLogger("MyJsfAjaxTimeoutPhaseListener-Logger");
	private String				timeoutPage			= "/pages/login.jsf";

	public void afterPhase(PhaseEvent event) {
	}

	public void beforePhase(PhaseEvent event) {

		FacesContext fc = FacesContext.getCurrentInstance();
		RequestContext rc = RequestContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) ec.getResponse();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);

		if (timeoutPage == null) {
			logger.info("JSF Ajax Timeout Setting is not configured. Do Nothing!");
			return;
		}

		String resourceUri = request.getContextPath() + "/resources";

		boolean loginRequest = request.getRequestURI().contains(timeoutPage);
		boolean resourceRequest = request.getRequestURI().startsWith(resourceUri) || request.getRequestURI()
		        .startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER + "/");

		if (session == null || session.getAttribute("password") == null && !(loginRequest || resourceRequest)) {

			if (ec.isResponseCommitted()) {
				return;
			}

			try {

				if (((rc != null && RequestContext.getCurrentInstance().isAjaxRequest())
				        || (fc != null && fc.getPartialViewContext().isPartialRequest()))
				        && fc.getResponseWriter() == null && fc.getRenderKit() == null) {

					response.setCharacterEncoding(request.getCharacterEncoding());

					RenderKitFactory factory = (RenderKitFactory) FactoryFinder
					        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);

					RenderKit renderKit = factory.getRenderKit(fc,
					        fc.getApplication().getViewHandler().calculateRenderKitId(fc));

					ResponseWriter responseWriter = renderKit.createResponseWriter(response.getWriter(), null,
					        request.getCharacterEncoding());
					fc.setResponseWriter(responseWriter);
					String url = ec.getRequestContextPath() + (timeoutPage != null ? timeoutPage : "");
					ec.redirect(url);
				}

			} catch (IOException e) {
				logger.error("Redirect to the specified page " + timeoutPage + " failed");
				throw new FacesException(e);
			}
		} else {
			return;
		}
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
