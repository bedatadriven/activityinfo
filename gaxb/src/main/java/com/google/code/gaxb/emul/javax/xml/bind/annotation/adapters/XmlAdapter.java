/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.xml.bind.annotation.adapters;

public abstract class XmlAdapter<ValueType,BoundType> {

  protected XmlAdapter() {}

  public abstract BoundType unmarshal(ValueType v) throws Exception;

  public abstract ValueType marshal(BoundType v) throws Exception;
}
