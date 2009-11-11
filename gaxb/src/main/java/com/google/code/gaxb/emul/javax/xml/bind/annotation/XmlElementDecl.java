/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;

@Retention(RUNTIME)
@Target({METHOD})
public @interface XmlElementDecl {

    Class scope() default GLOBAL.class;


    String namespace() default "##default";

    String name();

    String substitutionHeadNamespace() default "##default";


    String substitutionHeadName() default "";


    String defaultValue() default "\u0000";
   
    public final class GLOBAL {}
}
