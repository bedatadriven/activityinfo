/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package com.google.gwt.gears.persistence.mapping;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Alex Bertram
 */
public class RuntimeMethodInfoImplTest {

  public class MyClass<Z> {

    public void aMethod(String p0,
                        List<Set<Integer>> p1,
                        Map<Integer, String>[] p2,
                        Collection<? extends Integer> p3,
                        Z p4,
                        boolean p5,
                        int[] p6) {

    }

  }


  @Test
  public void testComposeTypeDecl() {

    TypeInfo typeInfo = new TypeInfo.RuntimeImpl(MyClass.class);
    MethodInfo method = typeInfo.getMethods().get(0);
    List<String> params = method.getParameterTypeNames();

    Assert.assertEquals("java.lang.String", params.get(0));
    Assert.assertEquals("java.util.List<java.util.Set<java.lang.Integer>>", params.get(1));
    Assert.assertEquals("java.util.Map<java.lang.Integer, java.lang.String>[]", params.get(2));
    Assert.assertEquals("java.util.Collection<? extends java.lang.Integer>", params.get(3));
    Assert.assertEquals("Z", params.get(4));
    Assert.assertEquals("boolean", params.get(5));
    Assert.assertEquals("int[]", params.get(6));

  }

}
