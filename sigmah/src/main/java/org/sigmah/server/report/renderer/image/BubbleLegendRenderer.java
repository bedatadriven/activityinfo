package org.sigmah.server.report.renderer.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import org.sigmah.server.util.ColorUtil;
import org.sigmah.shared.report.content.BubbleLayerLegend;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;


public class BubbleLegendRenderer {

	private static final int PADDING = 2;
	
	private static final int MAX_LABEL_WIDTH = 30;
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
	
	private Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
	
	
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
	
	public Image createImage() {

		BufferedImage image = new BufferedImage(width, height, ColorSpace.TYPE_RGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0,0,width, height);

		if(renderRange) {
			drawSymbolRange(g2d);
		} else {
			ImageMapRenderer.drawBubble(g2d, 
				ColorUtil.colorFromString(layer.getBubbleColor()), 
				bubbleCenterX, 
				PADDING + layer.getMinRadius(), 
				layer.getMinRadius());
		}
		return image;

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
