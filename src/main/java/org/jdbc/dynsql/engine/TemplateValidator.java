package org.jdbc.dynsql.engine;

import org.jdbc.dynsql.exception.TemplateException;

public class TemplateValidator {
    public static final String EXPRESSION_IS_NOT_CLOSED = "Expression ${ is not closed.";
    public static final String TEMPLATE_TOO_SHORT_FOR_VALIDATION = "Template too short for validation.";
    public static final String TEMPLATE_IS_NULL = "Unable to validate null template.";

    public void validate(String template) throws TemplateException {
        if (template == null) {
            throw new TemplateException(TEMPLATE_IS_NULL);
        }

        int length = template.length() - 1;

        if (length < 4) {
            throw new TemplateException(TEMPLATE_TOO_SHORT_FOR_VALIDATION);
        }

        int parenthesesCount = 0;
        for (int index = 0; index < length; index++) {
            char c0 = template.charAt(index);
            char c1 = template.charAt(index + 1);
            if (c0 == '$' && c1 == '{')
                parenthesesCount++;
            else if (parenthesesCount > 0 && c0 == '}')
                parenthesesCount--;

            if (!isParenthesesCountProper(parenthesesCount)) {
                throw new TemplateException(EXPRESSION_IS_NOT_CLOSED);
            }
        }
        if (parenthesesCount != 0) {
            throw new TemplateException(EXPRESSION_IS_NOT_CLOSED);
        }
    }

    private boolean isParenthesesCountProper(int parenthesesCount) {
        if (parenthesesCount < 0)
            return false;
        else if (parenthesesCount > 1)
            return false;
        return true;
    }
}
