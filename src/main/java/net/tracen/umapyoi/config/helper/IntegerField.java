package net.tracen.umapyoi.config.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IntegerField {
    int min() default Integer.MIN_VALUE;
    int max() default Integer.MAX_VALUE;
    String valueFormatter() default "";
}
