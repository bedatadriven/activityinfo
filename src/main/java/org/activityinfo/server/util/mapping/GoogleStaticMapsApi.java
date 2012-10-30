package org.activityinfo.server.util.mapping;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.activityinfo.shared.map.GoogleBaseMap;
import org.activityinfo.shared.report.content.AiLatLng;

/**
 * 
 * Builds the URL for a request from the Google Maps Static API.
 * 
 * <p>See also the Google <a href="http://code.google.com/apis/maps/documentation/staticmaps">documentation</a>
 * 
 */
public class GoogleStaticMapsApi {
	
	private static final String END_POINT = "http://maps.googleapis.com/maps/api/staticmap?";
	
	public static final int MAX_WIDTH = 640;
	public static final int MAX_HEIGHT = 640; 
	
	private int width;
	private int height;
	private int zoom;
	private AiLatLng center;
	private GoogleBaseMap baseMap;
		
	public static GoogleStaticMapsApi buildRequest() {
		return new GoogleStaticMapsApi();
	}
	
	public GoogleStaticMapsApi setWidth(int width) {
		this.width = width;
		return this;
	}

	public GoogleStaticMapsApi setHeight(int height) {
		this.height = height;
		return this;
	}

	public GoogleStaticMapsApi setZoom(int zoom) {
		this.zoom = zoom;
		return this;
	}

	public GoogleStaticMapsApi setCenter(AiLatLng center) {
		this.center = center;
		return this;
	}

	public GoogleStaticMapsApi setBaseMap(GoogleBaseMap baseMap) {
		this.baseMap = baseMap;
		return this;
	}

	public URL url() {
		String url = urlString();
		
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public String urlString() {
		String url = new StringBuilder(END_POINT)
		.append("center=").append(urlEncode(String.format("%f,%f", center.getLat(), center.getLng())))
		.append("&zoom=").append(zoom)
		.append("&size=").append(width).append("x").append(height)
		.append("&maptype=").append(baseMap.getFormatId())
		.append("&sensor=false")
		.append("&key=AIzaSyD3GtyU21LWTZj0eShb3qNB1gec9bdkAj8")
		.toString();
		return url;
	}

	private String urlEncode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// should not happen
			throw new RuntimeException(s, e);
		}
	}
}
