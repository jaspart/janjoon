package com.starit.janjoonweb.ui.security;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class Http401EntryPoint
		implements
			AuthenticationEntryPoint,
			Serializable {

	private String timeoutPage = "pages/login.jsf";
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger("Http401EntryPoint-Logger");

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		logger.info("Session is invalid! redirecting to timeoutpage : "
				+ timeoutPage);

		if ((request instanceof HttpServletRequest)
				&& (response instanceof HttpServletResponse)) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			String timeoutUrl = httpServletRequest.getContextPath() + "/"
					+ httpServletRequest.getRequestURI();

			logger.info("Session is invalid! redirecting to timeoutpage : "
					+ timeoutUrl);

			httpServletResponse.sendRedirect(timeoutUrl);
			return;
		}

	}

}
