/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;


import java.awt.Color;
import java.io.IOException;

import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.report.content.MapLayerLegend;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.layers.MapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer.Slice;
import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.renderer.image.ImageCreator;
import org.sigmah.server.report.renderer.image.ImageMapRenderer;
import org.sigmah.server.util.ColorUtil;

import com.google.inject.Inject;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;


/**
 * Renders a {@link org.activityinfo.shared.report.model.MapReportElement MapElement} into an iText
 * document
 *
 */
public class ItextMapRenderer extends ImageMapRenderer implements ItextRenderer<MapReportElement> {

	private ImageCreator<? extends ItextImageResult> imageCreator;
	
    @Inject
    public ItextMapRenderer(@MapIconPath String mapIconPath, ImageCreator<? extends ItextImageResult> imageCreator) {
        super(mapIconPath);
        this.imageCreator = imageCreator;
    }

    @Override
	public void render(DocWriter writer, Document doc, MapReportElement element) {

        try {
            doc.add(ThemeHelper.elementTitle(element.getTitle()));
            ItextRendererHelper.addFilterDescription(doc, element.getContent().getFilterDescriptions());
            ItextRendererHelper.addDateFilterDescription(doc, element.getFilter().getDateRange());
            renderMap(writer, element, doc);
            if(!element.getContent().getLegends().isEmpty()) {
            	renderLegend(element, doc);
            }

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

	public void renderMap(DocWriter writer, MapReportElement element, Document doc) {
        try {
        	ItextImageResult image = imageCreator.create(element.getWidth(), element.getHeight());
        	
            render(element, image.getGraphics());
            
            doc.add(image.toItextImage());

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
 
    private void renderLegend(MapReportElement element, Document doc) throws DocumentException, IOException {
	    	
    	Table table = new Table(2);
    	table.setBorderWidth(1);
    	table.setWidth(100f);
    	table.setBorderColor(new Color(100,100,100));
    	table.setPadding(5);
    	table.setSpacing(0);
    	table.setCellsFitPage(true);
    	table.setTableFitsPage(true);
    	table.setWidths(new int[] {1,3} );
    	
    	Cell cell = new Cell("Legende");
    	cell.setHeader(true);
    	cell.setColspan(2);
    	table.addCell(cell);
    	table.endHeaders();
    
    	for(MapLayerLegend legend : element.getContent().getLegends()) {
    		
    		Cell symbolCell = new Cell();
    		symbolCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    		symbolCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		
    		ItextImageResult symbol = createLegendSymbol(legend, imageCreator);
			symbolCell.addElement(symbol.toItextImage());
			
    		Cell descriptionCell = new Cell();
    		addLegendDescription(element, legend.getDefinition(), descriptionCell);
    		
    		table.addCell(symbolCell);
    		table.addCell(descriptionCell);
    	}
    	doc.add(table);
	}

	private void addLegendDescription(MapReportElement element,
			MapLayer layer, Cell descriptionCell) throws BadElementException, IOException {
		
		if(layer instanceof PiechartMapLayer) {
			addPieChartDescription(element, descriptionCell, (PiechartMapLayer) layer);
		} else if(layer.getIndicatorIds().size() == 1) {
			addSingleIndicatorDescription(element, layer, descriptionCell);
		} else {
			addIndicatorList(element, layer, descriptionCell);
		}
		
	}

	private void addPieChartDescription(MapReportElement element, Cell descriptionCell, PiechartMapLayer layer) throws BadElementException, IOException {
		
		for(Slice slice : layer.getSlices()) {
			IndicatorDTO indicator = element.getContent().getIndicatorById(slice.getIndicatorId());
			Color color = ColorUtil.colorFromString(slice.getColor());
			ItextImageResult sliceImage = renderSlice(imageCreator, color, 10);
			
			Chunk box = new Chunk(sliceImage.toItextImage(), 0, 0);
			Chunk description = new Chunk(indicator.getName());
			
			Phrase phrase = new Phrase();
			phrase.add(box);
			phrase.add(description);
			
			Paragraph paragraph = new Paragraph(phrase);
			
			descriptionCell.add(paragraph);
		}	
	}
	
	private void addSingleIndicatorDescription(MapReportElement element,
			MapLayer layer, Cell descriptionCell) {
		int indicatorId = layer.getIndicatorIds().get(0);
		IndicatorDTO indicator = element.getContent().getIndicatorById(indicatorId);
		descriptionCell.add(ThemeHelper.filterDescription(indicator.getName()));
	}

	private void addIndicatorList(MapReportElement element, MapLayer layer,
			Cell descriptionCell) {
		com.lowagie.text.List list = new List(List.UNORDERED);
		
		for(int indicatorId : layer.getIndicatorIds()) {
			IndicatorDTO indicator = element.getContent().getIndicatorById(indicatorId);
			list.add(new ListItem(indicator.getName()));
		}
		
		descriptionCell.add(list);
	}	
}
