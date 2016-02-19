package com.starit.janjoonweb.domain.reference;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Selection;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.criteria.ParameterRegistry;
import org.hibernate.jpa.criteria.Renderable;
import org.hibernate.jpa.criteria.compile.RenderingContext;
import org.hibernate.jpa.criteria.expression.function.BasicFunctionExpression;
import org.hibernate.jpa.criteria.expression.function.FunctionExpression;

public class StrFunction<Y extends Number>
		extends
			BasicFunctionExpression<String>
		implements
			FunctionExpression<String>,
			Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String FCT_NAME = "str";

	private final Selection<Y> selection;

	public StrFunction(CriteriaBuilder criteriaBuilder,
			Selection<Y> selection) {
		super((CriteriaBuilderImpl) criteriaBuilder, String.class, FCT_NAME);
		this.selection = selection;
	}

	@Override
	public void registerParameters(ParameterRegistry registry) {
		Helper.possibleParameter(selection, registry);
	}

	@Override
	public String render(RenderingContext renderingContext) {
		return FCT_NAME + '('
				+ ((Renderable) selection).render(renderingContext) + ')';
	}
}