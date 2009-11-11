package org.activityinfo.server.report.renderer;

import org.activityinfo.server.report.renderer.html.ImageStorage;
import org.activityinfo.server.report.renderer.html.ImageStorageProvider;
import org.activityinfo.shared.report.Theme;
import org.activityinfo.shared.report.content.PivotChartContent;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.CategoryProperties;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.ModelException;
import org.activityinfo.shared.report.model.PivotChartElement;
import org.krysalis.jcharts.Chart;
import org.krysalis.jcharts.axisChart.AxisChart;
import org.krysalis.jcharts.chartData.AxisChartDataSet;
import org.krysalis.jcharts.chartData.ChartDataException;
import org.krysalis.jcharts.chartData.DataSeries;
import org.krysalis.jcharts.chartData.PieChartDataSet;
import org.krysalis.jcharts.chartData.interfaces.IAxisDataSeries;
import org.krysalis.jcharts.encoders.JPEGEncoder13;
import org.krysalis.jcharts.nonAxisChart.PieChart2D;
import org.krysalis.jcharts.properties.*;
import org.krysalis.jcharts.properties.util.ChartFont;
import org.krysalis.jcharts.properties.util.ChartStroke;
import org.krysalis.jcharts.types.ChartType;
import org.krysalis.jcharts.types.PieLabelType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.List;


/**
 * Pivot Chart Generator that uses JChart to render the chart as an image
 *
 * @author Alex Bertram
 *
 */
public class ChartRendererJC  {


    public String renderToUrl(PivotChartElement element, boolean includeTitle, ImageStorageProvider istorageProvider,
                              int width, int height, int dpi) throws IOException {
        try {
            Chart chart = createChart(element, includeTitle, width, height, dpi);
            ImageStorage storage = istorageProvider.getImageUrl(".jpg");
            JPEGEncoder13.encode(chart,  0.75f, storage.getOutputStream());
            return storage.getUrl();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BufferedImage renderImage(PivotChartElement element, boolean includeTitle, int width, int height, int dpi) {

        try {
            Chart chart = createChart(element, includeTitle, width, height, dpi);
            BufferedImage bufferedImage=new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
            chart.setGraphics2D( bufferedImage.createGraphics() );
            chart.render();

            return bufferedImage;

        } catch (IOException e) {
            throw new ModelException(e, element);
        } catch (ChartDataException e) {
            throw new ModelException(e, element);
        } catch (PropertyException e) {
            throw new ModelException(e, element);
        }
    }

    public void render(PivotChartElement element, boolean includeTitle, Graphics2D g2d, int width, int height, int dpi) {
        try {
            Chart chart = createChart(element, includeTitle, width, height, dpi);
            chart.setGraphics2D(g2d);
            chart.render();
        } catch(Exception e) {
            throw new RuntimeException("Rendering of chart entitled '" + element.getTitle() + "' failed",  e);
        }
    }

    /**
     * Creates a JChart from the given PivotChartElement and its generated PivotChartContent
     *
     * @param element  The underlying PivotChartElement
     * @param includeTitle True if the title should be rendered as part of the image
     * @param width Width in pixels
     * @param height Heights in pixels
     * @param dpi Resolution (dots-per-inch) to use in the calculation of font sizes
     * @return A JChart <code>Chart</code>
     * @throws IOException
     * @throws ChartDataException
     */
    protected Chart createChart(PivotChartElement element, boolean includeTitle, int width, int height, int dpi)
            throws IOException, ChartDataException, PropertyException {
        if(element.getType() == PivotChartElement.Type.Pie)  {
            return createPieChart(element, includeTitle, width, height, dpi);
        } else {
            return createAxisChart(element, includeTitle, width, height, dpi);
        }
    }

    /**
     * Creates a JChart PieChart from our report element
     */
    protected Chart createPieChart(PivotChartElement element, boolean includeTitle, int width, int height, int dpi) throws IOException, ChartDataException {

        PivotTableData table = element.getContent().getData();
        List<PivotTableData.Axis> categories = table.getRootCategory().getLeaves();
        PivotTableData.Axis series = table.getRootSeries().getLeaves().get(0);

        PieChart2DProperties pieProps = new PieChart2DProperties();
        pieProps.setValueLabelFont(new ChartFont(
                new Font( Font.SANS_SERIF, Font.PLAIN, fontSize(10, dpi)), Color.black));
        pieProps.setShowGrouping(true);
        pieProps.setPieLabelType(PieLabelType.VALUE_LABELS);
        pieProps.setBorderChartStroke( ChartStroke.DEFAULT_ZERO_LINE );

        PieChartDataSet dataSet = new PieChartDataSet(
                includeTitle ? element.getTitle() : null,
                toDataArray(categories, series),
                toLabelArray(element.getContent(), categories),
                computePaints(categories),
                pieProps);

        LegendProperties legendProps = computeLegendProperties(dpi, categories);
        ChartProperties chartProps = computeChartProperties(dpi);

        return new PieChart2D(dataSet, legendProps, chartProps, width, height );
    }

    private Paint[] computePaints(List<PivotTableData.Axis> categories) {
        Paint[] paints = getDefaultPaints(categories.size());
        Dimension dim = categories.get(0).getDimension();
        
        if(dim == null)
            return paints;

        for(int i=0; i!=categories.size();++i) {
            CategoryProperties props = dim.getCategories().get(categories.get(i).getCategory());
            if(props != null && props.getColor()!= null ) {
                paints[i] = new Color(props.getColor());
            }
        }
        return paints;
    }

    protected Chart createAxisChart(PivotChartElement element, boolean includeTitle,
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
                includeTitle ? element.getTitle() : null );

        dataSeries.addIAxisPlotDataSet(
                new AxisChartDataSet(
                    toDataArray(categories, series),
                    toLabelArray(element.getContent(), series),
                    computePaints(series),
                    computeChartType(element),
                    computeAxisChartProperties(dpi, element)) );

        return new AxisChart(
                dataSeries,
                computeChartProperties(dpi),
                computeAxisProperties(dpi, element.getContent()),
                computeLegendProperties(dpi, series), width, height);
    }

    private ChartType computeChartType(PivotChartElement element) {
        switch (element.getType()) {
            case ClusteredBar:
                return ChartType.BAR_CLUSTERED;
            case Line:
                return ChartType.LINE;
            case StackedBar:
                return ChartType.BAR_STACKED;
            default:
                return ChartType.BAR;
        }
    }

    protected String[] toLabelArray(PivotChartContent content, Collection<PivotTableData.Axis> leaves) {
        String[] labels = new String[leaves.size() ];
        int i=0;
        for(PivotTableData.Axis leaf : leaves) {
            labels[i] = leaf.flattenLabel();
            if(labels[i].length() == 0) {
                labels[i] = content.getYAxisTitle();
            }
            i++;
        }
        return labels;
    }

    protected double[][] toDataArray(List<PivotTableData.Axis> categories, List<PivotTableData.Axis> series) {
        double[][] seriesData = new double[series.size()][];
        for(int i=0; i!=series.size(); ++i) {
            double[] categoryData = new double[categories.size()];
            int j = 0;
            for(PivotTableData.Axis category : categories) {
                PivotTableData.Cell cell = category.getCell(series.get(i));
                if(cell == null || cell.getValue() == null) {
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
     * @param categories Leaf categories
     * @param series Leaf series
     * @return A one dimensional data array.
     */
    protected double[] toDataArray(List<PivotTableData.Axis> categories, PivotTableData.Axis series) {
        double[] data = new double[categories.size()];
        for(int i=0; i!=categories.size(); ++i) {
            PivotTableData.Cell cell = categories.get(i).getCell(series);
            if(cell != null && cell.getValue() != 0)
                data[i] = cell.getValue();
        }
        return data;
    }

    protected ChartProperties computeChartProperties(int dpi) {
        ChartProperties p = new ChartProperties();
        ChartFont titleFont = new ChartFont(
                new Font( Font.SANS_SERIF, Font.PLAIN, fontSize(12, dpi) ), Color.black );
        p.setTitleFont( titleFont );
        return p;
    }

    protected Paint[] getDefaultPaints(int count) {
        String[] accents = Theme.getAccents();
        Paint[] paints = new Paint[count];
        for(int i=0; i!=paints.length; ++i) {
            paints[i] = Color.decode(accents[i % accents.length]);
        }
        return paints;
    }

    /**
     * Caclulates the font size in pixels given the font size in points and
     * the resolution (dots-per-inch)
     *
     * @param pt Font size in points (1 in = 72 points)
     * @param dpi Resolution in dots-per-inch (pixels-per-inch)
     * @return the font-size in pixels
     */
    private int fontSize(int pt, int dpi) {
        return (int) ( ((float) pt) / 72f * ((float) dpi) );
    }

    protected AxisProperties computeAxisProperties(int dpi, PivotChartContent content)
            throws PropertyException {

        ChartFont axisScaleFont = new ChartFont(
                new Font( Font.SANS_SERIF, Font.PLAIN, fontSize(10, dpi) ), Color.black );
        ChartFont axisTitleFont = new ChartFont(
                new Font( Font.SANS_SERIF, Font.PLAIN, fontSize(11, dpi) ), Color.black );

        AxisProperties p = new AxisProperties( false );

        LabelAxisProperties x = (LabelAxisProperties) p.getXAxisProperties();
        x.setScaleChartFont( axisScaleFont );
        x.setAxisTitleChartFont( axisTitleFont );

        DataAxisProperties y = (DataAxisProperties) p.getYAxisProperties();
        y.setScaleChartFont( axisScaleFont );
        y.setUserDefinedScale( content.getYMin(), content.getYStep() );
        y.setAxisTitleChartFont( axisTitleFont );
        y.setShowGridLines(1);

        return p;
    }

    protected LegendProperties computeLegendProperties(int dpi, List<PivotTableData.Axis> series) {

        // if there is only series, there is no need for a legend
        if(series.size() <= 1)
            return null;

        ChartFont font = new ChartFont(
                new Font( Font.SANS_SERIF, Font.PLAIN, fontSize(10, dpi)), Color.BLACK);
        LegendProperties p = new LegendProperties();
        p.setChartFont( font );
        p.setBorderStroke( null ) ;
        p.setIconBorderPaint( null );
        p.setPlacement(LegendProperties.RIGHT);
        p.setNumColumns(1);

        return p;
    }

    protected AxisChartTypeProperties computeAxisChartProperties(int dpi, PivotChartElement element) {
        if(element.getType() == PivotChartElement.Type.Line) {
            return computeLineChartProperties(element, dpi);
        } else {
            BarChartProperties p;
            switch (element.getType()) {
                case Bar:
                    p = new BarChartProperties();
                    break;
                case StackedBar:
                    p = new StackedBarChartProperties();
                    break;
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

    private AxisChartTypeProperties computeLineChartProperties(PivotChartElement element, int dpi) {
        List<PivotTableData.Axis> series = element.getContent().getData().getRootSeries().getLeaves();
        Stroke[] stroke = new Stroke[series.size()];
        Shape[] shape = new Shape[series.size()];
        int si = 0;
        for(PivotTableData.Axis s : series) {
            stroke[si] = new BasicStroke(2f / 72f * dpi);
            shape[si] = new Rectangle2D.Double(0, 0, 4f / 72f * dpi, 4f/72f*dpi);
            si++;
        }
        return new LineChartProperties(stroke, shape);
    }
}
