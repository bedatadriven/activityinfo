/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.xml.bind.annotation;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Retention(RUNTIME) @Target({TYPE})
public @interface XmlType {

    String name() default "##default" ;

    String[] propOrder() default {""};

    String namespace() default "##default" ;

    Class factoryClass() default DEFAULT.class;
    static final class DEFAULT {}

    String factoryMethod() default "";
}


