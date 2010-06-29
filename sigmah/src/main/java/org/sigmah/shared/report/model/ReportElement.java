package org.sigmah.shared.report.model;

import org.sigmah.shared.report.content.Content;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * ReportElement is the base class for all report elements and the
 * report container itself.
 * <p/>
 * In ActivityInfo, we require that a given report element (such as chart) can
 * be rendered either on the client side or the server side. For example, on the
 * server side, a chart might be rendered to a GIF image that is included in a Word
 * document to be downloaded by the user, while on the client side, we want to render
 * the same chart using the OpenFlashChart flash component.
 * <p/>
 * So in order to maximize code reuse, the report pipeline is divided into serveral types of
 * interchangeable components:
 * <p/>
 * <ol>
 * <li><strong>Report Models</strong> define the structure and presentation of Tables, Pivot Tables,
 * Charts, etc. They have no dependencies and can be moved between the server and client.</li>
 * <p/>
 * <li><strong>Data Access Objects (DAOs)</strong> retrieve data and do the raw
 * number crunching. <code>SiteTableDAO</code> provides access to
 * lists of sites, while <code>PivotDAO</code> summarizes data
 * into a cube. Currently, there is a server-side implementation based on Hibernate and JDBC, but
 * ultimately we will have client-side implementations that read the data from client-side SqlLite
 * databases.
 * </li>
 * <p/>
 * <li><strong>Generators</strong> intrepret the Report Models and use DAOs to obtain data and structure the
 * content of a report element in <code>Content</code> objects.
 * In principal, generators should be able to run on either the server or client side, but in practice
 * they've been developed for the server side and probably have non-GWT-compatible-dependencies.</li>
 * <p/>
 * <li><strong>Renderers</strong> are server side class that accept Report Models and Report Content and render
 * them into a given format, such as PDF, a Word Document, or a PNG file. Renderers should not contain any
 * decisional logic or access data external to the <code>Content</code> object -- this sort of thing should be
 * centralized in the generators.</li>
 * <p/>
 * </ol>
 *
 * @author Alex Bertram
 * @see org.sigmah.server.dao.SiteTableDAO
 * @see org.sigmah.server.dao.PivotDAO
 * @see org.sigmah.server.report.generator.ReportGenerator
 * @see org.sigmah.shared.report.content.Content
 * @see org.sigmah.server.report.renderer.Renderer
 */
public abstract class ReportElement<ContentT extends Content> implements Serializable {

    private Filter filter = new Filter();
    private String title;
    private String sheetTitle;

    @XmlTransient
    protected ContentT content;

    public ReportElement() {

    }

    /**
     * Gets the filter that will be applied to this report.
     * Note that elements inherit the report's global filter,
     * as well as any other filter specified by the callers
     * at runtime.
     * <p/>
     * If this element is part of a <code>Report</code>, then
     * the <code>DateRange</code> provided to the generator will
     * also be applied to the filter, IF the <code>minDate</code>
     * and/or <code>maxDate</code> of this element's filter are <code>null</code>.
     * <p/>
     * This allows an individual <code>ReportElement</code> to override
     * the <code>DateRange</code> of the report-- for example, a <code>MONTHLY</code>
     * report may include a graph of results year-to-date.
     *
     * @return The filter applied to the report element.
     */
    @XmlElement
    public Filter getFilter() {
        return filter;
    }

    /**
     * Sets the filter that will be applied to this report.
     *
     * @param filter
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * Gets the full title of the report element. In document-based output,
     * like RTF, PDF, etc, this will be a text header that precedes the report element,
     * while in a PowerPoint presentation it may be the slide's title.
     *
     * @return the full title of the report element
     */
    @XmlElement
    public String getTitle() {
        return title;
    }

    /**
     * Sets the full title of the report element. In document-based output,
     * like RTF, PDF, etc, this will be a text header that precedes the report element,
     * while in a PowerPoint presentation it may be the slide's title.
     *
     * @param title the full title of the report element
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the short form of the title used to name worksheet tabs.
     * For example, something other than Sheet1, Sheet2, Sheet3 at the
     * bottom of Excel.
     *
     * @return The sheet title
     */
    @XmlElement
    public String getSheetTitle() {
        return sheetTitle;
    }

    /**
     * Sets the short form of the title used to name worksheet tabs.
     * For example, something other than Sheet1, Sheet2, Sheet3 at the
     * bottom of Excel.
     *
     * @param sheetTitle The sheet title
     */
    public void setSheetTitle(String sheetTitle) {
        this.sheetTitle = sheetTitle;
    }

    @XmlTransient
    public ContentT getContent() {
        return content;
    }

    public void setContent(ContentT content) {
        this.content = content;
    }
}
