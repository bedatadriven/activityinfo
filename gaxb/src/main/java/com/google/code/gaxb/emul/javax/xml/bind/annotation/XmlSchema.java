/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;



@Retention(RUNTIME) @Target(PACKAGE)
public @interface XmlSchema {


    XmlNs[]  xmlns() default {};


    String namespace() default "";

    XmlNsForm elementFormDefault() default XmlNsForm.UNSET;

    XmlNsForm attributeFormDefault() default XmlNsForm.UNSET;

    String location() default NO_LOCATION;

    static final String NO_LOCATION = "##generate";
}
