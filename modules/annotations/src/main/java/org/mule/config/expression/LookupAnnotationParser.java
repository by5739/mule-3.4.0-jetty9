/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.config.expression;

import org.mule.api.annotations.expressions.Lookup;
import org.mule.api.annotations.meta.Evaluator;
import org.mule.api.expression.ExpressionAnnotationParser;
import org.mule.expression.ExpressionConfig;
import org.mule.expression.transformers.ExpressionArgument;
import org.mule.util.StringUtils;

import java.lang.annotation.Annotation;

/**
 * Used to parse Expr parameter annotations
 *
 * @see org.mule.expression.StringExpressionEvaluator
 * @see org.mule.api.annotations.expressions.Expr
 *
 * @since 3.0
 */
public class LookupAnnotationParser implements ExpressionAnnotationParser
{
    public ExpressionArgument parse(Annotation annotation, Class<?> parameterType)
    {
        Evaluator evaluator = annotation.annotationType().getAnnotation(Evaluator.class);
        if (evaluator != null)
        {
            String expression = ((Lookup) annotation).value();
            if(StringUtils.isEmpty(expression))
            {
                expression  = "type:" + parameterType.getName();
            }
            return new ExpressionArgument(null, new ExpressionConfig(expression,
                    evaluator.value(), null), ((Lookup) annotation).optional(), parameterType);
        }
        else
        {
            throw new IllegalArgumentException("The @Evaluator annotation must be set on an Expression Annotation");
        }

    }

    public boolean supports(Annotation annotation)
    {
        return annotation instanceof Lookup;
    }
}
