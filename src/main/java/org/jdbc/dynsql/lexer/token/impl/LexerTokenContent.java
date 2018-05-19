package org.jdbc.dynsql.lexer.token.impl;

import java.util.List;

import org.jdbc.dynsql.lexer.token.LexerToken;


public class LexerTokenContent implements LexerToken {

    private String expression;
    private String [] tokenComponents;
    private List<LexerToken> tokens;
    private int lineNumber = 0;
    private int charNumber = 0;
    
    public void setExpression(String expression) {
       this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public String[] getTokenComponents() {
        return tokenComponents;
    }
    
    public List<LexerToken> getTokens() {
        return null;
    }

    public void addToken(LexerToken token) {
    }

    public void setTemplateBeginning(int lineNumber, int charNumber) {
        this.lineNumber = lineNumber;
        this.charNumber = charNumber;
    }

    @Override
    public String toString() {
        return String.format("TOKEN CONTENT <%s,%s>: [%s]", lineNumber, charNumber, expression);
    }

	public void setAssociatedObject(Object object) {
		// TODO Auto-generated method stub
		
	}

	public Object getAssociatedObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
