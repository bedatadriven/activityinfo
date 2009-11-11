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

package org.activityinfo.client;

import com.google.gwt.junit.client.GWTTestCase;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.client.mock.MockCommandService;
import org.activityinfo.client.page.map.SingleMapForm;
import org.activityinfo.shared.command.GetBaseMaps;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.report.model.BubbleMapLayer;
import org.activityinfo.shared.report.model.MapElement;

/**
 * @author Alex Bertram
 */
public class SingleMapFormGwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.activityinfo.Application";
    }

    public void testForm() {

        MockCommandService service = new MockCommandService();
        service.setResult(GetSchema.class, DummyData.PEAR());
        service.setResult(GetBaseMaps.class, DummyData.BaseMaps());

        SingleMapForm form = new SingleMapForm(service, Application.CONSTANTS,
                 Application.ICONS);

        MapElement element = (MapElement) form.getMapElement();
        BubbleMapLayer layer = (BubbleMapLayer) element.getLayers().get(0);

        assertTrue(layer.getMinRadius() > 0);
        assertTrue(layer.getMaxRadius() > 0);
    }


}
