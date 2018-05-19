package org.jdbc.dynsql.engine;

import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.lexer.token.LexerToken;
import org.jdbc.dynsql.lexer.token.impl.LexerTokenConverter;
import org.jdbc.dynsql.lexer.token.impl.LexerTokenTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTemplateParser {

    private String EXAMPLE_TEMPLATE_1 = "--##section 0\n--FOR horse in horses\nSELECT * FROM ${horse.name}\n--END_FOR\nSELECT 1";

    @Test
    public void test1() throws TemplateCommandException
    {
        TemplateParser parser = new TemplateParser();
        LexerToken mainToken = parser.parse(EXAMPLE_TEMPLATE_1);
        
        LexerTokenConverter converter = new LexerTokenConverter();
        System.out.println(converter.toString(mainToken));
    }

}
