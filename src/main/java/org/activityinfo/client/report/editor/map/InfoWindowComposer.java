package org.activityinfo.client.report.editor.map;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.IconMapMarker;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.content.PieMapMarker.SliceValue;
import org.activityinfo.shared.report.model.clustering.AdministrativeLevelClustering;
import org.activityinfo.shared.report.model.clustering.AutomaticClustering;
import org.activityinfo.shared.report.model.clustering.NoClustering;

public class InfoWindowComposer {
//
//	private String constructInfoWindowContent(MapMarker marker) {
//		if (marker instanceof PieMapMarker) {
//			PieMapMarker piemarker = (PieMapMarker)marker;
//			
//			// Create a list with all items with the value colored
//			StringBuilder builder = new StringBuilder();
//			
//			addClusteringMessage(piemarker, builder);
//			addIndicatorTitle(builder);
//			
//			// Start an html list
//			builder.append("<ul style=\"list-style:inside;\">");
//
//			// Add each slice of the pie as a listitem
//			for (SliceValue slice : piemarker.getSlices()) {
//				IndicatorDTO indicator = content.getIndicatorById(slice.getIndicatorId());
//
//				builder.append("<li>")
//					   .append("<b><span style=\"background-color:" + slice.getColor() + "\">")
//					   .append(slice.getValue())
//					   .append("</span></b> ")
//					   .append(indicator.getName());
//			}
//
//			// Close and return the html list
//			builder.append("</ul>");
//			return builder.toString();
//			
//		} else if (marker instanceof BubbleMapMarker) {
//			BubbleMapMarker bubbleMarker = (BubbleMapMarker)marker;
//			
//			// Create a list with all items with the value colored
//			StringBuilder builder = new StringBuilder();
//			
//			addClusteringMessage(bubbleMarker, builder);
//
//			builder.append("<p><b>")
//				   .append(I18N.CONSTANTS.sum())
//				   .append(": ")
//				   .append(marker.getTitle())
//				   .append("</b></p>");
//
//			addIndicatorTitle(builder);
//
//			// Start an html list
//			builder.append("<ul style=\"list-style:inside;\">");
//
//			// Add each slice of the pie as a listitem
//			for (Integer indicatorId : bubbleMarker.getIndicatorIds()) {
//				IndicatorDTO indicator = content.getIndicatorById(indicatorId);
//
//				builder.append("<li>");
//				builder.append(indicator.getName());
//			}
//
//			// Close and return the html list
//			builder.append("</ul>");
//			return builder.toString();
//		} else if (marker instanceof IconMapMarker) {
//			IndicatorDTO indicator = content.getIndicatorById
//										(((IconMapMarker) marker).getIndicatorId());
//			return new StringBuilder()
//				.append(indicator.getName())
//				.append(": ")
//				.append("<b>")
//				.append(marker.getTitle())
//				.append("</b>")
//				.toString();
//		}
//		return null;
//	}
//
//	private void addIndicatorTitle(StringBuilder builder) {
//		builder.append("<p>")
//			   .append(I18N.CONSTANTS.indicators())
//			   .append(":</p>");
//	}
//	
//	
//	private void addClusteringMessage(BubbleMapMarker marker, StringBuilder builder) {
//		builder.append("<p>"); 
//		if (marker.getClustering() instanceof NoClustering) {
//			builder.append(I18N.CONSTANTS.none() + " " + I18N.CONSTANTS.clustering());
//		
//		} else if (marker.getClustering() instanceof AutomaticClustering){
//			builder.append(I18N.MESSAGES.amountSitesClusteredByClusteringMethod(
//						  	  Integer.toString(marker.getClusterAmount()), 
//					  		  I18N.CONSTANTS.automatic())); 
//		
//		} else if (marker.getClustering() instanceof AdministrativeLevelClustering) {
//			AdministrativeLevelClustering admincl = (AdministrativeLevelClustering) marker.getClustering();
//			AdminLevelDTO adminLevel = schema.getAdminLevelById(admincl.getAdminLevels().get(0));
//			
//			builder.append(I18N.MESSAGES.amountSitesClusteredByClusteringMethod(
//							  Integer.toString(marker.getClusterAmount()), 
//							  I18N.CONSTANTS.administrativeLevel() + " " + adminLevel.getName())); 			
//		}
//		builder.append("</p>"); 
//	}

}
