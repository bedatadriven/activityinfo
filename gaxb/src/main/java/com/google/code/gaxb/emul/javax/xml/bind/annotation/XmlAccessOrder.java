/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.xml.bind.annotation;

public enum XmlAccessOrder { 
    /**
     * The ordering of fields and properties in a class is undefined. 
     */
    UNDEFINED,
    /**
     * The ordering of fields and properties in a class is in
     * alphabetical order as determined by the
     * method java.lang.String.compareTo(String anotherString).
     */
    ALPHABETICAL
}

