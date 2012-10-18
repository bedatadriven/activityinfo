package org.activityinfo.server.report.renderer.image;


public interface ImageCreator {

	ItextGraphic create(int width, int height);
	
	ItextGraphic createMap(int width, int height);
		
}
