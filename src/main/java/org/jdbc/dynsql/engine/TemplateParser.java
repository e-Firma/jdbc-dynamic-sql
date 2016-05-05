package org.jdbc.dynsql.engine;

import java.util.Stack;

import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.lexer.Lexer;
import org.jdbc.dynsql.lexer.LexerToken;
import org.jdbc.dynsql.lexer.LexerTokenCommand;
import org.jdbc.dynsql.lexer.LexerTokenTemplate;

public class TemplateParser {

	public LexerToken parse(String template) throws TemplateCommandException {
		LexerToken mainToken = new LexerTokenTemplate();

		Lexer lexer = new Lexer();
		lexer.setTemplate(template);

		Stack<LexerToken> stack = new Stack<LexerToken>();
		while (lexer.hasMoreTokens()) {
			LexerToken token = lexer.nextToken();
			mainToken.addToken(token);
			if (token instanceof LexerTokenCommand) {
				LexerTokenCommand command = (LexerTokenCommand) token;
				
				switch (command.getCommandName()) {
				case FOR:
					stack.add(mainToken);
					mainToken = token;
					break;
					
				case END_FOR:
					mainToken = stack.pop();
					break;
				
				case IF:
				    stack.add(mainToken);
                    mainToken = token;
                    break;
                    
				case END_IF:
                    mainToken = stack.pop();
                    break;
					
				default:
					break;
				}
			}
		}
		return mainToken;
	}
}
