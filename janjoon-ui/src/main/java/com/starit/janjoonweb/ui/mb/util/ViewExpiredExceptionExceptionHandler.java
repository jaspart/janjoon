package com.starit.janjoonweb.ui.mb.util;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import com.starit.janjoonweb.ui.mb.LoginBean;

public class ViewExpiredExceptionExceptionHandler
		extends
			ExceptionHandlerWrapper {
	private ExceptionHandler wrapped;

	public ViewExpiredExceptionExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	@Override
	public void handle() throws FacesException {
		for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents()
				.iterator(); i.hasNext();) {
			ExceptionQueuedEvent event = i.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event
					.getSource();

			Throwable t = context.getException();
			if (t instanceof ViewExpiredException) {
				// ViewExpiredException vee = (ViewExpiredException) t;
				FacesContext facesContext = FacesContext.getCurrentInstance();
				// ExternalContext ec = facesContext.getExternalContext();
				// Map<String, Object> requestMap =
				// facesContext.getExternalContext().getRequestMap();
				// NavigationHandler navigationHandler =
				// facesContext.getApplication().getNavigationHandler();

				try {
					if (facesContext.getPartialViewContext().isAjaxRequest()) {
						System.err.println("ajax request");
					}

				} finally {
					i.remove();
				}
			} else if (FacesContext.getCurrentInstance() != null
					&& FacesContext.getCurrentInstance()
							.getExternalContext() != null
					&& LoginBean.findBean("loginBean") != null) {

				if (FacesContext.getCurrentInstance().getViewRoot() != null) {
					String viewId = FacesContext.getCurrentInstance()
							.getViewRoot().getViewId();
					String path = FacesContext.getCurrentInstance()
							.getExternalContext().getRequestContextPath();
					String view = viewId.replace(path, "");
					view = view.replace("/pages/", "");
					if (view.indexOf(".jsf") != -1)
						view = view.substring(0, view.indexOf(".jsf"));
					else
						view = view.replace(".xhtml", "");

				} else {
					LoginBean loginBean = (LoginBean) LoginBean
							.findBean("loginBean");
					loginBean.setFacesMessage(
							MessageFactory.getMessage("label_exceptionHandler",
									FacesMessage.SEVERITY_ERROR));
				}

			}

		}

		// At this point, the queue will not contain any ViewExpiredEvents.
		// Therefore, let the parent handle them.
		getWrapped().handle();
	}
}
