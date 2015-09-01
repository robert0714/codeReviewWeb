package com.iisigroup.sonar.httpclient.internal.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonMappingName {
    /**
     * 測試項目編號
     * Test item number.
     *
     * @return the string
     */
    String jsonName() ;
}
