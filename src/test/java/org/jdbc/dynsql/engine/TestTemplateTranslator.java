package org.jdbc.dynsql.engine;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.jdbc.dynsql.engine.TemplateTranslator;
import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.exception.TemplateException;
import org.jdbc.dynsql.exception.TemplateTranslateException;
import org.jdbc.dynsql.lexer.LexerToken;
import org.jdbc.dynsql.lexer.LexerTokenExpression;
import org.jdbc.dynsql.test.Animal;
import org.jdbc.dynsql.test.ZOO;
import org.junit.Assert;
import org.junit.Test;

public class TestTemplateTranslator extends TestCase{
    
    private ZOO zoo;
    private Animal horse;
    private Date currentDate;
    private Map<String, Object> dataObjects = new HashMap<String, Object>();

    
    @Override
    protected void setUp() throws Exception 
    {
        currentDate = new Date(Calendar.getInstance().getTime().getTime());
        horse = new Animal();
        horse.setName("Luna");
        horse.setAge(8);

        zoo = new ZOO();
        zoo.setHorse(horse);
        zoo.setOpenFrom(currentDate);
        
        List<Animal> emptyHorsesList = new ArrayList<Animal>();
        
        dataObjects.put("zoo", zoo);
        dataObjects.put("horse", horse);
        dataObjects.put("horses", emptyHorsesList);
    };
    
    @Test
    public void testWhenPutHorseObjectToTranslator_ExpectedGetCorrectAge() throws TemplateTranslateException
    {
        TemplateTranslator translator = new TemplateTranslator();
        
        LexerTokenExpression token = new LexerTokenExpression();
        token.setExpression("${horse.age}");
        
        Object age = translator.getValueExpression(token, dataObjects);
        Assert.assertEquals(age, 8);
    }
    
    
    @Test
    public void testWhenPutHorseObjectToTranslator_ExpectedGetCorrectName() throws TemplateTranslateException
    {
        TemplateTranslator translator = new TemplateTranslator();
        
        LexerTokenExpression token = new LexerTokenExpression();
        token.setExpression("${horse.name}");
        
        Object name = translator.getValueExpression(token, dataObjects);
        Assert.assertEquals(name, "Luna");
    }
    
    @Test
    public void testWhenPutZooObjectToTranslator_ExpectedGetCorrectHorseName() throws TemplateTranslateException
    {
        TemplateTranslator translator = new TemplateTranslator();
        
        LexerTokenExpression token = new LexerTokenExpression();
        token.setExpression("${zoo.horse.name}");
        
        Object name = translator.getValueExpression(token, dataObjects);
        Assert.assertEquals(name, "Luna");
    }
    

    @Test
    public void testWhenPutZooObjectToTranslator_ExpectedGetCorrectOpenFrom() throws TemplateTranslateException
    {
        TemplateTranslator translator = new TemplateTranslator();
        
        LexerTokenExpression token = new LexerTokenExpression();
        token.setExpression("${zoo.openFrom}");
        
        Object openFrom = translator.getValueExpression(token, dataObjects);
        Assert.assertEquals(openFrom, currentDate);
    }
}
