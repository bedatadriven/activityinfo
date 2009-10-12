package org.activityinfo.shared.report.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Marker<SiteT extends SiteGeoData> {

	private LatLng latlng;

	private List<Point> points = new ArrayList<Point>();

	private List<SiteT> sites = new ArrayList<SiteT>(1);


	private String symbol;

	public Marker() {
	}

	public Marker(SiteT site) {
		if(site.hasLatLong()) {
			latlng = new LatLng(site.getLatitude(), site.getLongitude());
		}
		sites.add(site);
	}

	public LatLng getLatLng() {
		return latlng;
	}

	public void addSite(SiteT site) {
		this.sites.add(site);
	}

	public void addSites(Collection<SiteT> sites) {
		this.sites.addAll(sites);
	}

	public void addPoint(Point p) {
		this.points.add(p);
	}

	public Point center() {
		if(this.points.size() == 1) {
			return this.points.get(0);
		} else {
			double x = 0.0;
			double y = 0.0;
			double count = 0.0;

			for(Point point : points) {
				x += point.x;
				y += point.y;
				count ++;
			}

			return new Point((int)Math.round(x / count), (int)Math.round(y / count));
		}

	}


	public Collection<SiteT> getSites() {
		return sites;
	}

	public void merge(Marker<SiteT> marker) {
		this.sites.addAll(marker.sites);
		this.points.addAll(marker.points);
	}

	public SiteT getFirstSite() {
		return this.sites.get(0);
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
}
