package net.tracen.umapyoi.config.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LongField {
    long min() default Long.MIN_VALUE;
    long max() default Long.MAX_VALUE;
    String valueFormatter() default "";
}
