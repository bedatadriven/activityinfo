package org.activityinfo.shared.command;

import com.extjs.gxt.ui.client.data.RpcMap;

import java.util.Map;

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.HtmlResult;

public class RenderReportHtml implements Command<HtmlResult> {

    private int templateId;
    private RpcMap parameters;


    public RenderReportHtml() {
    }

    public RenderReportHtml(int templateId, Map<String,Object> parameters) {
        this.templateId = templateId;
        this.parameters = new RpcMap();
        this.parameters.putAll(parameters);
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public RpcMap getParameters() {
        return parameters;
    }

    public void setParameters(RpcMap parameters) {
        this.parameters = parameters;
    }
}
