package org.jdbc.dynsql.engine;

import org.jdbc.dynsql.exception.TemplateException;
import org.jdbc.dynsql.exception.TemplateLoadException;
import org.junit.Assert;
import org.junit.Test;

public class TestTemplateValidator {

    @Test(expected = TemplateException.class)
    public void TestValidateBrokenTemplate_ExpectedTemplateException() throws TemplateLoadException, TemplateException {
        TemplateEngine engine = new TemplateEngine();
        engine.load("template/validate1.sql");
        String template = engine.getTemplate();
        TemplateValidator validator = new TemplateValidator();
        validator.validate(template);
    }

    @Test
    public void Test1ValidateTemplateWithNotClosedExpression_ExpectedGetMessageExpressionIsNotClosed() throws TemplateLoadException {
        TemplateEngine engine = new TemplateEngine();
        engine.load("template/validate1.sql");
        String template = engine.getTemplate();
        TemplateValidator validator = new TemplateValidator();
        try {
            validator.validate(template);
        } catch (TemplateException e) {
            Assert.assertEquals(TemplateValidator.EXPRESSION_IS_NOT_CLOSED, e.getMessage());
        }
    }

    @Test
    public void Test2ValidateTemplateWithNotClosedExpression_ExpectedGetMessageExpressionIsNotClosed() throws TemplateLoadException {
        TemplateEngine engine = new TemplateEngine();
        engine.load("template/validate2.sql");
        String template = engine.getTemplate();
        TemplateValidator validator = new TemplateValidator();
        try {
            validator.validate(template);
        } catch (TemplateException e) {
            Assert.assertEquals(TemplateValidator.EXPRESSION_IS_NOT_CLOSED,e.getMessage());
        }
    }

    @Test
    public void TestValidateProperlyTemplate_ExpectedSilent() throws TemplateLoadException, TemplateException {
        TemplateEngine engine = new TemplateEngine();
        engine.load("template/example.sql");
        String template = engine.getTemplate();
        TemplateValidator validator = new TemplateValidator();
        validator.validate(template);
        Assert.assertTrue(true);
    }
    
    @Test
    public void TestValidateShortTemplate_ExpectedMessageUnableToValidateTemplate() {
        TemplateValidator validator = new TemplateValidator();
        try {
            validator.validate("_");
        } catch (TemplateException e) {
            Assert.assertEquals(TemplateValidator.TEMPLATE_TOO_SHORT_FOR_VALIDATION, e.getMessage());
        }
    }
    
    @Test
    public void TestValidateNullTemplate_ExpectedMessageTemplateIsNull() {
        TemplateValidator validator = new TemplateValidator();
        try {
            validator.validate(null);
        } catch (TemplateException e) {
            Assert.assertEquals(TemplateValidator.TEMPLATE_IS_NULL, e.getMessage());
        }
    }
}
