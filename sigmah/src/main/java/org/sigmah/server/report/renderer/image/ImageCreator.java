package org.sigmah.server.report.renderer.image;


public interface ImageCreator<T extends ImageResult> {

	T create(int width, int height);
		
}
