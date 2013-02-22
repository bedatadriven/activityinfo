package org.activityinfo.server.report.renderer;

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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.activityinfo.server.report.output.StorageProvider;
import org.activityinfo.server.report.output.TempStorage;
import org.activityinfo.shared.exception.ReportModelException;
import org.activityinfo.shared.report.Theme;
import org.activityinfo.shared.report.content.PivotChartContent;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.CategoryProperties;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement.Type;
import org.krysalis.jcharts.Chart;
import org.krysalis.jcharts.axisChart.AxisChart;
import org.krysalis.jcharts.chartData.AxisChartDataSet;
import org.krysalis.jcharts.chartData.ChartDataException;
import org.krysalis.jcharts.chartData.DataSeries;
import org.krysalis.jcharts.chartData.PieChartDataSet;
import org.krysalis.jcharts.chartData.interfaces.IAxisDataSeries;
import org.krysalis.jcharts.encoders.JPEGEncoder;
import org.krysalis.jcharts.nonAxisChart.PieChart2D;
import org.krysalis.jcharts.properties.AxisChartTypeProperties;
import org.krysalis.jcharts.properties.AxisProperties;
import org.krysalis.jcharts.properties.BarChartProperties;
import org.krysalis.jcharts.properties.ChartProperties;
import org.krysalis.jcharts.properties.ClusteredBarChartProperties;
import org.krysalis.jcharts.properties.DataAxisProperties;
import org.krysalis.jcharts.properties.LabelAxisProperties;
import org.krysalis.jcharts.properties.LegendProperties;
import org.krysalis.jcharts.properties.LineChartProperties;
import org.krysalis.jcharts.properties.PieChart2DProperties;
import org.krysalis.jcharts.properties.PropertyException;
import org.krysalis.jcharts.properties.StackedBarChartProperties;
import org.krysalis.jcharts.properties.util.ChartFont;
import org.krysalis.jcharts.properties.util.ChartStroke;
import org.krysalis.jcharts.types.ChartType;
import org.krysalis.jcharts.types.PieLabelType;

import com.google.code.appengine.awt.BasicStroke;
import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Font;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.Paint;
import com.google.code.appengine.awt.Shape;
import com.google.code.appengine.awt.Stroke;
import com.google.code.appengine.awt.geom.Rectangle2D;
import com.google.code.appengine.awt.image.BufferedImage;
import com.google.code.appengine.imageio.ImageIO;

/**
 * Pivot Chart Generator that uses JChart to render the chart as an image
 * 
 * @author Alex Bertram
 * 
 */
public class ChartRendererJC {

    private static final String SANS_SERIF = null;

    public String renderToUrl(PivotChartReportElement element,
        boolean includeTitle, StorageProvider istorageProvider,
        int width, int height, int dpi) throws IOException {
        try {
            Chart chart = createChart(element, includeTitle, width, height, dpi);
            TempStorage storage = istorageProvider.allocateTemporaryFile(null,
                "activityinfo.jpg");
            JPEGEncoder.encode(chart, 0.75f, storage.getOutputStream());
            return storage.getUrl();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void render(PivotChartReportElement element, OutputStream os)
        throws IOException {
        BufferedImage image = renderImage(element, true, 640, 480, 96);
        ImageIO.write(image, "PNG", os);
    }

    public BufferedImage renderImage(PivotChartReportElement element,
        boolean includeTitle, int width, int height, int dpi) {

        try {
            Chart chart = createChart(element, includeTitle, width, height, dpi);
            BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
            chart.setGraphics2D(bufferedImage.createGraphics());
            chart.render();

            return bufferedImage;

        } catch (IOException e) {
            throw new ReportModelException(e, element);
        } catch (ChartDataException e) {
            throw new ReportModelException(e, element);
        } catch (PropertyException e) {
            throw new ReportModelException(e, element);
        }
    }

    public void render(PivotChartReportElement element, boolean includeTitle,
        Graphics2D g2d, int width, int height, int dpi) {
        try {
            Chart chart = createChart(element, includeTitle, width, height, dpi);
            chart.setGraphics2D(g2d);
            chart.render();
        } catch (Exception e) {
            throw new RuntimeException("Rendering of chart entitled '"
                + element.getTitle() + "' failed", e);
        }
    }

    /**
     * Creates a JChart from the given PivotChartElement and its generated
     * PivotChartContent
     * 
     * @param element
     *            The underlying PivotChartElement
     * @param includeTitle
     *            True if the title should be rendered as part of the image
     * @param width
     *            Width in pixels
     * @param height
     *            Heights in pixels
     * @param dpi
     *            Resolution (dots-per-inch) to use in the calculation of font
     *            sizes
     * @return A JChart <code>Chart</code>
     * @throws IOException
     * @throws ChartDataException
     */
    protected Chart createChart(PivotChartReportElement element,
        boolean includeTitle, int width, int height, int dpi)
        throws IOException, ChartDataException, PropertyException {
        if (element.getType() == PivotChartReportElement.Type.Pie) {
            return createPieChart(element, includeTitle, width, height, dpi);
        } else {
            return createAxisChart(element, includeTitle, width, height, dpi);
        }
    }

    /**
     * Creates a JChart PieChart from our report element
     */
    protected Chart createPieChart(PivotChartReportElement element,
        boolean includeTitle, int width, int height, int dpi)
        throws IOException, ChartDataException {

        PivotTableData table = element.getContent().getData();
        List<PivotTableData.Axis> categories = table.getRootCategory()
            .getLeaves();
        PivotTableData.Axis series = table.getRootSeries().getLeaves().get(0);

        PieChart2DProperties pieProps = new PieChart2DProperties();
        pieProps.setValueLabelFont(new ChartFont(
            new Font(SANS_SERIF, Font.PLAIN, fontSize(10, dpi)), Color.black));
        pieProps.setShowGrouping(true);
        pieProps.setPieLabelType(PieLabelType.VALUE_LABELS);
        pieProps.setBorderChartStroke(ChartStroke.DEFAULT_ZERO_LINE);

        PieChartDataSet dataSet = new PieChartDataSet(
            includeTitle ? element.getTitle() : null,
            toDataArray(categories, series),
            toLabelArray(element.getContent(), categories),
            computePaints(categories),
            pieProps);

        LegendProperties legendProps = computeLegendProperties(element, dpi,
            categories);
        ChartProperties chartProps = computeChartProperties(dpi);

        return new PieChart2D(dataSet, legendProps, chartProps, width, height);
    }

    private Paint[] computePaints(List<PivotTableData.Axis> categories) {
        Paint[] paints = getDefaultPaints(categories.size());
        Dimension dim = categories.get(0).getDimension();

        if (dim == null) {
            return paints;
        }

        for (int i = 0; i != categories.size(); ++i) {
            CategoryProperties props = dim.getCategories().get(
                categories.get(i).getCategory());
            if (props != null && props.getColor() != null) {
                paints[i] = new Color(props.getColor());
            }
        }
        return paints;
    }

    protected Chart createAxisChart(PivotChartReportElement element,
        boolean includeTitle,
        int width, int height, int dpi)
        throws IOException, ChartDataException, PropertyException {

        PivotTableData table = element.getContent().getData();

        // find our categories and series leaves
        List<PivotTableData.Axis> categories = table.getRootRow().getLeaves();
        List<PivotTableData.Axis> series = table.getRootColumn().getLeaves();

        IAxisDataSeries dataSeries = new DataSeries(
            toLabelArray(element.getContent(), categories),
            element.getContent().getXAxisTitle(),
            element.getContent().getYAxisTitle(),
            includeTitle ? element.getTitle() : null);

        dataSeries.addIAxisPlotDataSet(
            new AxisChartDataSet(
                toDataArray(categories, series),
                toLabelArray(element.getContent(), series),
                computePaints(series),
                computeChartType(element),
                computeAxisChartProperties(dpi, element)));

        return new AxisChart(
            dataSeries,
            computeChartProperties(dpi),
            computeAxisProperties(dpi, element.getContent()),
            computeLegendProperties(element, dpi, series), width, height);
    }

    private ChartType computeChartType(PivotChartReportElement element) {
        switch (element.getType()) {
        case Line:
            return ChartType.LINE;
        case StackedBar:
            return ChartType.BAR_STACKED;
        case Bar:
        case ClusteredBar:
        default:
            return ChartType.BAR_CLUSTERED;
        }
    }

    protected String[] toLabelArray(PivotChartContent content,
        Collection<PivotTableData.Axis> leaves) {
        String[] labels = new String[leaves.size()];
        int i = 0;
        for (PivotTableData.Axis leaf : leaves) {
            labels[i] = leaf.flattenLabel();
            if (labels[i].length() == 0) {
                labels[i] = content.getYAxisTitle();
            }
            i++;
        }
        return labels;
    }

    protected double[][] toDataArray(List<PivotTableData.Axis> categories,
        List<PivotTableData.Axis> series) {
        double[][] seriesData = new double[series.size()][];
        for (int i = 0; i != series.size(); ++i) {
            double[] categoryData = new double[categories.size()];
            int j = 0;
            for (PivotTableData.Axis category : categories) {
                PivotTableData.Cell cell = category.getCell(series.get(i));
                if (cell == null || cell.getValue() == null) {
                    categoryData[j++] = 0;
                } else {
                    categoryData[j++] = cell.getValue();
                }
            }
            seriesData[i] = categoryData;
        }
        return seriesData;
    }

    /**
     * Builds a single dimensional array from a 2d PivotTableData.
     * 
     * @param categories
     *            Leaf categories
     * @param series
     *            Leaf series
     * @return A one dimensional data array.
     */
    protected double[] toDataArray(List<PivotTableData.Axis> categories,
        PivotTableData.Axis series) {
        double[] data = new double[categories.size()];
        for (int i = 0; i != categories.size(); ++i) {
            PivotTableData.Cell cell = categories.get(i).getCell(series);
            if (cell != null && cell.getValue() != 0) {
                data[i] = cell.getValue();
            }
        }
        return data;
    }

    protected ChartProperties computeChartProperties(int dpi) {
        ChartProperties p = new ChartProperties();
        ChartFont titleFont = new ChartFont(
            new Font(SANS_SERIF, Font.PLAIN, fontSize(12, dpi)), Color.black);
        p.setTitleFont(titleFont);
        return p;
    }

    protected Paint[] getDefaultPaints(int count) {
        String[] accents = Theme.getAccents();
        Paint[] paints = new Paint[count];
        for (int i = 0; i != paints.length; ++i) {
            paints[i] = Color.decode(accents[i % accents.length]);
        }
        return paints;
    }

    /**
     * Caclulates the font size in pixels given the font size in points and the
     * resolution (dots-per-inch)
     * 
     * @param pt
     *            Font size in points (1 in = 72 points)
     * @param dpi
     *            Resolution in dots-per-inch (pixels-per-inch)
     * @return the font-size in pixels
     */
    private int fontSize(int pt, int dpi) {
        return (int) ((pt) / 72f * (dpi));
    }

    protected AxisProperties computeAxisProperties(int dpi,
        PivotChartContent content)
        throws PropertyException {

        ChartFont axisScaleFont = new ChartFont(
            new Font(SANS_SERIF, Font.PLAIN, fontSize(10, dpi)), Color.black);
        ChartFont axisTitleFont = new ChartFont(
            new Font(SANS_SERIF, Font.PLAIN, fontSize(11, dpi)), Color.black);

        AxisProperties p = new AxisProperties(false);

        LabelAxisProperties x = (LabelAxisProperties) p.getXAxisProperties();
        x.setScaleChartFont(axisScaleFont);
        x.setAxisTitleChartFont(axisTitleFont);

        DataAxisProperties y = (DataAxisProperties) p.getYAxisProperties();
        y.setScaleChartFont(axisScaleFont);
        y.setUserDefinedScale(content.getYMin(), content.getYStep());
        y.setAxisTitleChartFont(axisTitleFont);
        y.setShowGridLines(1);

        return p;
    }

    protected LegendProperties computeLegendProperties(
        PivotChartReportElement element, int dpi,
        List<PivotTableData.Axis> series) {

        // if there is only series, there is no need for a legend
        if (series.size() <= 1) {
            return null;
        }

        ChartFont font = new ChartFont(
            new Font(SANS_SERIF, Font.PLAIN, fontSize(10, dpi)), Color.BLACK);
        LegendProperties p = new LegendProperties();
        p.setChartFont(font);
        p.setBorderStroke(null);
        p.setIconBorderPaint(null);
        if (element.getType() == Type.Pie) {
            p.setPlacement(LegendProperties.RIGHT);
        } else {
            p.setPlacement(maxLegendTextLength(series) > 30 ?
                LegendProperties.BOTTOM :
                LegendProperties.RIGHT);
        }
        p.setNumColumns(1);

        return p;
    }

    private int maxLegendTextLength(List<PivotTableData.Axis> series) {
        int max = 0;
        for (PivotTableData.Axis axis : series) {
            max = Math.max(max, axis.flattenLabel().length());
        }
        return max;
    }

    protected AxisChartTypeProperties computeAxisChartProperties(int dpi,
        PivotChartReportElement element) {
        if (element.getType() == PivotChartReportElement.Type.Line) {
            return computeLineChartProperties(element, dpi);
        } else {
            BarChartProperties p;
            switch (element.getType()) {
            case StackedBar:
                p = new StackedBarChartProperties();
                break;
            case Bar:
            case ClusteredBar:
                p = new ClusteredBarChartProperties();
                break;
            default:
                throw new IllegalArgumentException(element.getType().toString());
            }
            p.setShowOutlinesFlag(false);
            return p;
        }
    }

    private AxisChartTypeProperties computeLineChartProperties(
        PivotChartReportElement element, int dpi) {
        List<PivotTableData.Axis> series = element.getContent().getData()
            .getRootSeries().getLeaves();
        Stroke[] stroke = new Stroke[series.size()];
        Shape[] shape = new Shape[series.size()];
        int si = 0;
        for (PivotTableData.Axis s : series) {
            stroke[si] = new BasicStroke(2f / 72f * dpi);
            shape[si] = new Rectangle2D.Double(0, 0, 4f / 72f * dpi,
                4f / 72f * dpi);
            si++;
        }
        return new LineChartProperties(stroke, shape);
    }
}
