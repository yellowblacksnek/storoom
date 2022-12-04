package ru.itmo.highload.storromm.aggregator.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CustomizedOperation {
    String description() default "";
    int[] responseCodes() default {};

    boolean pageable() default false;
}