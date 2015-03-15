package com.tsoft.dictionary.server.app;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public final class InjectorHelper {
    private static final Logger logger = Logger.getLogger(InjectorHelper.class.getName());

    public static class InjectorFieldList extends ArrayList<Field> {
        private Field entityManagerField;

        public Field getEntityManagerField() {
            return entityManagerField;
        }

        public void setEntityManagerField(Field entityManagerField) {
            this.entityManagerField = entityManagerField;
        }
    }

    private static Map<Class, InjectorFieldList> map = new HashMap<Class, InjectorFieldList>();

    private InjectorHelper() { }

    public static synchronized InjectorFieldList getFieldList(Class clazz) {
        InjectorFieldList fieldList = map.get(clazz);
        if (fieldList != null) {
            return fieldList;
        }

        fieldList = new InjectorFieldList();
        Set<Field> fields = new HashSet<Field>();

        Field[] onlyDeclaredFields = clazz.getDeclaredFields();
        fields.addAll(Arrays.asList(onlyDeclaredFields));

        Field[] allPublicFields = clazz.getFields();
        fields.addAll(Arrays.asList(allPublicFields));

        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                if (field.getType().isAssignableFrom(EntityManager.class)) {
                    fieldList.setEntityManagerField(field);
                } else {
                    fieldList.add(field);
                }
            }
        }

        map.put(clazz, fieldList);
        return fieldList;
    }
}
