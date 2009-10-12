package org.activityinfo.server.report.renderer;

import org.activityinfo.server.report.renderer.html.ImageStorage;
import org.activityinfo.server.report.renderer.html.ImageStorageProvider;
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

            Chart chart = generateChart(element, includeTitle, width, height, dpi);

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
            Chart chart = generateChart(element, includeTitle, width, height, dpi);
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
            Chart chart = generateChart(element, includeTitle, width, height, dpi);
            chart.setGraphics2D(g2d);
            chart.render();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Chart generateChart(PivotChartElement element, boolean includeTitle, int width, int height, int dpi) throws IOException, ChartDataException {
        if(element.getType() == PivotChartElement.Type.Pie)  {
            return generatePieChart(element, includeTitle, width, height, dpi);
        } else {
            return generateAxisChart(element, includeTitle, width, height, dpi);
        }
    }

    protected Chart generatePieChart(PivotChartElement element, boolean includeTitle, int width, int height, int dpi) throws IOException, ChartDataException {

        PivotTableData table = element.getContent().getData();
        List<PivotTableData.Axis> categories = table.getRootCategory().getLeaves();
        PivotTableData.Axis series = table.getRootSeries().getLeaves().get(0);

        Paint[] paints = getDefaultPaints(categories.size());
        Dimension dim = categories.get(0).getDimension();
        for(int i=0; i!=categories.size();++i) {
            CategoryProperties props = dim.getCategories().get(categories.get(i).getCategory());
            if(props != null && props.getColor()!= null ) {
                paints[i] = new Color(props.getColor());
            }
        }

        PieChart2DProperties pieProps = new PieChart2DProperties();
        pieProps.setValueLabelFont(new ChartFont( new Font( Font.SANS_SERIF, Font.PLAIN, fontSize(10, dpi)), Color.black));
        pieProps.setShowGrouping(true);
        pieProps.setPieLabelType(PieLabelType.VALUE_LABELS);

        PieChartDataSet dataSet = new PieChartDataSet(
                includeTitle ? element.getTitle() : null,
                toDataArray(categories, series),
                toLabelArray(categories),
                paints,
                pieProps);


        LegendProperties legendProps = getLegendProperties(dpi);
        legendProps.setPlacement(LegendProperties.RIGHT);
        legendProps.setNumColumns(1);
        legendProps.setBorderStroke(null);

        ChartProperties chartProps = getChartProperties(dpi);

        return new PieChart2D(dataSet, legendProps, chartProps, width, height );
    }

    protected Chart generateAxisChart(PivotChartElement element, boolean includeTitle, int width, int height, int dpi) throws IOException {


        PivotTableData table = element.getContent().getData();

        /*
           * Sort out categories / legends
           */

        List<PivotTableData.Axis> categories = table.getRootRow().getLeaves();
        List<PivotTableData.Axis> series = table.getRootColumn().getLeaves();


        String[] axisLabels = toLabelArray(categories);

        IAxisDataSeries dataSeries = new DataSeries(
                axisLabels,
                element.getContent().getXAxisTitle(),
                element.getContent().getYAxisTitle(),
                includeTitle ? element.getTitle() : null );



        String[] legend = toLabelArray(series);

        Paint[] paints = getDefaultPaints(legend.length);



        double[][] data = toDataArray(categories, series);

        try {
            ChartType type;
            if(element.getType() == PivotChartElement.Type.StackedBar) {
                type = ChartType.BAR_STACKED;
            } else if(element.getType() == PivotChartElement.Type.ClusteredBar) {
                type = ChartType.BAR_CLUSTERED;
            } else {
                type = ChartType.BAR;

            }

            dataSeries.addIAxisPlotDataSet( new AxisChartDataSet(
                    data,
                    legend,
                    paints,
                    type,
                    getBarChartProperties(element)) );

        } catch (ChartDataException e) {
            e.printStackTrace();
            return null;
        }

        return new AxisChart(
                dataSeries,
                getChartProperties(dpi),
                getAxisProperties(dpi),
                getLegendProperties(dpi), width, height);

    }


    protected String[] toLabelArray(Collection<PivotTableData.Axis> leaves) {

        String[] labels = new String[leaves.size() ];
        int i=0;
        for(PivotTableData.Axis leaf : leaves) {
            labels[i++] = composeLabel(leaf);
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
                if(cell == null) {

                    categoryData[j++] = 0;

                } else {

                    categoryData[j++] = cell.getValue();
                }

            }

            seriesData[i] = categoryData;

        }

        return seriesData;
    }

    protected double[] toDataArray(List<PivotTableData.Axis> categories, PivotTableData.Axis series) {

        double[] data = new double[categories.size()];
        for(int i=0; i!=categories.size(); ++i) {

            data[i] = categories.get(i).getCell(series).getValue();

        }

        return data;
    }

    protected ChartProperties getChartProperties(int dpi) {

        ChartProperties props = new ChartProperties();

        ChartFont titleFont = new ChartFont( new Font( Font.SANS_SERIF, Font.PLAIN, fontSize(12, dpi) ), Color.black );
        props.setTitleFont( titleFont );


        return props;
    }

    protected Paint[] getDefaultPaints(int count) {

        Paint[] colors = new Paint[] { Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GREEN, Color.RED };

        Paint[] paints = new Paint[count];
        for(int i=0; i!=paints.length; ++i) {
            paints[i] = colors[i % colors.length];
        }
        return paints;
    }

    private int fontSize(int pt, int dpi) {
        return (int) ( ((float) pt) / 72f * ((float) dpi) );
    }

    protected AxisProperties getAxisProperties(int dpi) {

        AxisProperties props = new AxisProperties( false );
        ChartFont axisScaleFont = new ChartFont( new Font( Font.SANS_SERIF, Font.PLAIN, fontSize(10, dpi) ), Color.black );
        props.getXAxisProperties().setScaleChartFont( axisScaleFont );
        props.getYAxisProperties().setScaleChartFont( axisScaleFont );

        ChartFont axisTitleFont = new ChartFont( new Font( Font.SANS_SERIF, Font.PLAIN, fontSize(11, dpi) ), Color.black );
        props.getXAxisProperties().setTitleChartFont( axisTitleFont );
        props.getYAxisProperties().setTitleChartFont( axisTitleFont );

        return props;
    }

    protected LegendProperties getLegendProperties(int dpi) {

        ChartFont font = new ChartFont(new Font( Font.SANS_SERIF, Font.PLAIN, fontSize(10, dpi)), Color.BLACK);

        LegendProperties legendProperties = new LegendProperties();
        legendProperties.setChartFont( font );
        legendProperties.setBorderStroke( ChartStroke.DEFAULT_ZERO_LINE ) ;
        return legendProperties;
    }

    protected BarChartProperties getBarChartProperties(PivotChartElement element) {


        if(element.getType() == PivotChartElement.Type.Bar) {
            BarChartProperties chartProperties = new BarChartProperties();
            return chartProperties;
        } else if(element.getType() == PivotChartElement.Type.StackedBar) {
            return new StackedBarChartProperties();
        } else if(element.getType() == PivotChartElement.Type.ClusteredBar){
            return new ClusteredBarChartProperties();
        } else {
            throw new IllegalArgumentException(element.getType().toString());
        }
    }


    /**
     * Composes the label of pivot table column, row.
     * @param axis
     * @return
     */
    protected String composeLabel(PivotTableData.Axis axis) {
        StringBuilder sb =  new StringBuilder();

        do {
            if(axis.getLabel() != null) {
                if(sb.length()!=0)
                    sb.append(" ");

                sb.append(axis.getLabel());
            }
            axis = axis.getParent();

        } while(axis != null);

        return sb.toString();

    }

}
