package org.jdbc.dynsql.lexer.token.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.lexer.LexerCommand;
import org.jdbc.dynsql.lexer.token.LexerToken;

public class LexerTokenCommand implements LexerToken {

    private String expression;
    private Object associatedObject;
    private String[] tokenComponents;
    private List<LexerToken> tokens;
    private int lineNumber = 0;
    private int charNumber = 0;

    public void setExpression(String expression) {
        this.expression = expression;
        tokenComponents = tokenize(expression);
    }

    public String getExpression() {
        return expression;
    }

    public String[] getTokenComponents() {
        return tokenComponents;
    }

    public String[] tokenize(String variableExpression) {
        variableExpression = trim(variableExpression);
        String tokens[] = variableExpression.split(Pattern.quote(" "));
        for (int index = 0; index < tokens.length; index++) {
            tokens[index] = tokens[index].trim();
        }
        tokens[0] = tokens[0].toUpperCase();
        return tokens;
    }

    public String trim(String variableExpression) {
        variableExpression = variableExpression.trim();
        if (variableExpression.startsWith("--"))
            return variableExpression.substring(2, variableExpression.length()).trim();
        else
            return variableExpression;
    }

    public List<LexerToken> getTokens() {
        return this.tokens;
    }

    public void addToken(LexerToken token) {
        if ( this.tokens == null)
            this.tokens = new ArrayList<LexerToken>();
        this.tokens.add(token);
    }

    public void setStartingOfTemplate(int startingOfLine, int startingOfChar) {
        lineNumber = startingOfLine;
        charNumber = startingOfChar;
    }

    public LexerCommand getCommandName() throws TemplateCommandException {        
        try {
            return LexerCommand.valueOf(getTokenComponents()[0]);
        } catch (Exception ex) {
            throw new TemplateCommandException("Unrecognize command: " + getTokenComponents()[0]);
        }
    }
    
    public String getForCollectionName() {
    	return tokenComponents[3];
    }
    
    public String getForVariableName() {
    	return tokenComponents[1];
    }

	public void setAssociatedObject(Object object) {
		this.associatedObject = object;
	}

	public Object getAssociatedObject() {
		return this.associatedObject;
	}
    
    @Override
    public String toString() {
        return String.format("TOKEN COMMAND <%s,%s>: [%s]", lineNumber, charNumber, expression);
    }
}
