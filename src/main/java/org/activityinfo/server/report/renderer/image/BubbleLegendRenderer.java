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
import org.activityinfo.shared.report.content.BubbleLayerLegend;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Font;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.font.LineMetrics;
import com.google.code.appengine.awt.geom.Rectangle2D;


public class BubbleLegendRenderer {

	private static final int PADDING = 2;
	
	private static final int LABEL_HEIGHT = 12;
	
	private BubbleLayerLegend legend;
	private BubbleMapLayer layer;
	
	private DecimalFormat labelFormat;

	/**
	 * True if we are drawing a labeld range of symbols (two) or
	 * just a simple circle.
	 */
	private boolean renderRange;
	
	private int width;
	private int height;
	
	private int bubbleCenterX;
		
	
	public BubbleLegendRenderer(BubbleLayerLegend legend) {
		this.legend = legend;
		this.layer = legend.getDefinition();
		
		this.renderRange = layer.getMinRadius() != layer.getMaxRadius() &&
				legend.hasValues();
		

		calculateSize();

		labelFormat = new DecimalFormat();
		labelFormat.setMinimumFractionDigits(0);
		labelFormat.setMaximumFractionDigits(0);
		
	}

	private void calculateSize() {
		if(renderRange) {
			this.width = PADDING + (layer.getMaxRadius() * 2) + PADDING;
			this.height = 
			   	  PADDING + 
		 		 (layer.getMinRadius() * 2) + 
				  PADDING +
				  LABEL_HEIGHT +
				  PADDING + 
				 (layer.getMaxRadius() * 2) + 
				  PADDING +
				  LABEL_HEIGHT +
				  PADDING;

		} else {
			this.width = this.height = 
				PADDING + (layer.getMaxRadius() * 2) + PADDING;		
		}
		
		this.bubbleCenterX = PADDING + layer.getMaxRadius();
	}
	
	public ItextGraphic createImage(ImageCreator creator) {

		ItextGraphic result = creator.create(width, height);
		Graphics2D g2d = result.getGraphics();
		
		if(renderRange) {
			drawSymbolRange(g2d);
		} else {
			ImageMapRenderer.drawBubble(g2d, 
				ColorUtil.colorFromString(layer.getBubbleColor()), 
				bubbleCenterX, 
				PADDING + layer.getMinRadius(), 
				layer.getMinRadius());
		}
		return result;
	}
	

    private void drawSymbolRange(Graphics2D g2d) {
    	  
    	int y = 0;
    	y += PADDING;
    	y += layer.getMinRadius();
    	
		ImageMapRenderer.drawBubble(g2d, 
				ColorUtil.colorFromString(layer.getBubbleColor()), 
				bubbleCenterX, 
				y, 
				layer.getMinRadius());
	
		y += layer.getMinRadius();
		y += PADDING;
		
		drawBubbleLegendLabel(g2d, 
				bubbleCenterX, 
				y, 
				legend.getMinValue());
		
		y += PADDING;
		y += LABEL_HEIGHT;
		y += PADDING;
		y += layer.getMaxRadius();
		
		ImageMapRenderer.drawBubble(g2d, 
				ColorUtil.colorFromString(layer.getBubbleColor()), 
				bubbleCenterX, 
				y, 
				layer.getMaxRadius());

		y += layer.getMaxRadius();
		y += PADDING;
		
		drawBubbleLegendLabel(g2d, 
				bubbleCenterX, 
				y, 
				legend.getMaxValue());
		    	
	}

	private void drawBubbleLegendLabel(Graphics2D g2d, int x, int y, double value) {
    	Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
        String label = labelFormat.format(value);
        LineMetrics metrics = font.getLineMetrics(label, g2d.getFontRenderContext());
        Rectangle2D bounds = font.getStringBounds(label, g2d.getFontRenderContext());
        
        x -= bounds.getWidth() / 2d;
        y += metrics.getAscent();
        
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(label, x, y);
    }

}
