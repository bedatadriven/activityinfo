package org.activityinfo.client.dispatch.remote;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Wires a {@code Dispatcher} to the Remote dispatcher
 */
@BindingAnnotation
@Target({ FIELD, PARAMETER, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Remote {

}
