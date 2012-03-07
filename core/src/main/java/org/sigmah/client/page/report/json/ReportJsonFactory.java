package org.sigmah.client.page.report.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.sigmah.shared.command.Filter;
import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.model.CategoryProperties;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.ReportFrequency;
import org.sigmah.shared.report.model.clustering.AutomaticClustering;
import org.sigmah.shared.report.model.clustering.NoClustering;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.IconMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;
import org.sigmah.shared.report.model.layers.PiechartMapLayer.Slice;
import org.sigmah.shared.report.model.layers.ScalingType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class ReportJsonFactory implements ReportSerializer {

	private final JsonParser parser;

	public ReportJsonFactory() {
		parser = new JsonParser();
	}

	@Override
	public String serialize(Report report) {
		JsonObject jsonReport = new JsonObject();

		// write custom maker
		jsonReport.addProperty("id", (Integer) report.getId());
		jsonReport.addProperty("title", report.getTitle());
		if (report.getSheetTitle() != null) {
			jsonReport.addProperty("sheetTitle", report.getSheetTitle());
		}
		if (report.getDescription() != null) {
			jsonReport.addProperty("description", report.getDescription());
		}
		if (report.getFileName() != null) {
			jsonReport.addProperty("fileName", report.getFileName());
		}
		if (report.getFrequency() != null) {
			jsonReport.addProperty("frequency", report.getFrequency()
					.toString());
		}
		if (report.getDay() != null) {
			jsonReport.addProperty("day", (Integer) report.getDay());
		}
		if (report.getElements() != null) {

			List<ReportElement> reportElements = report.getElements();

			JsonArray jsonElements = new JsonArray();

			for (int i = 0; i < reportElements.size(); i++) {

				ReportElement rp = report.getElement(i);

				if (rp instanceof PivotTableReportElement) {
					jsonElements
							.add(encodePivotTableReportElement((PivotTableReportElement) rp));
				} else if (rp instanceof PivotChartReportElement) {
					jsonElements
							.add(encodePivotChartReportElement((PivotChartReportElement) rp));
				} else if (rp instanceof MapReportElement) {
					jsonElements
							.add(encodeMapReportElement((MapReportElement) rp));
				}
			}
			jsonReport.add("elements", jsonElements);
		}

		return jsonReport.toString();
	}

	@Override
	public Report deserialize(String json) {

		JsonObject jsonObject = (JsonObject) parser.parse(json);

		Report report = new Report();

		report.setId(jsonObject.get("id").getAsInt());
		report.setTitle(jsonObject.get("title").getAsString());

		JsonElement sheetTitle = jsonObject.get("sheetTitle");
		if (sheetTitle != null) {
			report.setSheetTitle(sheetTitle.getAsString());
		}
		JsonElement description = jsonObject.get("description");
		if (description != null) {
			report.setDescription(description.getAsString());
		}
		JsonElement fileName = jsonObject.get("fileName");
		if (fileName != null) {
			report.setFileName(fileName.getAsString());
		}
		JsonElement frequency = jsonObject.get("frequency");
		if (frequency != null) {
			report.setFrequency(ReportFrequency.valueOf(frequency.getAsString()));
		}
		JsonElement day = jsonObject.get("day");
		if (day != null) {
			report.setDay(day.getAsInt());
		}
		JsonElement elements = jsonObject.get("elements");
		if (elements != null) {
			report.setElements(decodeElements(elements));
		}

		return report;
	}

	public JsonElement encodePivotTableReportElement(PivotTableReportElement rp) {

		JsonObject element = new JsonObject();

		element.addProperty("elementType", "pivotTable");
		if (rp.getTitle() != null) {
			element.addProperty("title", (String) rp.getTitle());
		}
		if (rp.getSheetTitle() != null) {
			element.addProperty("sheetTitle", (String) rp.getSheetTitle());
		}
		element.addProperty("filter", encodeFilter(rp.getFilter()));
		if (rp.getColumnDimensions() != null) {
			element.addProperty("columnDimensions",
					encodeDimensionList(((PivotTableReportElement) rp)
							.getColumnDimensions()));
		} else {
			element.addProperty("columnDimensions", new JsonObject().toString());
		}

		element.addProperty("rowDimensions",
				encodeDimensionList(((PivotTableReportElement) rp)
						.getRowDimensions()));

		return element;
	}

	public JsonElement encodePivotChartReportElement(PivotChartReportElement rp) {

		JsonObject element = new JsonObject();

		element.addProperty("elementType", "pivotChart");
		if (rp.getTitle() != null) {
			element.addProperty("title", (String) rp.getTitle());
		}
		if (rp.getSheetTitle() != null) {
			element.addProperty("sheetTitle", (String) rp.getSheetTitle());
		}
		if (rp.getType() != null) {
			element.addProperty("type", ((PivotChartReportElement) rp)
					.getType().toString());
		}
		if (rp.getCategoryAxisTitle() != null) {
			element.addProperty("categoryAxisTitle",
					((PivotChartReportElement) rp).getCategoryAxisTitle());
		}
		if (rp.getValueAxisTitle() != null) {
			element.addProperty("valueAxisTitle",
					((PivotChartReportElement) rp).getValueAxisTitle());
		}
		element.addProperty("filter", encodeFilter(rp.getFilter()));
		if (rp.getCategoryDimensions().size() > 0
				&& rp.getCategoryDimensions().get(0) != null) {
			element.addProperty("categoryDimensions",
					encodeDimensionList(((PivotChartReportElement) rp)
							.getCategoryDimensions()));
		} else {
			element.addProperty("categoryDimensions",
					new JsonObject().toString());
		}

		element.addProperty("seriesDimensions",
				encodeDimensionList(((PivotChartReportElement) rp)
						.getSeriesDimension()));

		return element;
	}

	public JsonElement encodeMapReportElement(MapReportElement rp) {

		JsonObject element = new JsonObject();

		element.addProperty("elementType", "map");
		if (rp.getTitle() != null) {
			element.addProperty("title", (String) rp.getTitle());
		}
		if (rp.getSheetTitle() != null) {
			element.addProperty("sheetTitle", (String) rp.getSheetTitle());
		}
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

		JsonObject jsonFilter = new JsonObject();
		if (filter.getMinDate() != null) {
			jsonFilter.addProperty("minDate", filter.getMinDate().getTime());
		}
		if (filter.getMaxDate() != null) {
			jsonFilter.addProperty("maxDate", filter.getMaxDate().getTime());
		}
		jsonFilter.addProperty("isOr", (Boolean) filter.isOr());
		jsonFilter.addProperty("restrictions",
				encodeRestrictions(filter.getRestrictions()));

		return jsonFilter.toString();
	}

	public String encodeRestrictions(
			Map<DimensionType, Set<Integer>> restrictions) {

		JsonArray jsonRestrictions = new JsonArray();

		for (Entry<DimensionType, Set<Integer>> entry : restrictions.entrySet()) {
			JsonObject jsonEntry = new JsonObject();
			jsonEntry.addProperty("type", entry.getKey().toString());

			Set<Integer> set = entry.getValue();
			JsonArray jsonSet = new JsonArray();
			for (Integer val : set) {
				jsonSet.add(new JsonPrimitive(val));
			}
			jsonEntry.add("set", jsonSet);

			jsonRestrictions.add(jsonEntry);
		}

		return jsonRestrictions.toString();
	}

	public String encodeDimensionList(List<Dimension> dims) {

		JsonArray jsonDims = new JsonArray();
		for (int i = 0; i < dims.size(); i++) {
			Dimension colDim = dims.get(i);
			JsonObject jsonDim = new JsonObject();
			jsonDim.addProperty("type", colDim.getType().toString());
			if (colDim.getColor() != null) {
				jsonDim.addProperty("color", colDim.getColor());
			}
			if (!colDim.getCategories().isEmpty()) {
				jsonDim.addProperty("categories",
						encodeCategories(colDim.getCategories()));
			}

			jsonDims.add(jsonDim);
		}

		return jsonDims.toString();
	}

	public String encodeCategories(
			Map<DimensionCategory, CategoryProperties> categories) {

		JsonArray jsonCats = new JsonArray();

		for (Entry<DimensionCategory, CategoryProperties> entry : categories
				.entrySet()) {
			JsonObject jsonEntry = new JsonObject();
			jsonEntry.addProperty("dimensionCategory", entry.getKey()
					.toString());
			jsonEntry.addProperty("categoryProperties", entry.getValue()
					.toString());

			jsonCats.add(jsonEntry);
		}

		return jsonCats.toString();
	}

	public String encodeLayers(List<MapLayer> layers) {
		JsonArray jsonLayers = new JsonArray();
		for (int i = 0; i < layers.size(); i++) {
			MapLayer layer = layers.get(i);
			JsonObject jsonLayer = new JsonObject();
			if (layer instanceof BubbleMapLayer) {
				jsonLayer.addProperty("layerType", layer.getTypeName());

				jsonLayer.addProperty("colorDimensions",
						encodeDimensionList(((BubbleMapLayer) layer)
								.getColorDimensions()));
				jsonLayer.addProperty("bubbleColor",
						((BubbleMapLayer) layer).getBubbleColor());
				jsonLayer.addProperty("labelColor",
						((BubbleMapLayer) layer).getLabelColor());

				jsonLayer.addProperty("minRadius",
						(Integer) ((BubbleMapLayer) layer).getMinRadius());
				jsonLayer.addProperty("maxRadius",
						(Integer) ((BubbleMapLayer) layer).getMaxRadius());
				jsonLayer.addProperty("alpha",
						(Double) ((BubbleMapLayer) layer).getAlpha());
				jsonLayer.addProperty("scaling", ((BubbleMapLayer) layer)
						.getScaling().toString());

			} else if (layer instanceof PiechartMapLayer) {
				jsonLayer.addProperty("layerType", layer.getTypeName());

				jsonLayer
						.addProperty("slices",
								encodeSlicesList(((PiechartMapLayer) layer)
										.getSlices()));

				jsonLayer.addProperty("minRadius",
						(Integer) ((PiechartMapLayer) layer).getMinRadius());
				jsonLayer.addProperty("maxRadius",
						(Integer) ((PiechartMapLayer) layer).getMaxRadius());
				jsonLayer.addProperty("alpha",
						(Double) ((PiechartMapLayer) layer).getAlpha());
				jsonLayer.addProperty("scaling", ((PiechartMapLayer) layer)
						.getScaling().toString());

			} else if (layer instanceof IconMapLayer) {
				jsonLayer.addProperty("layerType", layer.getTypeName());

				jsonLayer.addProperty("activityIds",
						encodeIntegerList(((IconMapLayer) layer)
								.getActivityIds()));
				jsonLayer.addProperty("icon", ((IconMapLayer) layer).getIcon());

			}
			jsonLayer.addProperty("isVisible", (Boolean) layer.isVisible());
			jsonLayer.addProperty("indicatorIds",
					encodeIntegerList(layer.getIndicatorIds()));
			jsonLayer.addProperty("labelSequence", layer.getLabelSequence()
					.next());
			jsonLayer.addProperty("cluster", (Boolean) layer.isClustered());
			jsonLayer.addProperty("name", layer.getName());
			jsonLayer.addProperty("filter", encodeFilter(layer.getFilter()));

			jsonLayers.add(jsonLayer);
		}

		return jsonLayers.toString();
	}

	private String encodeSlicesList(List<Slice> slices) {

		JsonArray jsonSlices = new JsonArray();
		for (int i = 0; i < slices.size(); i++) {
			Slice slice = slices.get(i);
			JsonObject jsonSlice = new JsonObject();
			if (slice.getColor() != null) {
				jsonSlice.addProperty("color", slice.getColor());
			}
			jsonSlice.addProperty("indicatorId", slice.getIndicatorId());

			jsonSlices.add(jsonSlice);
		}

		return jsonSlices.toString();
	}

	private String encodeIntegerList(List<Integer> indicatorIds) {

		JsonArray jsonIntList = new JsonArray();
		for (int i = 0; i < indicatorIds.size(); i++) {
			Integer integer = indicatorIds.get(i);
			jsonIntList.add(new JsonPrimitive(integer));
		}

		return jsonIntList.toString();
	}

	private List<ReportElement> decodeElements(JsonElement elements) {

		JsonArray elementArray = (JsonArray) parser.parse(elements.toString());
		for (int i = 0; i < elementArray.size(); i++) {
			JsonObject element = (JsonObject) elementArray.get(i);
			String type = element.get("elementType").getAsString();

			if ("pivotTable".equals(type)) {
				decodePivotTableReportElement(element);
			} else if ("pivotChart".equals(type)) {
				decodePivotChartReportElement(element);
			} else if ("map".equals(type)) {
				decodeMapReportElement(element);
			}
		}

		return null;
	}

	public PivotTableReportElement decodePivotTableReportElement(
			JsonObject element) {

		PivotTableReportElement pivotTableElement = new PivotTableReportElement();

		JsonElement title = element.get("title");
		if (title != null) {
			pivotTableElement.setTitle(title.getAsString());
		}
		JsonElement sheetTitle = element.get("sheetTitle");
		if (sheetTitle != null) {
			pivotTableElement.setSheetTitle(sheetTitle.getAsString());
		}
		JsonElement filter = element.get("filter");
		pivotTableElement.setFilter(decodeFilter(filter));

		JsonElement colDims = element.get("columnDimensions");
		if (colDims != null) {
			pivotTableElement.setColumnDimensions(decodeDimensionList(colDims));

		}
		JsonElement rowDims = element.get("rowDimensions");
		if (rowDims != null) {
			pivotTableElement.setRowDimensions(decodeDimensionList(rowDims));
		}

		return pivotTableElement;
	}

	public PivotChartReportElement decodePivotChartReportElement(
			JsonObject element) {

		PivotChartReportElement pivotChartElement = new PivotChartReportElement();

		JsonElement title = element.get("title");
		if (title != null) {
			pivotChartElement.setTitle(title.getAsString());
		}
		JsonElement sheetTitle = element.get("sheetTitle");
		if (sheetTitle != null) {
			pivotChartElement.setSheetTitle(sheetTitle.getAsString());
		}
		JsonElement type = element.get("type");
		if (type != null) {
			pivotChartElement.setType(PivotChartReportElement.Type.valueOf(type
					.getAsString()));
		}
		JsonElement categoryAxisTitle = element.get("categoryAxisTitle");
		if (categoryAxisTitle != null) {
			pivotChartElement.setCategoryAxisTitle(categoryAxisTitle
					.getAsString());
		}
		JsonElement valueAxisTitle = element.get("valueAxisTitle");
		if (valueAxisTitle != null) {
			pivotChartElement.setValueAxisTitle(valueAxisTitle.getAsString());
		}
		JsonElement filter = element.get("filter");
		pivotChartElement.setFilter(decodeFilter(filter));
		JsonElement categoryDimensions = element.get("categoryDimensions");
		if (categoryDimensions != null) {
			pivotChartElement
					.setCategoryDimensions(decodeDimensionList(categoryDimensions));
		}
		JsonElement seriesDimensions = element.get("seriesDimensions");
		if (seriesDimensions != null) {
			pivotChartElement
					.setSeriesDimension(decodeDimensionList(seriesDimensions));
		}

		return pivotChartElement;
	}

	public MapReportElement decodeMapReportElement(JsonObject element) {

		MapReportElement mapElement = new MapReportElement();
		JsonElement title = element.get("title");
		if (title != null) {
			mapElement.setTitle(title.getAsString());
		}
		JsonElement sheetTitle = element.get("sheetTitle");
		if (sheetTitle != null) {
			mapElement.setSheetTitle(sheetTitle.getAsString());
		}
		JsonElement baseMapId = element.get("baseMapId");
		if (baseMapId != null) {
			mapElement.setBaseMapId(baseMapId.getAsString());
		}
		JsonElement width = element.get("width");
		if (width != null) {
			mapElement.setWidth(width.getAsInt());
		}
		JsonElement height = element.get("height");
		if (height != null) {
			mapElement.setHeight(height.getAsInt());
		}
		JsonElement zoomLevel = element.get("zoomLevel");
		if (zoomLevel != null) {
			mapElement.setBaseMapId(zoomLevel.getAsString());
		}
		JsonElement center = element.get("center");
		if (center != null) {
			mapElement.setBaseMapId(center.getAsString());
		}
		JsonElement layers = element.get("layers");
		if (layers != null) {
			mapElement.setLayers(decodeLayers(layers));
		}
		JsonElement filter = element.get("filter");
		mapElement.setFilter(decodeFilter(filter));

		return mapElement;
	}

	public Filter decodeFilter(JsonElement filter) {

		JsonObject jsonFilter = (JsonObject) parser.parse(filter.getAsString());
		Filter elementFilter = new Filter();

		JsonElement jsonMinDate = jsonFilter.get("minDate");
		JsonElement jsonMaxDate = jsonFilter.get("maxDate");
		DateRange dateRange = new DateRange();
		if (jsonMinDate != null) {
			long min = jsonMinDate.getAsLong();
			Date minDate = new Date(jsonMinDate.getAsLong());
			dateRange.setMinDate(minDate);
		}
		if (jsonMaxDate != null) {
			Date maxDate = new Date(jsonMaxDate.getAsLong());
			dateRange.setMaxDate(maxDate);
		}

		elementFilter.setDateRange(dateRange);
		elementFilter.setOr(jsonFilter.get("isOr").getAsBoolean());

		JsonArray restrictions = (JsonArray) parser.parse(jsonFilter.get(
				"restrictions").getAsString());
		for (int i = 0; i < restrictions.size(); i++) {
			JsonObject rest = (JsonObject) restrictions.get(i);
			JsonArray arr = rest.get("set").getAsJsonArray();
			Set<Integer> set = new HashSet<Integer>();
			Iterator<JsonElement> it = arr.iterator();

			while (it.hasNext()) {
				set.add(it.next().getAsInt());
			}
			elementFilter.addRestriction(
					DimensionType.valueOf(rest.get("type").getAsString()), set);

		}

		return elementFilter;
	}

	public List<Dimension> decodeDimensionList(JsonElement dimensions) {
		JsonArray jsonDimList = (JsonArray) parser.parse(dimensions
				.getAsString());

		Iterator<JsonElement> it = jsonDimList.iterator();
		List<Dimension> dimensionsList = new ArrayList<Dimension>();
		while (it.hasNext()) {
			JsonObject dim = it.next().getAsJsonObject();

			Dimension dimension = new Dimension(DimensionType.valueOf(dim.get(
					"type").getAsString()));
			JsonElement color = dim.get("color");
			if (color != null) {
				dimension.setColor(color.getAsString());
			}
			JsonElement categories = dim.get("categories");
			if (categories != null) {
				dimension.setCategories(decodeCategories(categories));
			}

			dimensionsList.add(dimension);
		}
		return dimensionsList;
	}

	public Map<DimensionCategory, CategoryProperties> decodeCategories(
			JsonElement categories) {

		Map<DimensionCategory, CategoryProperties> cats = new HashMap<DimensionCategory, CategoryProperties>();
		JsonArray jsonCats = (JsonArray) parser.parse(categories.getAsString());
		Iterator<JsonElement> it = jsonCats.iterator();
		while (it.hasNext()) {
			JsonObject cat = it.next().getAsJsonObject();
			// TODO get categories from Json to Map
		}

		return cats;
	}

	public List<MapLayer> decodeLayers(JsonElement layers) {
		JsonArray jsonLayers = (JsonArray) parser.parse(layers.getAsString());
		Iterator<JsonElement> it = jsonLayers.iterator();

		List<MapLayer> mapLayers = new ArrayList<MapLayer>();

		while (it.hasNext()) {
			JsonObject jsonLayer = it.next().getAsJsonObject();

			if ("Bubble".equals(jsonLayer.get("type"))) {
				BubbleMapLayer layer = new BubbleMapLayer();

				JsonElement colorDimensions = jsonLayer.get("colorDimensions");
				if (colorDimensions != null) {
					layer.setColorDimensions(decodeDimensionList(colorDimensions));
				}
				JsonElement bubbleColor = jsonLayer.get("bubbleColor");
				if (bubbleColor != null) {
					layer.setBubbleColor(bubbleColor.getAsString());
				}
				JsonElement labelColor = jsonLayer.get("labelColor");
				if (labelColor != null) {
					layer.setLabelColor(labelColor.getAsString());
				}
				JsonElement minRadius = jsonLayer.get("minRadius");
				if (minRadius != null) {
					layer.setMinRadius(minRadius.getAsInt());
				}
				JsonElement maxRadius = jsonLayer.get("maxRadius");
				if (maxRadius != null) {
					layer.setMaxRadius(maxRadius.getAsInt());
				}
				JsonElement alpha = jsonLayer.get("alpha");
				if (alpha != null) {
					layer.setAlpha(alpha.getAsDouble());
				}
				JsonElement scaling = jsonLayer.get("scaling");
				if (scaling != null) {
					layer.setScaling(ScalingType.valueOf(scaling.getAsString()));
				}

				layer.setVisible(jsonLayer.get("isVisible").getAsBoolean());
				JsonArray indicators = jsonLayer.get("indicatorIds")
						.getAsJsonArray();
				Iterator<JsonElement> itr = indicators.iterator();
				while (itr.hasNext()) {
					layer.addIndicator(itr.next().getAsInt());
				}

				// TODO implement label sequence

				if (jsonLayer.get("cluster").getAsBoolean()) {
					layer.setClustering(new AutomaticClustering());
				} else {
					layer.setClustering(new NoClustering());
				}

				layer.setName(jsonLayer.get("name").getAsString());
				layer.setFilter(decodeFilter(jsonLayer.get("filter")));

				mapLayers.add(layer);

			} else if ("Piechart".equals(jsonLayer.get("type"))) {
				PiechartMapLayer layer = new PiechartMapLayer();

				JsonElement minRadius = jsonLayer.get("minRadius");
				if (minRadius != null) {
					layer.setMinRadius(minRadius.getAsInt());
				}
				JsonElement maxRadius = jsonLayer.get("maxRadius");
				if (maxRadius != null) {
					layer.setMaxRadius(maxRadius.getAsInt());
				}
				JsonElement alpha = jsonLayer.get("alpha");
				if (alpha != null) {
					layer.setAlpha(alpha.getAsDouble());
				}
				JsonElement scaling = jsonLayer.get("scaling");
				if (scaling != null) {
					layer.setScaling(ScalingType.valueOf(scaling.getAsString()));
				}

				layer.setVisible(jsonLayer.get("isVisible").getAsBoolean());
				JsonArray indicators = jsonLayer.get("indicatorIds")
						.getAsJsonArray();
				Iterator<JsonElement> itr = indicators.iterator();
				while (itr.hasNext()) {
					layer.addIndicatorId(itr.next().getAsInt());
				}

				// TODO implement label sequence

				if (jsonLayer.get("cluster").getAsBoolean()) {
					layer.setClustering(new AutomaticClustering());
				} else {
					layer.setClustering(new NoClustering());
				}

				layer.setName(jsonLayer.get("name").getAsString());
				layer.setFilter(decodeFilter(jsonLayer.get("filter")));

				mapLayers.add(layer);

			} else if ("Bubble".equals(jsonLayer.get("type"))) {
				IconMapLayer layer = new IconMapLayer();

				JsonArray activityIds = jsonLayer.get("activityIds")
						.getAsJsonArray();
				Iterator<JsonElement> activityIrtator = activityIds.iterator();
				while (activityIrtator.hasNext()) {
					layer.addActivityId(activityIrtator.next().getAsInt());
				}
				JsonElement icon = jsonLayer.get("icon");
				if (icon != null) {
					layer.setIcon(icon.getAsString());
				}

				layer.setVisible(jsonLayer.get("isVisible").getAsBoolean());
				JsonArray indicators = jsonLayer.get("indicatorIds")
						.getAsJsonArray();
				Iterator<JsonElement> itr = indicators.iterator();
				while (itr.hasNext()) {
					layer.addIndicatorId(itr.next().getAsInt());
				}

				// TODO implement label sequence

				if (jsonLayer.get("cluster").getAsBoolean()) {
					layer.setClustering(new AutomaticClustering());
				} else {
					layer.setClustering(new NoClustering());
				}

				layer.setName(jsonLayer.get("name").getAsString());
				layer.setFilter(decodeFilter(jsonLayer.get("filter")));

				mapLayers.add(layer);

			}
		}
		return mapLayers;
	}

}
