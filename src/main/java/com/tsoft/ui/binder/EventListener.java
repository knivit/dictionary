package com.tsoft.ui.binder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( ElementType.FIELD )
public @interface EventListener {
    String property() default "";

    Class<? extends java.util.EventListener> type();
    
    String[] mappings();
}
