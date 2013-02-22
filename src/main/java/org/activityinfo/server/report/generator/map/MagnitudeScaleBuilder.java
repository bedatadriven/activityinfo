package org.activityinfo.server.report.generator.map;

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

import java.util.List;

import org.activityinfo.shared.map.RgbColor;
import org.activityinfo.shared.report.content.PolygonLegend;
import org.activityinfo.shared.report.content.PolygonLegend.ColorClass;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;

import com.google.common.collect.Lists;

public class MagnitudeScaleBuilder {

    private RgbColor maxColor;
    private RgbColor minColor = new RgbColor(255, 255, 255);
    private Jenks jenks = new Jenks();
    private PolygonMapLayer layer;

    public MagnitudeScaleBuilder(PolygonMapLayer layer) {
        this.layer = layer;
        this.maxColor = new RgbColor(layer.getMaxColor());
    }

    public void addValue(double value) {
        if (value > 0) {
            jenks.addValue(value);
        }
    }

    public RgbColor nullColor() {
        return minColor;
    }

    public Scale build() {
        return new Scale();
    }

    public class Scale {

        private Jenks.Breaks breaks;

        private Scale() {
            breaks = jenks.computeBreaks();
        }

        public RgbColor color(Double value) {
            if (value == null || value <= 0) {
                return minColor;
            } else {
                // we add an extra category for 0/null
                double p = ((double) (breaks.classOf(value) + 1))
                    / ((double) (breaks.numClassses() + 1));
                return RgbColor.interpolate(minColor, maxColor, p);
            }
        }

        public PolygonLegend legend() {
            List<PolygonLegend.ColorClass> classes = Lists.newArrayList();
            for (int i = 0; i != breaks.numClassses(); ++i) {
                PolygonLegend.ColorClass clazz = new ColorClass();
                clazz.setMinValue(breaks.getClassMin(i));
                clazz.setMaxValue(breaks.getClassMax(i));
                clazz.setColor(color(breaks.getClassMin(i)).toHexString());
                classes.add(clazz);
            }
            return new PolygonLegend(layer, classes);

        }
    }

}
