package org.sigmah.shared.dto.report;

import com.extjs.gxt.ui.client.data.BaseModelData;
import java.util.Date;
import org.sigmah.shared.domain.report.ProjectReport;
import org.sigmah.shared.dto.value.ListableValue;

public class ReportReference extends BaseModelData implements ListableValue {

    private static final long serialVersionUID = 1736989091550004973L;

    public ReportReference() {
    }

    public ReportReference(ProjectReport report) {
        this.set("id", report.getId());
        this.set("name", report.getName());
        this.set("lastEditDate", report.getCurrentVersion().getEditDate());
        this.set("editorName", report.getCurrentVersion().getEditorShortName());
        this.set("phaseName", report.getCurrentVersion().getPhaseName());
        if (report.getFlexibleElement() != null) {
            this.set("flexibleElementLabel", report.getFlexibleElement().getLabel());
        }
    }

    public Integer getId() {
        return get("id");
    }

    public void setId(Integer id) {
        this.set("id", id);
    }

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        this.set("name", name);
    }

    public String getPhaseName() {
        return get("phaseName");
    }

    public void setPhaseName(String phaseName) {
        this.set("phaseName", phaseName);
    }

    public String getFlexibleElementLabel() {
        return get("flexibleElementLabel");
    }

    public void setFlexibleElementLabel(String label) {
        this.set("flexibleElementLabel", label);
    }

    public Date getLastEditDate() {
        return get("lastEditDate");
    }

    public void setLastEditDate(Date date) {
        this.set("lastEditDate", date);
    }

    public String getEditorName() {
        return get("editorName");
    }

    public void setEditorName(String editorName) {
        this.set("editorName", editorName);
    }

    public boolean isDocument() {
        final Boolean is = (Boolean) get("isDocument");
        return is == null ? false : is;
    }

    public void setDocument(boolean isDocument) {
        this.set("isDocument", isDocument);
    }
}
