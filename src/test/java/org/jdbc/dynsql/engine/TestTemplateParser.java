package org.jdbc.dynsql.engine;

import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.lexer.LexerToken;
import org.jdbc.dynsql.lexer.LexerTokenConverter;
import org.jdbc.dynsql.lexer.LexerTokenTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTemplateParser {

    @Test
    public void test1() throws TemplateCommandException
    {
        TemplateParser parser = new TemplateParser();
        String template = "--##section 0\n--FOR horse in horses\nSELECT * FROM ${horse.name}\n--END_FOR\nSELECT 1";
        LexerToken mainToken = parser.parse(template);
        
        LexerTokenConverter converter = new LexerTokenConverter();
        System.out.println(converter.toString(mainToken));
    }

}
