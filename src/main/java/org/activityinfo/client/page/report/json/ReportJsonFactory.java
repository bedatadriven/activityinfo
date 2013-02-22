package org.activityinfo.client.page.report.json;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.CategoryProperties;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportElement;
import org.activityinfo.shared.report.model.clustering.AutomaticClustering;
import org.activityinfo.shared.report.model.clustering.NoClustering;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.shared.report.model.layers.IconMapLayer;
import org.activityinfo.shared.report.model.layers.MapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer.Slice;
import org.activityinfo.shared.report.model.layers.ScalingType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.inject.Inject;

public class ReportJsonFactory implements ReportSerializer {

	private final JsonParser parser;

	@Inject
	public ReportJsonFactory(JsonParser parser) {
		this.parser = parser;
	}

	public String serialize(Report report) {
		JsonObject jsonReport = new JsonObject();

		// write custom maker
		jsonReport.addProperty("id", (Integer) report.getId());
		if(report.getTitle() != null){
			jsonReport.addProperty("title", report.getTitle());
		}
		if (report.getSheetTitle() != null) {
			jsonReport.addProperty("sheetTitle", report.getSheetTitle());
		}
		if (report.getDescription() != null) {
			jsonReport.addProperty("description", report.getDescription());
		}
		if (report.getFileName() != null) {
			jsonReport.addProperty("fileName", report.getFileName());
		}
	
		jsonReport.add("filter", encodeFilter(report.getFilter()));
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

	public Report deserialize(String json) {

		if (json == null || json.length() < 1) {
			return null;
		}

		JsonObject jsonObject = (JsonObject) parser.parse(json);

		Report report = new Report();

		report.setId(jsonObject.get("id").getAsInt());
		JsonElement title = jsonObject.get("title");
		if (title != null) {
			report.setTitle(title.getAsString());
		}
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
		JsonObject filter = jsonObject.get("filter").getAsJsonObject();
		if(filter != null){
			report.setFilter(decodeFilter(filter));
		}
		JsonArray elements = jsonObject.get("elements").getAsJsonArray();
		if (elements.size() > 0) {
			report.setElements(decodeElements(elements));
		}

		return report;
	}

	public JsonElement encodePivotTableReportElement(PivotTableReportElement rp) {

		JsonObject element = new JsonObject();

		element.addProperty("elementType", "pivotTable");
		if (rp.getTitle() != null) {
			element.addProperty("title", rp.getTitle());
		}
		if (rp.getSheetTitle() != null) {
			element.addProperty("sheetTitle", rp.getSheetTitle());
		}
		element.add("filter", encodeFilter(rp.getFilter()));
		if (rp.getColumnDimensions() != null) {
			element.add("columnDimensions",
					encodeDimensionList(((PivotTableReportElement) rp)
							.getColumnDimensions()));
		} else {
			element.add("columnDimensions", new JsonArray());
		}

		element.add("rowDimensions",
				encodeDimensionList(((PivotTableReportElement) rp)
						.getRowDimensions()));

		return element;
	}

	public JsonElement encodePivotChartReportElement(PivotChartReportElement rp) {

		JsonObject element = new JsonObject();

		element.addProperty("elementType", "pivotChart");
		if (rp.getTitle() != null) {
			element.addProperty("title", rp.getTitle());
		}
		if (rp.getSheetTitle() != null) {
			element.addProperty("sheetTitle", rp.getSheetTitle());
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
		element.add("filter", encodeFilter(rp.getFilter()));
		if (rp.getCategoryDimensions().size() > 0
				&& rp.getCategoryDimensions().get(0) != null) {
			element.add("categoryDimensions",
					encodeDimensionList(((PivotChartReportElement) rp)
							.getCategoryDimensions()));
		}
		else{
			element.add("categoryDimensions", new JsonArray());
		}
		element.add("seriesDimensions",
				encodeDimensionList(((PivotChartReportElement) rp)
						.getSeriesDimension()));

		return element;
	}

	public JsonElement encodeMapReportElement(MapReportElement rp) {

		JsonObject element = new JsonObject();

		element.addProperty("elementType", "map");
		if (rp.getTitle() != null) {
			element.addProperty("title", rp.getTitle());
		}
		if (rp.getSheetTitle() != null) {
			element.addProperty("sheetTitle", rp.getSheetTitle());
		}
		element.addProperty("baseMapId", ((MapReportElement) rp).getBaseMapId());
		element.addProperty("width",
				(Integer) ((MapReportElement) rp).getWidth());
		element.addProperty("height",
				(Integer) ((MapReportElement) rp).getHeight());
		element.addProperty("zoomLevel",
				(Integer) ((MapReportElement) rp).getZoomLevel());
		if(rp.getCenter() !=  null){
			element.addProperty("center", ((MapReportElement) rp).getCenter()
					.toString());
		}
		element.add("layers",
				encodeLayers(((MapReportElement) rp).getLayers()));
		element.add("filter", encodeFilter(rp.getFilter()));

		return element;
	}

	public JsonElement encodeFilter(Filter filter) {

		JsonObject jsonFilter = new JsonObject();
		if (filter.getMinDate() != null) {
			jsonFilter.addProperty("minDate", filter.getMinDate().getTime());
		}
		if (filter.getMaxDate() != null) {
			jsonFilter.addProperty("maxDate", filter.getMaxDate().getTime());
		}
		jsonFilter.add("restrictions",
				encodeRestrictions(filter.getRestrictions()));

		return jsonFilter;
	}

	public JsonArray encodeRestrictions(
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

		return jsonRestrictions;
	}

	public JsonArray encodeDimensionList(List<Dimension> dims) {

		JsonArray jsonDims = new JsonArray();
		for (int i = 0; i < dims.size(); i++) {

			JsonObject jsonDim = new JsonObject();

			DimensionType type = dims.get(i).getType();
			if (type.equals(DimensionType.Date)) {
				DateDimension dim = (DateDimension) dims.get(i);
				jsonDim.addProperty("type", dim.getType().toString());

				jsonDim.addProperty("dateUnit", dim.getUnit().toString());

				if (dim.getColor() != null) {
					jsonDim.addProperty("color", dim.getColor());
				}
				if (!dim.getCategories().isEmpty()) {
					jsonDim.add("categories",
							encodeCategories(dim.getCategories()));
				}
			} else if (type.equals(DimensionType.AdminLevel)) { 
				AdminDimension dim = (AdminDimension) dims.get(i);
				jsonDim.addProperty("type", dim.getType().toString());
				jsonDim.addProperty("level", (Integer)dim.getLevelId());
				
				if (dim.getColor() != null) {
					jsonDim.addProperty("color", dim.getColor());
				}
				if (!dim.getCategories().isEmpty()) {
					jsonDim.add("categories",
							encodeCategories(dim.getCategories()));
				}
			} else {
				Dimension dim = dims.get(i);

				jsonDim.addProperty("type", dim.getType().toString());
			
				if (dim.getColor() != null) {
					jsonDim.addProperty("color", dim.getColor());
				}
				if (!dim.getCategories().isEmpty()) {
					jsonDim.add("categories",
							encodeCategories(dim.getCategories()));
				}
			}

			jsonDims.add(jsonDim);
		}

		return jsonDims;
	}

	public JsonArray encodeCategories(
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

		return jsonCats;
	}

	public JsonArray encodeLayers(List<MapLayer> layers) {
		JsonArray jsonLayers = new JsonArray();
		for (int i = 0; i < layers.size(); i++) {
			MapLayer layer = layers.get(i);
			JsonObject jsonLayer = new JsonObject();
			if (layer instanceof BubbleMapLayer) {
				jsonLayer.addProperty("layerType", layer.getTypeName());

				jsonLayer.add("colorDimensions",
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
						.add("slices",
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

				jsonLayer.add("activityIds",
						encodeIntegerList(((IconMapLayer) layer)
								.getActivityIds()));
				jsonLayer.addProperty("icon", ((IconMapLayer) layer).getIcon());

			}
			jsonLayer.addProperty("isVisible", (Boolean) layer.isVisible());
			jsonLayer.add("indicatorIds",
					encodeIntegerList(layer.getIndicatorIds()));
//			jsonLayer.addProperty("labelSequence", layer.getLabelSequence()
//					.next());
//			jsonLayer.addProperty("cluster", (Boolean) layer.isClustered());
			jsonLayer.addProperty("name", layer.getName());
			jsonLayer.add("filter", encodeFilter(layer.getFilter()));

			jsonLayers.add(jsonLayer);
		}

		return jsonLayers;
	}

	private JsonArray encodeSlicesList(List<Slice> slices) {

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

		return jsonSlices;
	}

	private JsonArray encodeIntegerList(List<Integer> indicatorIds) {

		JsonArray jsonIntList = new JsonArray();
		for (int i = 0; i < indicatorIds.size(); i++) {
			Integer integer = indicatorIds.get(i);
			jsonIntList.add(new JsonPrimitive(integer));
		}

		return jsonIntList;
	}

	private List<ReportElement> decodeElements(JsonArray elements) {
		List<ReportElement> reportElements = new ArrayList<ReportElement>();

		for (int i = 0; i < elements.size(); i++) {
			JsonObject element = (JsonObject) elements.get(i);
			String type = element.get("elementType").getAsString();

			if ("pivotTable".equals(type)) {
				reportElements.add(decodePivotTableReportElement(element));
			} else if ("pivotChart".equals(type)) {
				reportElements.add(decodePivotChartReportElement(element));
			} else if ("map".equals(type)) {
				reportElements.add(decodeMapReportElement(element));
			}
		}

		return reportElements;
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
		JsonObject filter = element.get("filter").getAsJsonObject();
		pivotTableElement.setFilter(decodeFilter(filter));

		JsonArray colDims = element.get("columnDimensions").getAsJsonArray();
		if (colDims.size() > 0) {
			pivotTableElement.setColumnDimensions(decodeDimensionList(colDims));

		}
		JsonArray rowDims = element.get("rowDimensions").getAsJsonArray();
		if (rowDims.size() > 0) {
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
		JsonObject filter = element.get("filter").getAsJsonObject();
		pivotChartElement.setFilter(decodeFilter(filter));
		JsonArray categoryDimensions = element.get("categoryDimensions").getAsJsonArray();
		if (categoryDimensions.size() > 0) {
			pivotChartElement
					.setCategoryDimensions(decodeDimensionList(categoryDimensions));
		}
		JsonArray seriesDimensions = element.get("seriesDimensions").getAsJsonArray();
		if (seriesDimensions.size() > 0) {
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
		JsonArray layers = element.get("layers").getAsJsonArray();
		if (layers.size() > 0) {
			mapElement.setLayers(decodeLayers(layers));
		}
		JsonObject filter = element.get("filter").getAsJsonObject();
		mapElement.setFilter(decodeFilter(filter));

		return mapElement;
	}

	public Filter decodeFilter(JsonObject filter) {

		Filter elementFilter = new Filter();

		JsonElement jsonMinDate = filter.get("minDate");
		JsonElement jsonMaxDate = filter.get("maxDate");
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
		
		JsonArray restrictions = (JsonArray) filter.get(
				"restrictions");

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

	public List<Dimension> decodeDimensionList(JsonArray dimensions) {

		Iterator<JsonElement> it = dimensions.iterator();
		List<Dimension> dimensionsList = new ArrayList<Dimension>();
		while (it.hasNext()) {
			JsonObject dim = it.next().getAsJsonObject();
			String type = dim.get("type").getAsString();
			
			if (type.equals(DimensionType.Date.toString())) {
				String dateUnit = dim.get("dateUnit").getAsString();
				DateDimension dimension = new DateDimension(DateUnit.valueOf(dateUnit));
				JsonElement categories = dim.get("categories");
				if (categories != null) {
					dimension.setCategories(decodeCategories(categories));
				}
				dimensionsList.add(dimension);

			} else if(type.equals(DimensionType.AdminLevel.toString())){
				Integer level = dim.get("level").getAsInt();
				AdminDimension dimension = new AdminDimension(level);
				JsonElement categories = dim.get("categories");
				if (categories != null) {
					dimension.setCategories(decodeCategories(categories));
				}
				
				dimensionsList.add(dimension);
				
			} else
			{
				Dimension dimension = new Dimension(DimensionType.valueOf(dim
						.get("type").getAsString()));
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

	public List<MapLayer> decodeLayers(JsonArray layers) {

		Iterator<JsonElement> it = layers.iterator();

		List<MapLayer> mapLayers = new ArrayList<MapLayer>();

		while (it.hasNext()) {
			JsonObject jsonLayer = it.next().getAsJsonObject();

			if ("Bubble".equals(jsonLayer.get("type"))) {
				BubbleMapLayer layer = new BubbleMapLayer();

				JsonArray colorDimensions = jsonLayer.get("colorDimensions").getAsJsonArray();
				if (colorDimensions.size() > 0) {
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
				layer.setFilter(decodeFilter(jsonLayer.get("filter").getAsJsonObject()));

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
				layer.setFilter(decodeFilter(jsonLayer.get("filter").getAsJsonObject()));

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
				layer.setFilter(decodeFilter(jsonLayer.get("filter").getAsJsonObject()));

				mapLayers.add(layer);

			}
		}
		return mapLayers;
	}

}
