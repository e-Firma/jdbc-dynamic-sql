package org.jdbc.dynsql.engine;

import org.jdbc.dynsql.engine.TemplateEngine;
import org.jdbc.dynsql.engine.TemplateValidator;
import org.jdbc.dynsql.exception.TemplateException;
import org.jdbc.dynsql.exception.TemplateLoadException;
import org.junit.Assert;
import org.junit.Test;

public class TestTemplateValidator {

    @Test(expected = TemplateException.class)
    public void TestValidateBrokenTemplate_ExpectedTemplateException() throws TemplateLoadException, TemplateException {
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/validate1.sql");
        String template = engine.getTemplate();
        TemplateValidator validator = new TemplateValidator();
        validator.validate(template);
    }

    @Test
    public void Test1ValidateTemplateWithNotClosedExpression_ExpectedGetMessageExpressionIsNotClosed() throws TemplateLoadException {
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/validate1.sql");
        String template = engine.getTemplate();
        TemplateValidator validator = new TemplateValidator();
        try {
            validator.validate(template);
        } catch (TemplateException e) {
            Assert.assertTrue(TemplateValidator.EXPRESSION_IS_NOT_CLOSED.equals(e.getMessage()));
        }
    }

    @Test
    public void Test2ValidateTemplateWithNotClosedExpression_ExpectedGetMessageExpressionIsNotClosed() throws TemplateLoadException {
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/validate2.sql");
        String template = engine.getTemplate();
        TemplateValidator validator = new TemplateValidator();
        try {
            validator.validate(template);
        } catch (TemplateException e) {
            Assert.assertTrue(TemplateValidator.EXPRESSION_IS_NOT_CLOSED.equals(e.getMessage()));
        }
    }

    @Test
    public void TestValidateProperlyTemplate_ExpectedSilent() throws TemplateLoadException, TemplateException {
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/example.sql");
        String template = engine.getTemplate();
        TemplateValidator validator = new TemplateValidator();
        validator.validate(template);
        Assert.assertTrue(true);
    }
    
    @Test
    public void TestValidateShortTemplate_ExpectedMessageUnableToValidateTemplate() throws TemplateLoadException {
        TemplateValidator validator = new TemplateValidator();
        try {
            validator.validate("_");
        } catch (TemplateException e) {
            Assert.assertTrue(TemplateValidator.TEMPLATE_TOO_SHORT_FOR_VALIDATION.equals(e.getMessage()));
        }
    }
    
    @Test
    public void TestValidateNullTemplate_ExpectedMessageTemplateIsNull() throws TemplateLoadException {
        TemplateValidator validator = new TemplateValidator();
        try {
            validator.validate(null);
        } catch (TemplateException e) {
            Assert.assertTrue(TemplateValidator.TEMPLATE_IS_NULL.equals(e.getMessage()));
        }
    }
}
