package com.starit.janjoonweb.ui.mb.util;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class MySessionListener implements HttpSessionListener {

	static Logger logger = Logger.getLogger("MySessionListener-Logger");

	public MySessionListener() {
	}

	public void sessionCreated(HttpSessionEvent event) {
		logger.info("Current Session created : " + event.getSession().getId()
				+ " at " + new Date());

	}

	public void sessionDestroyed(HttpSessionEvent event) {
		// get the destroying session…
		HttpSession session = event.getSession();
		logger.info("Current Session destroyed :" + session.getId()
				+ " Logging out user…");

		try {
			prepareLogoutInfoAndLogoutActiveUser(session);
		} catch (Exception e) {
			logger.error("Error while logging out at " + " destroyed : "
					+ e.getMessage());
		}
	}

	/**
	 * 
	 * Clean your logout operations.
	 * 
	 * @throws IOException
	 */
	public void prepareLogoutInfoAndLogoutActiveUser(HttpSession httpSession) throws IOException {		
//		logger.info("Contact logged out");		
//		ExternalContext ec = fContext.getExternalContext();	
//		httpSession.invalidate();
//		SecurityContextHolder.clearContext();
//		ec.redirect(FacesContext.getCurrentInstance()
//				.getExternalContext().getRequestContextPath()+"/"+timeoutPage);
		
		//HttpRequest request;
				}
}