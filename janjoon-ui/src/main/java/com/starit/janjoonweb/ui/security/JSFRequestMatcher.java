package com.starit.janjoonweb.ui.security;

import java.io.Serializable;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.RequestMatcher;

@SuppressWarnings("deprecation")
public class JSFRequestMatcher implements RequestMatcher,Serializable{

   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
    public boolean matches(HttpServletRequest request) {
        System.out.println(Collections.list(request.getHeaderNames()));
        return request.getHeader("faces-request") != null;
    }
}
