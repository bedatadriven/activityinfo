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

package org.activityinfo.server.report.generator;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author Alex Bertram
 */
public class ScaleTest {

    @Test
    public void testScale() {

        ScaleUtil.Scale s = ScaleUtil.computeScale(1.2, 4.2, 10);

        Assert.assertEquals(0.0, s.valmin);
        Assert.assertEquals(4.5, s.valmax);
        Assert.assertEquals(0.5, s.step);
    }

    @Test
    public void testLargeScale() {

        ScaleUtil.Scale s = ScaleUtil.computeScale(1500, 9000, 10);

        Assert.assertEquals("valmin", 1000.0, s.valmin);
        Assert.assertEquals("valmax", 10000.0, s.valmax);
        Assert.assertEquals("step", 1000.0, s.step);
    }

}
