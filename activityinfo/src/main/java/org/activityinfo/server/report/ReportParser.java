package org.activityinfo.server.report;

import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.model.*;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.TableElement.Column;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.xni.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Stack;


public class ReportParser extends SAXParser {

	
	private Report report;
	private ReportElement currentElement;

	private DimensionType currentFilterDim = null;
	private Collection<Dimension> currentDimSet = null;
	private Dimension currentDim;

	private List<Column> currentColumnList = null;
	private Stack<Column> currentColumnGroup = new Stack<Column>();
	
	private String currentText = null;

    private MapLayer currentLayer;
	
	
	@Override
	public void startElement(QName qname, XMLAttributes attribs, Augmentations augs)
			throws XNIException {
		
		currentText = null;
		
		/*
		 * Root Element
		 */
		
		if("report".equals(qname.localpart)) {
			
			report = new Report();
			currentElement = report;

            

		/* 
		 * Properties of the root (report) element
		 */
			
		} else if("parameter".equals(qname.localpart)) {
			
			Parameter param = new Parameter();
			param.setName(attribs.getValue("name"));
			param.setType(parseParamType(attribs.getValue("type")));
			
			if(param.getType() == Parameter.Type.DATE &&
					attribs.getValue("date-unit") != null) {
			
				param.setDateUnit(parseDateUnit(attribs.getValue("date-unit")));
			}
			report.addParameter(param);
 			
		/* 
		 * Properties common to all ReportElements 
		 */
			
		} else if("filter".equals(qname.localpart)) {
			
			currentDimSet = null;
		
		} else if(currentDimSet == null &&
                "dimension".equals(qname.localpart)) {
			
			currentFilterDim = parseDimensionType(attribs.getValue("type"));
		
		} else if("include".equals(qname.localpart)) {

			currentElement.getFilter().addRestriction(currentFilterDim,
					Integer.parseInt(attribs.getValue("id")));

		} else if("date-range".equals(qname.localpart)) {

			if(attribs.getValue("min")!=null) {
				currentElement.getFilter().setMinDate( parseDateValue(attribs.getValue("min")));
			}
			if(attribs.getValue("max")!=null) {
				currentElement.getFilter().setMaxDate( parseDateValue(attribs.getValue("max")));
			}

		/*
		 * Report Elements
		 */

		} else if("pivotTable".equals(qname.localpart)) {

			currentElement = new PivotTableElement();
			report.addElement(currentElement);

		} else if("chart".equals(qname.localpart)) {

			currentElement = new PivotChartElement(parseChartType(attribs.getValue("type")));
			report.addElement(currentElement);

		} else if("table".equals(qname.localpart)) {

			currentElement = new TableElement();
			report.addElement(currentElement);

			currentColumnList = null;

		} else if("map".equals(qname.localpart)) {

			currentElement = new MapElement();
			report.addElement(currentElement);

		/*
		 * PivotTable properties
		 */

		} else if("row".equals(qname.localpart)) {

			currentDimSet = ((PivotTableElement)currentElement).getRowDimensions();

		} else if(currentElement instanceof PivotTableElement &&
                "column".equals(qname.localpart)) {

			currentDimSet = ((PivotTableElement)currentElement).getColumnDimensions();

		/*
		 * PivotChart properties
		 */

		} else if(currentDim == null && "category".equals(qname.localpart)) {

			currentDimSet = ((PivotChartElement)currentElement).getCategoryDimensions();

		} else if("legend".equals(qname.localpart)) {

			currentDimSet = ((PivotChartElement)currentElement).getLegendDimensions();

        /* Map Dimension Properties */

        } else if("color".equals(qname.localpart)) {

            currentDimSet = ((GsMapLayer)currentLayer).getColorDimensions();

		/*
		 * Properties common to PivotTable and PivotChart (PivotElement)
		 */

		} else if("indicator".equals(qname.localpart)) {

            if(currentElement instanceof PivotChartElement) {
                ((PivotChartElement)currentElement).addIndicator( Integer.parseInt(attribs.getValue("id")));
            } else if(currentLayer instanceof GsMapLayer) {
                ((GsMapLayer)currentLayer).addIndicator( Integer.parseInt(attribs.getValue("id")) );
            } else if(currentLayer instanceof IconMapLayer) {
                ((IconMapLayer)currentLayer).addIndicator( Integer.parseInt(attribs.getValue("id")));
            }
        } else if("activity".equals(qname.localpart)) {
             if(currentLayer instanceof IconMapLayer) {
                ((IconMapLayer)currentLayer).addActivityId( Integer.parseInt(attribs.getValue("id")));
            }

        } else if(currentDimSet != null &&
                "dimension".equals(qname.localpart)) {
				currentDim = createDimension(attribs);
				currentDimSet.add( currentDim );

		} else if(currentDim != null && "category".equals(qname.localpart)) {

            int id = Integer.parseInt(attribs.getValue("id"));
            DimensionCategory category = new EntityCategory(id,null);

            CategoryProperties props = new CategoryProperties();

            if(attribs.getValue("label")!=null){
                props.setLabel(attribs.getValue("label"));
            }
            if(attribs.getValue("color")!=null) {
                props.setColor(Color.decode(attribs.getValue("color")).getRGB());
            }

            currentDim.setProperties(category, props);

		/*
		 * Properties of TableElement
		 */

		} else if(currentElement instanceof TableElement &&
                "columns".equals(qname.localpart)) {

			if(currentColumnGroup.size() != 0) {
				throw new Error();
			}
			currentColumnList = null;
			currentColumnGroup.push(((TableElement) currentElement).getRootColumn());

			if(attribs.getValue("frozen")!= null) {
				((TableElement) currentElement).setFrozenColumns(Integer.parseInt(attribs.getValue("frozen")));
			}

		} else if("sortBy".equals(qname.localpart)) {

			currentColumnList = ((TableElement) currentElement).getSortBy();

		} else if("panelBy".equals(qname.localpart)) {

			currentColumnList = ((TableElement) currentElement).getPanelBy();

		} else if("columnGroup".equals(qname.localpart)) {

			Column group = new Column(attribs.getValue("label"));
			currentColumnGroup.peek().addChild(group);
			currentColumnGroup.push(group);


		} else if("column".equals(qname.localpart)) {

			Column column = parseColumn(attribs);

			if(currentColumnList == null) {
				currentColumnGroup.peek().addChild(column);
			} else {
				currentColumnList.add(column);
			}

        /* Map Layers */
		} else if("graduatedSymbolLayer".equals(qname.localpart)) {

            currentLayer = new GsMapLayer();
            ((MapElement)currentElement).addLayer(currentLayer);

        } else if("iconLayer".equals(qname.localpart)) {

            currentLayer = new IconMapLayer();
            ((MapElement)currentElement).addLayer(currentLayer);

        }

	}



	@Override
	public void characters(XMLString text, Augmentations augs)
			throws XNIException {

		
		super.characters(text, augs);
			
		if(currentText == null) {
			currentText = text.toString();
		} else {
			currentText = currentText + text.toString();
		}

	}

	@Override
	public void endElement(QName qname, Augmentations augs) throws XNIException {

		super.endElement(qname, augs);

		/* Text properties */
		
		if("title".equals(qname.localpart)) {
			currentElement.setTitle(currentText);
		} else if("sheetTitle".equals(qname.localpart)) {
			currentElement.setTitle(currentText);
		} else if("filename".equals(qname.localpart)) {
			report.setFileName(currentText);
		} else if("categoryAxisTitle".equals(qname.localpart)) {
			((PivotChartElement)currentElement).setCategoryAxisTitle(currentText);
		} else if("valueAxisTitle".equals(qname.localpart)) {
			((PivotChartElement)currentElement).setValueAxisTitle(currentText);
        } else if("icon".equals(qname.localpart)) {
            ((IconMapLayer)currentLayer).setIcon(currentText);
        } else if("minRadius".equals(qname.localpart)) {
            ((GsMapLayer)currentLayer).setMinRadius(Integer.parseInt(currentText));
        } else if("maxRadius".equals(qname.localpart)) {
            ((GsMapLayer)currentLayer).setMaxRadius(Integer.parseInt(currentText));
        } else if("baseMap".equals(qname.localpart)) {
            ((MapElement)currentElement).setBaseMapId(currentText);
        } else if("dimension".equals(qname.localpart)) {
             currentDim  = null;

		/* Nested elements */
			
		} else if(currentColumnGroup.size() != 0 &&
                "columnGroup".equals(qname.localpart)) {
			currentColumnGroup.pop();
		}
		
		
		
		currentText = null;
	}
	

	private Dimension createDimension(XMLAttributes attribs)  {
		
		DimensionType type = parseDimensionType(attribs.getValue("type"));
		
		if(type == DimensionType.Date) {
			DateDimension dim = new DateDimension(parseDateUnit(attribs.getValue("date-unit")));
			
			if(attribs.getValue("format") != null) {
				dim.setFormat(attribs.getValue("format"));
			}
			
			return dim;

        } else if(type == DimensionType.AdminLevel) {

            return new AdminDimension(
                    Integer.parseInt(attribs.getValue("levelId")));

		} else {
			
			return new Dimension(type);
		}
		
	}
	
	private PivotChartElement.Type parseChartType(String typeAttrib) throws XNIException {
		if("bar".equals(typeAttrib)) {
			return PivotChartElement.Type.Bar;
		} else if("stacked-bar".equals(typeAttrib)) {
			return PivotChartElement.Type.StackedBar;
		} else if("clustered-bar".equals(typeAttrib)) {
			return PivotChartElement.Type.ClusteredBar;
        } else if("pie".equals(typeAttrib)) {
            return PivotChartElement.Type.Pie;
        } else {
			throw new XNIException("chart type: " + typeAttrib);
		}		
	}
	

	private Parameter.Type parseParamType(String typeAttrib) throws XNIException {
		
		for(Parameter.Type type : Parameter.Type.values()) {
			if(type.toString().toLowerCase().equals(typeAttrib)) {
				return type;
			}
		}
		throw new XNIException("parameter type name: " + typeAttrib);
	}

	
	private DimensionType parseDimensionType(String typeAttrib) throws XNIException {
		
		for(DimensionType type : DimensionType.values()) {
			if(type.toString().toLowerCase().equals(typeAttrib)) {
				return type;

			}
		}
		throw new XNIException("dimension type: " + typeAttrib);
	}
	
	private DateUnit parseDateUnit(String unitAttrib) throws XNIException {
		
		for(DateUnit type : DateUnit.values()) {
			if(type.toString().toLowerCase().equals(unitAttrib)) {
				return type;
			}
		}
		throw new XNIException("date unit: " + unitAttrib);
	}


    private Date parseDateValue(String value) {

        if(value.startsWith("${")) {
            // legacy parameter. treat as null and it should
            // work out.
            
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            throw new XNIException(e);
        }
	}

	public Report getReport() {
		return report;
	}

	public static Report parseXml(String reportXml) throws SAXException, IOException {
		
		ReportParser parser = new ReportParser();
		parser.parse(new InputSource(new StringReader(reportXml)));
		
		return parser.getReport();
	}
	

	private Column parseColumn(XMLAttributes attribs) {
		
		Column column = new Column();
		String source = attribs.getValue("source");
		
		if(source == null) {
			throw new XNIException("source attrib is required");
		}

        column.setProperty(source);
		column.setLabel( attribs.getValue("label") );

        if(attribs.getValue("sourceId") != null) {
            column.setPropertyQualifyingId(Integer.parseInt(attribs.getValue("sourceId")));
        }
		
		if("descending".equals(attribs.getValue("order"))) {
			column.setOrderAscending(false);
		} else {
			column.setOrderAscending(true);
		}
		
		return column;
		
	}

	
}


