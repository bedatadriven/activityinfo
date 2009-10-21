package org.activityinfo.server.report;

import org.activityinfo.shared.dto.ReportTemplateDTO;
import org.activityinfo.shared.report.model.ReportFrequency;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.xni.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


public class ReportMetadataParser extends SAXParser {

	private ReportTemplateDTO report;
	private String currentText = null;
	private boolean inBody = false;
	private Map<String, Integer> frequencyNames;

    public ReportMetadataParser() {

		frequencyNames = new HashMap<String, Integer>();
		frequencyNames.put("monthly", ReportFrequency.MONTHLY);
		frequencyNames.put("weekly", ReportFrequency.WEEKLY);
		frequencyNames.put("day", ReportFrequency.DAILY);
		frequencyNames.put("none", ReportFrequency.NOT_DATE_BOUND);
        frequencyNames.put("adhoc", ReportFrequency.ADHOC);
		
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

            String freqString = attributes.getValue("frequency");
            if(freqString != null) {
                report.setFrequency(frequencyNames.get(freqString));

               String day = attributes.getValue("day");
                if(report.getFrequency() == ReportFrequency.MONTHLY) {
                    if("last".equals(day)) {
                        report.setDay(ReportFrequency.LAST_DAY_OF_MONTH);
                    } else if(day !=null) {
                        report.setDay(Integer.parseInt(day));
                    } else {
                        report.setDay(1);
                    }
                } else if(report.getFrequency() == ReportFrequency.WEEKLY) {
                    report.setDay(Integer.parseInt(day));
                }

            }

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
