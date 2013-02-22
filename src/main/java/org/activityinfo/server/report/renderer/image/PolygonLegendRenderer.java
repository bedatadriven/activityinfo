package org.activityinfo.server.report.renderer.image;

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

import java.text.DecimalFormat;

import org.activityinfo.server.util.ColorUtil;
import org.activityinfo.shared.report.content.PolygonLegend;
import org.activityinfo.shared.report.content.PolygonLegend.ColorClass;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Font;
import com.google.code.appengine.awt.Graphics2D;

public class PolygonLegendRenderer {

    private static final int PADDING = 2;

    private static final int SWATCH_WIDTH = 25;
    private static final int SWATCH_HEIGHT = 25;

    private static final int LABEL_HEIGHT = 12;
    private static final int LABEL_WIDTH = 75;

    private PolygonLegend legend;
    private PolygonMapLayer layer;

    private DecimalFormat labelFormat;

    private int width;
    private int height;

    public PolygonLegendRenderer(PolygonLegend legend) {
        this.legend = legend;
        this.layer = legend.getDefinition();

        calculateSize();

        labelFormat = new DecimalFormat();
        labelFormat.setMinimumFractionDigits(0);
        labelFormat.setMaximumFractionDigits(0);

    }

    private void calculateSize() {
        this.width = PADDING + SWATCH_WIDTH + PADDING + LABEL_WIDTH + PADDING;
        this.height = ((PADDING + SWATCH_HEIGHT) * legend.getClasses().size())
            + PADDING;
    }

    public ItextGraphic createImage(ImageCreator creator) {

        ItextGraphic result = creator.create(width, height);
        Graphics2D g2d = result.getGraphics();

        int y = 0;
        for (PolygonLegend.ColorClass clazz : legend.getClasses()) {
            y += PADDING;
            g2d.setColor(ColorUtil.colorFromString(clazz.getColor()));
            g2d.fillRect(PADDING, y, SWATCH_WIDTH, SWATCH_HEIGHT);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, LABEL_HEIGHT));
            g2d.drawString(formatLabel(clazz),
                PADDING + SWATCH_WIDTH + PADDING,
                y + SWATCH_HEIGHT - (LABEL_HEIGHT / 2));

            y += SWATCH_HEIGHT;
        }

        return result;
    }

    private String formatLabel(ColorClass clazz) {
        StringBuilder label = new StringBuilder();
        label.append(labelFormat.format(clazz.getMinValue()));
        if (clazz.getMinValue() != clazz.getMaxValue()) {
            label.append(" - ").append(labelFormat.format(clazz.getMaxValue()));
        }
        return label.toString();
    }

}
