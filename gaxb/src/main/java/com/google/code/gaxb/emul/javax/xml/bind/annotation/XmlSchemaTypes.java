/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.xml.bind.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.PACKAGE;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME) @Target({PACKAGE})
public @interface XmlSchemaTypes {
    XmlSchemaType[] value();
}
