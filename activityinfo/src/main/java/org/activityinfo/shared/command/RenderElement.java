package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.RenderResult;
import org.activityinfo.shared.report.model.ReportElement;

public class RenderElement implements Command<RenderResult> {

    public enum Format {
        PNG,
        Excel,
        Excel_Data,
        PowerPoint,
        PDF,
        Word
    }

    private Format format;
    private ReportElement element;

    public RenderElement() {
    }

    public RenderElement(ReportElement element, Format format) {
        this.element = element;
        this.format = format;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public ReportElement getElement() {
        return element;
    }

    public void setElement(ReportElement element) {
        this.element = element;
    }
}
