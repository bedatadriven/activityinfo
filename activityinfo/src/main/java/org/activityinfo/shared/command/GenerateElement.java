package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.model.PivotChartElement;
import org.activityinfo.shared.report.model.ReportElement;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GenerateElement<T extends Content> implements Command<T> {

    private ReportElement element;

    protected GenerateElement() {
    }

    public GenerateElement(ReportElement element) {
        this.element = element;
    }

    public ReportElement getElement() {
        return element;
    }

    public void setElement(ReportElement element) {
        this.element = element;
    }
}
