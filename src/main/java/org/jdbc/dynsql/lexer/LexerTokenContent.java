package org.jdbc.dynsql.lexer;

import java.util.List;


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

    public void setStartingOfTemplate(int startingOfLine, int startingOfChar) {
        lineNumber = startingOfLine;
        charNumber = startingOfChar;
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
