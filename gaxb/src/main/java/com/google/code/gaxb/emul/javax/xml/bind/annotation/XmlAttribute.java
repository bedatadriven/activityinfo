/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;


@Retention(RUNTIME) @Target({FIELD, METHOD})
public @interface XmlAttribute {

    String name() default "##default";
 

     boolean required() default false;

    String namespace() default "##default" ;
}
