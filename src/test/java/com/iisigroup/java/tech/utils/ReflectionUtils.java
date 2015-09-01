package com.iisigroup.java.tech.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {
	public static Object getField(Object o, String fieldName) {
        Field field = getAccessibleField(fieldName, o.getClass());
        Object fieldValue = null;
        try {
            fieldValue = field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }
  
    public static void setField(Object o, String fieldName, Object inject) {
        Field field = getAccessibleField(fieldName, o.getClass());
        try {
            field.set(o, inject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
  
    // search in class, in case superclasses as well
    private static Field getAccessibleField(String fieldName, Class c) {
        Field field = null;
        while (c != null) {
            try {
                field = c.getDeclaredField(fieldName);
                field.setAccessible(true);
                break;
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            }
        }
        return field;
    }
}
