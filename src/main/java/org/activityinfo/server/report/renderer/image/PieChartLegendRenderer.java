package org.activityinfo.server.report.renderer.image;

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.Image;
import com.google.code.appengine.awt.color.ColorSpace;
import com.google.code.appengine.awt.image.BufferedImage;

import org.activityinfo.shared.report.content.PieChartLegend;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;

public class PieChartLegendRenderer {

	private static final int PADDING = 2;

	private int width;
	private int height;
	
	private PiechartMapLayer layer;
	
	public PieChartLegendRenderer(PieChartLegend legend) {
		this.layer = legend.getDefinition();
		
		this.width = this.height = PADDING + (layer.getMaxRadius() * 2) + PADDING;
	}
	
	public <T extends ImageResult> T createImage(ImageCreator<T> creator) {
		T result = creator.create(width, height);
		drawSymbol(result.getGraphics());
		
		return result;
	}

	private void drawSymbol(Graphics2D g2d) {
		PieMapMarker marker = new PieMapMarker();
		marker.setRadius(layer.getMaxRadius());
		marker.setX(PADDING + layer.getMaxRadius());
		marker.setY(PADDING + layer.getMaxRadius());
		
		double values[] = calculateNiceLookingValues(layer.getSlices().size());
		int nextValue = 0;
		
		for(PiechartMapLayer.Slice slice : layer.getSlices()) {
			PieMapMarker.SliceValue sliceValue = new PieMapMarker.SliceValue();
			sliceValue.setColor(slice.getColor());
			sliceValue.setValue(values[nextValue++]);
			
			marker.getSlices().add(sliceValue);
		}
		
		ImageMapRenderer.drawPieMarker(g2d, marker);
	}
	
	private double[] calculateNiceLookingValues(int count) {
		if(count == 1) {
			return new double [] { 100 };
		} else if( count == 2) {
			return new double [] { 75, 25 } ; 
		} else {
			double values [] = new double[count];
			double value = 1;
			for(int i=0;i!=values.length;++i) {
				values[i] = value;
				value *= 1.5 ;
			}
			return values;
		}
	}
	
}
