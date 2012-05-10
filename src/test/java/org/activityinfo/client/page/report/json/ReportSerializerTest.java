package org.activityinfo.client.page.report.json;

import java.util.Date;
import java.util.List;

import org.activityinfo.client.page.report.json.ReportJsonFactory;
import org.activityinfo.client.page.report.json.ReportSerializer;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.Report;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class ReportSerializerTest {

	private ReportSerializer reportSerializer;
	ReportJsonFactory factory;
	private Report report;
	Long longDate = Long.valueOf("1331712050839");
	private Date maxDate = new Date(longDate);
	private Date minDate = new Date(longDate);

	@Before
	public void setup() {
		reportSerializer = new ReportJsonFactory(new JsonParser());
		factory = new ReportJsonFactory(new JsonParser());

		report = getReport();

	}

	@Test
	public void serializerTest() {

	}

	@Test
	public void deSerializerTest() {
		assertNull(reportSerializer.deserialize(""));
	}

	@Test
	public void fullReportSerializerTest() {

	}

	@Test
	public void fullReportDeSerializerTest() {

	}

	@Test
	public void reportChartElementTest() {

		String chartJson = "{\"elementType\":\"pivotChart\",\"title\":\"Chart Element\",\"type\":\"Bar\",\"filter\":{\"isOr\":false,\"restrictions\":[]},\"categoryDimensions\":[{\"type\":\"AdminLevel\",\"caption\":\"admin level\",\"level\":0}],\"seriesDimensions\":[]}";

		PivotChartReportElement chartElement = report.getElement(0);
		JsonElement resultJsonElement = factory
				.encodePivotChartReportElement(chartElement);
		System.out.println(resultJsonElement.toString());
		assertEquals(chartJson, resultJsonElement.toString());

		PivotChartReportElement resultChart = factory
				.decodePivotChartReportElement((JsonObject) resultJsonElement);

		assertEquals(chartElement.getTitle(), resultChart.getTitle());
		assertEquals(chartElement.getContent(), resultChart.getContent());
		assertEquals(chartElement.getType(), resultChart.getType());

	}

	@Test
	public void dimensionsTest() {
		Report report = getReport();
	}

	@Test
	public void filtersTest() {

		String filterJson = "{\"minDate\":1331712050839,\"maxDate\":1331712050839,\"isOr\":true,\"restrictions\":[{\"type\":\"Activity\",\"set\":[0]}]}";

		JsonElement filterElement = factory.encodeFilter(report.getFilter());
		String resultFiltorJson = filterElement.toString();

		assertEquals(filterJson, resultFiltorJson);

		Filter filter = factory.decodeFilter((JsonObject) filterElement); // filterJson

		assertEquals(report.getFilter().getMaxDate(), filter.getMaxDate());
		assertEquals(report.getFilter().getMinDate(), filter.getMinDate());
	}

	@Test
	public void tableTest() {

	}

	private Report getReport() {
		Report report = new Report();

		report.setTitle("apple report");
		report.setId(0);

		addFilter(report);
		addChartElement(report);

		return report;
	}

	private void addFilter(Report report) {
		Filter filter = new Filter();
		filter.setMaxDate(maxDate);
		filter.setMinDate(minDate);

		filter.addRestriction(DimensionType.Activity, 0);
		report.setFilter(filter);
	}

	private void addChartElement(Report report) {
		PivotChartReportElement chartElement = new PivotChartReportElement();
		chartElement.setTitle("Chart Element");
		chartElement.setCategoryDimensions(createDimensionList());

		report.addElement(chartElement);
	}

	private List<Dimension> createDimensionList() {
		List dims = Lists.newArrayList();

		AdminDimension dim1 = new AdminDimension("admin level", 0);
		dims.add(dim1);

		return dims;
	}
}
