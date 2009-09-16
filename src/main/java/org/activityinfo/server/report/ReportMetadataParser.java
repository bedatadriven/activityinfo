package org.activityinfo.server.report;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.activityinfo.shared.dto.ReportParameterDTO;
import org.activityinfo.shared.dto.ReportTemplateDTO;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class ReportMetadataParser extends SAXParser {

	private ReportTemplateDTO report;
	private String currentText = null;
	private boolean inBody = false;
	private Map<String, Integer> typeNames;
	private Map<String, Integer> dateUnitNames; 
	
	public ReportMetadataParser() {
		typeNames = new HashMap<String, Integer>();
		typeNames.put("database", ReportParameterDTO.TYPE_DATABASE);
		typeNames.put("activity", ReportParameterDTO.TYPE_ACTIVITY);
		typeNames.put("attribute", ReportParameterDTO.TYPE_ATTRIBUTE);
		typeNames.put("indicator", ReportParameterDTO.TYPE_INDICATOR);
		typeNames.put("date", ReportParameterDTO.TYPE_DATE);
		
		
		dateUnitNames = new HashMap<String, Integer>();
		dateUnitNames.put("month", ReportParameterDTO.UNIT_MONTH);
		dateUnitNames.put("year", ReportParameterDTO.UNIT_YEAR);
		dateUnitNames.put("day", ReportParameterDTO.UNIT_DAY);
		dateUnitNames.put("quarter", ReportParameterDTO.UNIT_QUARTER);
		
	}

	public void parse(ReportTemplateDTO report, String xml) throws SAXException, IOException {
		this.report = report;
		
		parse(new InputSource(new StringReader(xml)));
	}

	@Override
	public void startElement(QName name, XMLAttributes attributes, Augmentations aug)
			throws XNIException {
		
		super.startElement(name, attributes, aug);
		
		if(name.localpart.equals("report")) {
			
			inBody = false;
			
		} else if(name.localpart.equals("parameter")) {
			
			ReportParameterDTO param = new ReportParameterDTO(
					attributes.getValue("name"),
					attributes.getValue("label"),
					typeNames.get(attributes.getValue("type")));

			if(param.getType() == ReportParameterDTO.TYPE_DATE &&
					attributes.getValue("date-unit") != null ) {
				
				param.setDateUnit( dateUnitNames.get(attributes.getValue("date-unit")));
				
			}
			
			report.addParameter(param);
			
		} else if (name.localpart.equals("body")) {
			
			inBody = true;
		
			
		}
		
		currentText = null;
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
	public void endElement(QName name, Augmentations augs) throws XNIException {

		super.endElement(name, augs);
		
		if(!inBody) {
			if(name.localpart.equals("title")) {
				report.setTitle(currentText);
			} else if(name.localpart.equals("description")) {
				report.setDescription(currentText);
			}
		}
	}
	
	public static ReportTemplateDTO parseIntoModel(String xml) throws SAXException, IOException {
		ReportTemplateDTO dto = new ReportTemplateDTO();
		ReportMetadataParser parser = new ReportMetadataParser();
		parser.parse(dto, xml);
		
		return dto;
	}
}
