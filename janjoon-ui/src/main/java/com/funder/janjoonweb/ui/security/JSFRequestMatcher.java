package com.funder.janjoonweb.ui.security;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.RequestMatcher;

@SuppressWarnings("deprecation")
public class JSFRequestMatcher implements RequestMatcher {

    public JSFRequestMatcher() {
		
	}

	@SuppressWarnings("unchecked")
	@Override
    public boolean matches(HttpServletRequest request) {
        System.out.println(Collections.list(request.getHeaderNames()));
        return request.getHeader("faces-request") != null;
    }
}
