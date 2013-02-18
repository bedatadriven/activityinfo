package org.activityinfo.server.report.renderer.itext;

import org.activityinfo.server.report.renderer.image.ItextGraphic;
import org.activityinfo.server.report.renderer.image.TileHandler;

public class ItextTileHandler implements TileHandler {

	private ItextGraphic template;

	public ItextTileHandler(ItextGraphic template) {
		this.template = template;
	}

	@Override
	public void addTile(String tileUrl, int x, int y, int width, int height) {
		template.addImage(tileUrl, x,  y, width, height);
	}
}
