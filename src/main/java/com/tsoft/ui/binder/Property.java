package com.tsoft.ui.binder;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Property {
    private static final Object[] EMPTY_ARRAY = {};
    
    private Object o;
    private String property;
    private Object value;

    public Property(Object o, String property) throws PropertyException {
        this.o = o;
        this.property = property;

        // split nested properties
        String[] nestedProperties;
        if (property.indexOf('.') == -1) {
            // no dots - simple property
            nestedProperties = new String[]{property};
        } else {
            // at least 1 dot - nested property
            nestedProperties = property.split("\\.");
        }

        // retrieve property values into deep, step-by-step
        for (int i = 0; i < nestedProperties.length; i++) {
            String nestedProperty = nestedProperties[i];

            o = extractNextProperty(o, nestedProperty);

            // check for null if there are more nested properties to dig (if the end is not reached)
            if (o == null && (i != nestedProperties.length - 1)) {
                throw new PropertyException(this.o, this.property,
                        "Could not retrieve next nested property value - property \"" + nestedProperties + "\" is null");
            }
        }

        this.value = o;
    }

    private Object extractNextProperty(Object nextObject, String nextProperty) throws PropertyException {
        // get bean info
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(nextObject.getClass());
        } catch (IntrospectionException e) {
            throw new PropertyException(o, property, "Could not retrieve bean info for property \"" + nextProperty + "\"", e);
        }

        // find property descriptor
        PropertyDescriptor propertyDescriptor = findPropertyDescriptorByName(beanInfo, nextProperty);

        if (propertyDescriptor != null) {
            // get read method
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                // extract the value
                try {
                    readMethod.setAccessible(true);
                    return readMethod.invoke(nextObject, EMPTY_ARRAY);
                } catch (IllegalAccessException e) {
                    throw new PropertyException(o, property, "Could not invoke read method", e);
                } catch (InvocationTargetException e) {
                    throw new PropertyException(o, property, "Could not invoke read method", e);
                }
            }
        }

        try {
            Field field = nextObject.getClass().getDeclaredField(nextProperty);
            field.setAccessible(true);
            return field.get(nextObject);
        } catch (NoSuchFieldException e) {
            throw new PropertyException(o, property, "Could not retrieve field or getter method for property \"" + nextProperty + "\"");
        } catch (IllegalAccessException e) {
            throw new PropertyException(o, property, "Could not retirve field value", e);
        }
    }

    private PropertyDescriptor findPropertyDescriptorByName(BeanInfo beanInfo, String nextProperty) throws PropertyException {
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            if (nextProperty.equals(propertyDescriptor.getName())) {
                return propertyDescriptor;
            }
        }

        return null;
    }

    public Object getValue() {
        return value;
    }

    public static Object getValue(Object o, String property) throws PropertyException {
        return new Property(o, property).getValue();
    }
}
