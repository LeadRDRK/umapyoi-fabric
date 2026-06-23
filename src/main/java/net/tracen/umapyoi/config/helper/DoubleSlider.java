package net.tracen.umapyoi.config.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DoubleSlider {
    double min() default Double.MIN_VALUE;
    double max() default Double.MAX_VALUE;
    double step();
    String valueFormatter() default "";
}
