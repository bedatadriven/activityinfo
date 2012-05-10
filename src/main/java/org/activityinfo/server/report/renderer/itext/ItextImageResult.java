package org.activityinfo.server.report.renderer.itext;

import org.activityinfo.server.report.renderer.image.ImageResult;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

public interface ItextImageResult extends ImageResult {
	Image toItextImage() throws BadElementException;
}
