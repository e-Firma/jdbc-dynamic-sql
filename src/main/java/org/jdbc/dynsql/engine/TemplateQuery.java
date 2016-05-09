package org.jdbc.dynsql.engine;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class TemplateQuery {
	
	private Connection connection;
	
	public TemplateQuery(Connection dbconnection)
	{
		this.connection = dbconnection;
	}
	
	public void load(String pathToTemplate)
	{
		
	}
	
	public List<Object[]> executeQuery(String sectionName, Map<String, Object> data)
	{
		return null;
	}
	
	public List<Object[]> executeQuery(Map<String, Object> data)
	{
		return null;
	}
	
	public Object executeNonQuery(String sectionName, Map<String, Object> data)
	{
		return null;
	}
	
	public Object executeNonQuery(Map<String, Object> data)
	{
		return null;
	}
}
