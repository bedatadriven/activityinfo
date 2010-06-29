package org.sigmah.server.report.renderer.itext;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import org.sigmah.shared.report.content.FilterDescription;

import java.util.List;

/**
 * Support routines for ItextRenderers
 */
class ItextRendererHelper {

    /**
     * Adds a set of paragraphs describing the filters which are applied to this
     * given ReportElement.
     * @param document
     * @param filterDescriptions
     * @throws DocumentException
     */
  public static void addFilterDescription(Document document, List<FilterDescription> filterDescriptions) throws DocumentException {
    for(FilterDescription description : filterDescriptions) {
      document.add(ThemeHelper.filterDescription(description.joinLabels(", ")));
    }
  }
}
