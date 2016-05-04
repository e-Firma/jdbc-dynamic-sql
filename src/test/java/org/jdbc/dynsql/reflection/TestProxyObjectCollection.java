package org.jdbc.dynsql.reflection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.reflection.BuildIterator;
import org.junit.Assert;
import org.junit.Test;

public class TestProxyObjectCollection {

    @Test
    public void TestGetIterartorForArray_ExpectedGivenIteratorForArray() throws TemplateCommandException {
        String arrayString[] = { "1", "2", "3" };
        Iterator<Object> iterator = BuildIterator.getIterator(arrayString);
        Assert.assertNotNull(iterator);
    }
    
    @Test
    public void TestGetIterartorForList_ExpectedGivenIteratorForList() throws TemplateCommandException {
        List<String> listString = new ArrayList<String>();       
        Iterator<Object> iterator = BuildIterator.getIterator(listString);
        Assert.assertNotNull(iterator);
    }
    
    @Test(expected=TemplateCommandException.class)
    public void TestGetIterartorForNotiterrableObject_ExpectedGivenTemplateException() throws TemplateCommandException {
        String mystring = "";
        BuildIterator.getIterator(mystring);
    }

}
