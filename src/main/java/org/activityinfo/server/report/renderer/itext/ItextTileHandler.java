package org.activityinfo.server.report.renderer.itext;

import org.activityinfo.server.report.renderer.image.ItextGraphic;
import org.activityinfo.server.report.renderer.image.TileHandler;

public class ItextTileHandler implements TileHandler {

	private ItextGraphic template;
	private int height;

	public ItextTileHandler(ItextGraphic template, int height) {
		this.template = template;
		this.height = height;
	}

	@Override
	public void addTile(String tileUrl, int x, int y, int width, int height) {
		template.addImage(tileUrl, x, height - y, width, height);
	}
}
