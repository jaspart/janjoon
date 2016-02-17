package com.starit.janjoonweb.ui.security;

import java.io.Serializable;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionHandler;

@SuppressWarnings("deprecation")
public class JsfDefaultWebSecurityExpressionhandler
        implements WebSecurityExpressionHandler, Serializable, SecurityExpressionHandler<FilterInvocation> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 1L;
	private DefaultWebSecurityExpressionHandler	delegate			= new DefaultWebSecurityExpressionHandler();

	@Override
	public ExpressionParser getExpressionParser() {
		return delegate.getExpressionParser();
	}

	@Override
	public EvaluationContext createEvaluationContext(Authentication authentication, FilterInvocation invocation) {
		return delegate.createEvaluationContext(authentication, invocation);
	}
}