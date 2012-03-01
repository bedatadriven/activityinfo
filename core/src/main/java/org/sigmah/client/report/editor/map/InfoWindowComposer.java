package org.sigmah.client.report.editor.map;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.IconMapMarker;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.content.PieMapMarker;
import org.sigmah.shared.report.content.PieMapMarker.SliceValue;
import org.sigmah.shared.report.model.clustering.AdministrativeLevelClustering;
import org.sigmah.shared.report.model.clustering.AutomaticClustering;
import org.sigmah.shared.report.model.clustering.NoClustering;

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
