package org.jdbc.dynsql.lexer;

import java.util.List;
import java.util.regex.Pattern;


public class LexerTokenExpression implements LexerToken {

    private String expression;
    private Object associatedObject;
    private String [] tokenComponents;
    private int lineNumber = 0;
    private int charNumber = 0;
    
    public LexerTokenExpression(String expression) {
        this.expression = expression;
        this.tokenComponents = tokenize(expression);
    }

    public LexerTokenExpression() {
    }

    public void setExpression(String expression) {
       this.expression = expression;
       this.tokenComponents = tokenize(expression);
    }

    public String getExpression() {
        return expression;
    }

    public String[] getTokenComponents() {
        return tokenComponents;
    }

    public String [] tokenize(String variableExpression) {        
        variableExpression = trim(variableExpression);
        String listFields[] = variableExpression.split(Pattern.quote("."));
        return listFields;
    }

    public String trim(String variableExpression) {
        variableExpression = variableExpression.trim();
        if (variableExpression.startsWith("${") && variableExpression.endsWith("}"))
            return variableExpression.substring(2, variableExpression.length() - 1).trim();
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
    
	public void setAssociatedObject(Object object) {
		this.associatedObject = object;
	}

	public Object getAssociatedObject() {
		return this.associatedObject;
	}
    
    public String toString() {
        return String.format("TOKEN EXPRESSION <%s,%s>: [%s]", lineNumber, charNumber, expression);
    }
}
