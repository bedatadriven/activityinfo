package org.activityinfo.server.report.renderer.image;

import java.text.DecimalFormat;

import org.activityinfo.server.util.ColorUtil;
import org.activityinfo.shared.report.content.PolygonLegend;
import org.activityinfo.shared.report.content.PolygonLegend.ColorClass;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Font;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.font.LineMetrics;
import com.google.code.appengine.awt.geom.Rectangle2D;

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
		this.height = ((PADDING + SWATCH_HEIGHT) * legend.getClasses().size()) + PADDING; 
	}
	
	public ItextGraphic createImage(ImageCreator creator) {

		ItextGraphic result = creator.create(width, height);
		Graphics2D g2d = result.getGraphics();
		    	  
    	int y = 0;
    	for(PolygonLegend.ColorClass clazz : legend.getClasses()) {
        	y += PADDING;
        	g2d.setColor(ColorUtil.colorFromString(clazz.getColor()));
        	g2d.fillRect(PADDING, y, SWATCH_WIDTH, SWATCH_HEIGHT);
        	
        	g2d.setColor(Color.BLACK);
        	g2d.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,  LABEL_HEIGHT));
        	g2d.drawString(formatLabel(clazz), PADDING + SWATCH_WIDTH + PADDING, 
        			y + SWATCH_HEIGHT - (LABEL_HEIGHT/2) );
        	
        	y += SWATCH_HEIGHT;
    	}
    	
    	return result;
	}

	private String formatLabel(ColorClass clazz) {
		StringBuilder label = new StringBuilder();
		label.append(labelFormat.format(clazz.getMinValue()));
		if(clazz.getMinValue() != clazz.getMaxValue()) {
			label.append(" - ").append(labelFormat.format(clazz.getMaxValue()));
		}
		return label.toString();
	}
	


}
