package org.activityinfo.server.report.generator;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Alex Bertram
 */
public class ScaleTest {

    @Test
    public void testScale() {

        ScaleUtil.Scale s = ScaleUtil.computeScale(1.2, 4.2, 10);

        Assert.assertEquals(0.0, s.getValmin());
        Assert.assertEquals(4.5, s.getValmax());
        Assert.assertEquals(0.5, s.getStep());
    }

    @Test
    public void testLargeScale() {

        ScaleUtil.Scale s = ScaleUtil.computeScale(1500, 9000, 10);

        Assert.assertEquals("valmin", 1000.0, s.getValmin());
        Assert.assertEquals("valmax", 10000.0, s.getValmax());
        Assert.assertEquals("step", 1000.0, s.getStep());
    }

}
