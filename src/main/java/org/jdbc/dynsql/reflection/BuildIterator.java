package org.jdbc.dynsql.reflection;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections4.iterators.ArrayIterator;
import org.jdbc.dynsql.exception.TemplateCommandException;

public class BuildIterator {

    @SuppressWarnings("unchecked")
    public static Iterator<Object> getIterator(Object object) throws TemplateCommandException {
    	if ( object == null)
    		throw new TemplateCommandException("Can't iterate on the null object");
        if (object.getClass().isArray()) {
            return new ArrayIterator<Object>(object);
        } else if (object instanceof Iterator || object instanceof Collection || object instanceof Iterable){
            return ((Collection<Object>) object).iterator();
        }
        else
        {
            throw new TemplateCommandException("Object <" + object.toString() + "> is not collection or array.");
        }
    }
}
