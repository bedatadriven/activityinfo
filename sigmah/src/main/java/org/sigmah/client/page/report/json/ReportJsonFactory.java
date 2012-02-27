package org.sigmah.client.page.report.json;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.sigmah.shared.command.Filter;
import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.model.CategoryProperties;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class ReportJsonFactory implements ReportSerializer {

	private final JsonParser parser;

	public ReportJsonFactory(){
		 parser = new JsonParser();
	}

	@Override
	public String serialize(Report report) {
		JsonObject jReport = new JsonObject();
    
		// write custom maker
		jReport.addProperty("id", (Integer)report.getId());
		if(report.getDescription() != null){
	        jReport.addProperty("description", (String)report.getDescription());
		}
		if(report.getFileName() != null){
	        jReport.addProperty("fileName", (String)report.getFileName());
		}
		if(report.getFrequency() != null){
			jReport.addProperty("frequency", report.getFrequency().toString());
		}
        if(report.getDay()!= null){
        	jReport.addProperty("day", (Integer)report.getDay());	
        }
        if(report.getElements() != null){
        	
        	List<ReportElement> reportElements = report.getElements();
        	
        	for(ReportElement rp : reportElements){
        		
        		JsonObject element = new JsonObject();
        		if(rp instanceof PivotTableReportElement){
            		element.addProperty("type", "pivotTable");
                	element.addProperty("title", (String)rp.getTitle());
                	element.addProperty("sheetTitle", (String)rp.getSheetTitle());
					element.addProperty("filter", encodeFilter(rp.getFilter()));
                	element.addProperty("columnDimensions", encodeColumnDimensions(((PivotTableReportElement) rp).getColumnDimensions()));
                	element.addProperty("rowDimensions", encodeRowDimensions(((PivotTableReportElement) rp).getRowDimensions()));
            	} else if(rp instanceof PivotChartReportElement){
            		element.addProperty("type", "pivotChart");
                	element.addProperty("title", (String)rp.getTitle());
                	element.addProperty("sheetTitle", (String)rp.getSheetTitle());
					element.addProperty("filter", encodeFilter(rp.getFilter()));
                	//TODO remaining elements
            	}
        		
        		jReport.add("elements", element);
        	}
        	
        	
        }
		return jReport.toString();
	}

	@Override
	public Report deserialize(String json) {

		JsonElement jsonElement = parser.parse(json);
		
JsonObject object = jsonElement.getAsJsonObject();
		
		int id = object.get("id").getAsInt();
		object.get("filters").getAsJsonArray();
		
	//	JsonArray array = jsonElement.getAsJsonArray();
		// loop through all elements
		
		
		
		Report report = new Report();
		
		return report;
	}
	
	public String encodeFilter(Filter filter){
		
		JsonObject jFilter = new JsonObject();
		jFilter.addProperty("dateRange", filter.getDateRange().toString());
		jFilter.addProperty("isOr", (Boolean)filter.isOr());
		jFilter.addProperty("restrictions", encodeRestrictions(filter.getRestrictions()));
		
		return jFilter.toString();
	}
	
	public String encodeRestrictions(Map<DimensionType, Set<Integer>> restrictions){
		
		JsonObject jRestrictions = new JsonObject();
		for(Entry<DimensionType, Set<Integer>>  entry : restrictions.entrySet()){	
			jRestrictions.addProperty(entry.getKey().toString(), entry.getValue().toString());
		}
		
		return jRestrictions.toString();		
	}

	public String encodeColumnDimensions(List<Dimension> colDims){
		
		JsonObject jColDims = new JsonObject();
		for(int i = 0; i < colDims.size(); i++){
			Dimension colDim = colDims.get(i);
			JsonObject jDim = new JsonObject();
			jDim.addProperty("type", colDim.getType().toString());
			jDim.addProperty("color", colDim.getColor());
			jDim.addProperty("categories", encodeCategories(colDim.getCategories()));
			
			jColDims.add(String.valueOf(i), jDim);
		}
		
		return jColDims.toString();
	}
	

	private String encodeRowDimensions(List<Dimension> rowDims) {
		
		JsonObject jRowDims = new JsonObject();
		for(int i = 0; i < rowDims.size(); i++){
			Dimension rowDim = rowDims.get(i);
			JsonObject jDim = new JsonObject();
			jDim.addProperty("type", rowDim.getType().toString());
			jDim.addProperty("color", rowDim.getColor());
			jDim.addProperty("categories", encodeCategories(rowDim.getCategories()));
			
			jRowDims.add(String.valueOf(i), jDim);
		}
		
		return jRowDims.toString();
	}

	public String encodeCategories(Map<DimensionCategory, CategoryProperties> cats){
		
		JsonObject jCats = new JsonObject();
		for(Entry<DimensionCategory, CategoryProperties> entry : cats.entrySet()){
			jCats.addProperty(entry.getKey().toString(), entry.getValue().toString());
		}
		return jCats.toString();
	}
	
	
	
}
