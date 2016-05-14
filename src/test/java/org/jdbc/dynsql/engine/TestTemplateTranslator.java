package org.jdbc.dynsql.engine;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdbc.dynsql.exception.TemplateTranslateException;
import org.jdbc.dynsql.lexer.token.impl.LexerTokenExpression;
import org.jdbc.dynsql.test.Animal;
import org.jdbc.dynsql.test.MapWrapper;
import org.jdbc.dynsql.test.ZOO;
import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class TestTemplateTranslator extends TestCase{
    
    private ZOO zoo;
    private Animal horse;
    private Date currentDate;
    private Map<String, Object> dataObjects = new HashMap<String, Object>();
    private TemplateTranslator translator;

    
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
        
        translator = new TemplateTranslator();
    };
    
    @Test
    public void testWhenPutHorseObjectToTranslator_ExpectedGetCorrectAge() throws TemplateTranslateException
    {
        LexerTokenExpression token = new LexerTokenExpression();
        token.setExpression("${horse.age}");
        
        Object age = translator.getValueExpression(token, dataObjects);
        Assert.assertEquals(age, 8);
    }
    
    
    @Test
    public void testWhenPutHorseObjectToTranslator_ExpectedGetCorrectName() throws TemplateTranslateException
    {
        LexerTokenExpression token = new LexerTokenExpression();
        token.setExpression("${horse.name}");
        
        Object name = translator.getValueExpression(token, dataObjects);
        Assert.assertEquals(name, "Luna");
    }
    
    @Test
    public void testWhenPutZooObjectToTranslator_ExpectedGetCorrectHorseName() throws TemplateTranslateException
    {
        LexerTokenExpression token = new LexerTokenExpression();
        token.setExpression("${zoo.horse.name}");
        
        Object name = translator.getValueExpression(token, dataObjects);
        Assert.assertEquals(name, "Luna");
    }
    

    @Test
    public void testWhenPutZooObjectToTranslator_ExpectedGetCorrectOpenFrom() throws TemplateTranslateException
    {
        LexerTokenExpression token = new LexerTokenExpression();
        token.setExpression("${zoo.openFrom}");
        
        Object openFrom = translator.getValueExpression(token, dataObjects);
        Assert.assertEquals(openFrom, currentDate);
    }
    
    @Test
    public void testWhenPutMapObjectToTranslator_ExpectedGetCorrectName() throws TemplateTranslateException
    {
    	// given
    	final String EXPECTED_NAME = "ZOO_NAME";
    	LexerTokenExpression token = new LexerTokenExpression();
    	HashMap<String, Object> data = new HashMap<String, Object>();
    	HashMap<String, Object> zoo = new HashMap<String, Object>();
    	
    	// when
    	zoo.put("name", EXPECTED_NAME);
    	data.put("zoo", zoo);
    	token.setExpression("${zoo.name}");
    	Object receivedName = translator.getValueExpression(token, data);
    	
    	// then
    	Assert.assertEquals(EXPECTED_NAME, receivedName);
    }
    
    @Test
    public void testWhenPutMixed_MapMapObjectToTranslator_ExpectedGetCorrectName() throws TemplateTranslateException
    {
    	// given
    	LexerTokenExpression token = new LexerTokenExpression();
    	HashMap<String, Object> data = new HashMap<String, Object>();
    	HashMap<String, Object> zoo = new HashMap<String, Object>();
    	
    	// when
    	zoo.put("horse", horse);
    	data.put("zoo", zoo);
    	token.setExpression("${zoo.horse.name}");
    	Object receivedName = translator.getValueExpression(token, data);
    	
    	// then
    	Assert.assertEquals(horse.getName(), receivedName);
    }
    
    @Test
    public void testWhenPutMixed_MapObjectMapToTranslator_ExpectedGetCorrectName() throws TemplateTranslateException
    {
    	// given
    	final String EXPECTED_NAME = "ZOO_NAME";
    	LexerTokenExpression token = new LexerTokenExpression();
    	HashMap<String, Object> data = new HashMap<String, Object>();
    	HashMap<String, Object> zoo = new HashMap<String, Object>();
    	MapWrapper mapWrapper = new MapWrapper();
    	HashMap<String, Object> nameProvider = new HashMap<String, Object>();
    	
    	// when
    	nameProvider.put("name", EXPECTED_NAME);
    	mapWrapper.setMap(nameProvider);
    	zoo.put("mapWrapper", mapWrapper);
    	data.put("zoo", zoo);
    	token.setExpression("${zoo.mapWrapper.map.name}");
    	Object receivedName = translator.getValueExpression(token, data);
    	
    	// then
    	Assert.assertEquals(EXPECTED_NAME, receivedName);
    }
}
