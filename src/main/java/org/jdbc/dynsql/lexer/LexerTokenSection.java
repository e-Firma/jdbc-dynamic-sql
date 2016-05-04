package org.jdbc.dynsql.lexer;

import java.util.List;
import java.util.regex.Pattern;

import org.jdbc.dynsql.exception.TemplateCommandException;

public class LexerTokenSection implements LexerToken {

    private String expression;
    private String[] tokenComponents;
    private int lineNumber = 0;
    private int charNumber = 0;

    public void setExpression(String expression) {
        this.expression = trim(expression);
    }

    public String getExpression() {
        return "--" + expression;
    }

    public String[] getTokenComponents() {
        return null;
    }

    public String trim(String variableExpression) {
        variableExpression = variableExpression.trim();
        if (variableExpression.startsWith("--##"))
            return variableExpression.substring(4, variableExpression.length()).trim();
        else
            return variableExpression;
    }

    public List<LexerToken> getTokens() {
        return null;
    }

    public void addToken(LexerToken token) {
        
    }

    public void setStartingOfTemplate(int startingOfLine, int startingOfChar) {
        lineNumber = startingOfLine;
        charNumber = startingOfChar;
    }

    @Override
    public String toString() {
        return String.format("TOKEN SECTION <%s,%s>: [%s]", lineNumber, charNumber, expression);
    }

	public void setAssociatedObject(Object object) {
		// TODO Auto-generated method stub
		
	}

	public Object getAssociatedObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
