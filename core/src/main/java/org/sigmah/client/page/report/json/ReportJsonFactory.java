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
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.layers.MapLayer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ReportJsonFactory implements ReportSerializer {

	private final JsonParser parser;

	public ReportJsonFactory() {
		parser = new JsonParser();
	}

	@Override
	public String serialize(Report report) {
		JsonObject jReport = new JsonObject();

		// write custom maker
		jReport.addProperty("id", (Integer) report.getId());
		if (report.getDescription() != null) {
			jReport.addProperty("description",report.getDescription());
		}
		if (report.getFileName() != null) {
			jReport.addProperty("fileName", report.getFileName());
		}
		if (report.getFrequency() != null) {
			jReport.addProperty("frequency", report.getFrequency().toString());
		}
		if (report.getDay() != null) {
			jReport.addProperty("day", (Integer) report.getDay());
		}
		if (report.getElements() != null) {

			List<ReportElement> reportElements = report.getElements();

			JsonObject jElements = new JsonObject();

			for (int i = 0; i < reportElements.size(); i++) {

				ReportElement rp = report.getElement(i);

				if (rp instanceof PivotTableReportElement) {
					jElements
							.add("element" + i,
									encodePivotTableReportElement((PivotTableReportElement) rp));
				} else if (rp instanceof PivotChartReportElement) {
					jElements
							.add("element" + i,
									encodePivotChartReportElement((PivotChartReportElement) rp));
				} else if (rp instanceof MapReportElement) {
					jElements.add("element" + i,
							encodeMapReportElement((MapReportElement) rp));
				}
			}
			jReport.addProperty("elements", jElements.toString());
		}

		return jReport.toString();
	}

	@Override
	public Report deserialize(String json) {

		JsonElement jsonElement = parser.parse(json);

		JsonObject object = jsonElement.getAsJsonObject();

		int id = object.get("id").getAsInt();
		object.get("filters").getAsJsonArray();

		// JsonArray array = jsonElement.getAsJsonArray();
		// loop through all elements

		Report report = new Report();

		return report;
	}

	public JsonElement encodePivotTableReportElement(PivotTableReportElement rp) {

		JsonObject element = new JsonObject();

		element.addProperty("type", "pivotTable");
		element.addProperty("title", (String) rp.getTitle());
		element.addProperty("sheetTitle", (String) rp.getSheetTitle());
		element.addProperty("filter", encodeFilter(rp.getFilter()));
		element.addProperty("columnDimensions",
				encodeDimensionList(((PivotTableReportElement) rp)
						.getColumnDimensions()));
		element.addProperty("rowDimensions",
				encodeDimensionList(((PivotTableReportElement) rp)
						.getRowDimensions()));

		return element;
	}

	public JsonElement encodePivotChartReportElement(PivotChartReportElement rp) {

		JsonObject element = new JsonObject();

		element.addProperty("type", "pivotChart");
		element.addProperty("title", (String) rp.getTitle());
		element.addProperty("type", ((PivotChartReportElement) rp).getType()
				.toString());
		element.addProperty("sheetTitle", (String) rp.getSheetTitle());
		element.addProperty("categoryAxisTitle",
				((PivotChartReportElement) rp).getCategoryAxisTitle());
		element.addProperty("valueAxisTitle",
				((PivotChartReportElement) rp).getValueAxisTitle());

		element.addProperty("filter", encodeFilter(rp.getFilter()));
		element.addProperty("categoryDimensions",
				encodeDimensionList(((PivotChartReportElement) rp)
						.getCategoryDimensions()));
		element.addProperty("seriesDimension",
				encodeDimensionList(((PivotChartReportElement) rp)
						.getSeriesDimension()));

		return element;
	}

	public JsonElement encodeMapReportElement(MapReportElement rp) {

		JsonObject element = new JsonObject();

		element.addProperty("type", "map");
		element.addProperty("title", (String) rp.getTitle());
		element.addProperty("sheetTitle", (String) rp.getSheetTitle());
		element.addProperty("baseMapId", ((MapReportElement) rp).getBaseMapId());
		element.addProperty("width",
				(Integer) ((MapReportElement) rp).getWidth());
		element.addProperty("height",
				(Integer) ((MapReportElement) rp).getHeight());
		element.addProperty("zoomLevel",
				(Integer) ((MapReportElement) rp).getZoomLevel());
		element.addProperty("center", ((MapReportElement) rp).getCenter()
				.toString());
		element.addProperty("layers",
				encodeLayers(((MapReportElement) rp).getLayers()));
		element.addProperty("filter", encodeFilter(rp.getFilter()));

		return element;
	}

	public String encodeFilter(Filter filter) {

		JsonObject jFilter = new JsonObject();
		jFilter.addProperty("dateRange", filter.getDateRange().toString());
		jFilter.addProperty("isOr", (Boolean) filter.isOr());
		jFilter.addProperty("restrictions",
				encodeRestrictions(filter.getRestrictions()));

		return jFilter.toString();
	}

	public String encodeRestrictions(
			Map<DimensionType, Set<Integer>> restrictions) {

		JsonObject jRestrictions = new JsonObject();
		for (Entry<DimensionType, Set<Integer>> entry : restrictions.entrySet()) {
			jRestrictions.addProperty(entry.getKey().toString(), entry
					.getValue().toString());
		}

		return jRestrictions.toString();
	}

	public String encodeDimensionList(List<Dimension> dims) {

		JsonObject jDims = new JsonObject();
		for (int i = 0; i < dims.size(); i++) {
			Dimension colDim = dims.get(i);
			JsonObject jDim = new JsonObject();
			jDim.addProperty("type", colDim.getType().toString());
			jDim.addProperty("color", colDim.getColor());
			jDim.addProperty("categories",
					encodeCategories(colDim.getCategories()));

			jDims.add("Dimension" + i, jDim);
		}

		return jDims.toString();
	}

	public String encodeCategories(
			Map<DimensionCategory, CategoryProperties> cats) {

		JsonObject jCats = new JsonObject();
		for (Entry<DimensionCategory, CategoryProperties> entry : cats
				.entrySet()) {
			jCats.addProperty(entry.getKey().toString(), entry.getValue()
					.toString());
		}
		return jCats.toString();
	}

	public String encodeLayers(List<MapLayer> layers) {
		JsonObject jLayers = new JsonObject();
		for (int i = 0; i < layers.size(); i++) {
			MapLayer layer = layers.get(i);
			JsonObject jLayer = new JsonObject();

			jLayer.addProperty("name", layer.getName());
			jLayer.addProperty("name", layer.getTypeName());
			jLayer.addProperty("filter", encodeFilter(layer.getFilter()));
			jLayer.addProperty("indicatorIds", layer.getIndicatorIds()
					.toString());
			jLayer.addProperty("cluster", (Boolean) layer.getClustering()
					.isClustered());

			jLayers.add("layer" + i, jLayer);
		}

		return jLayers.toString();
	}

}
