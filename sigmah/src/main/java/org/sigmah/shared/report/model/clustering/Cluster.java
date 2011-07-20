package org.sigmah.shared.report.model.clustering;

import java.awt.Rectangle;
import java.util.List;

import org.sigmah.server.report.generator.map.MarkerGraph;
import org.sigmah.shared.report.content.LatLng;
import org.sigmah.shared.report.content.Point;
import org.sigmah.shared.report.model.PointValue;

public interface Cluster {

	public abstract void setRectangle(Rectangle rectangle);

	public abstract Rectangle getRectangle();

	public abstract List<PointValue> getPointValues();

	public abstract double sumValues();

	public abstract void setPoint(Point point);

	public abstract Point getPoint();

	public abstract void setRadius(double radius);

	public abstract double getRadius();

	public abstract Point weightedCentroid();

	public abstract LatLng latLngCentroid();

	public abstract Point bboxCenter();

}
