package org.activityinfo.client.command.loader;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.Style;

import org.activityinfo.shared.command.GetListCommand;
import org.activityinfo.shared.command.PagingGetCommand;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DummyLoadConfig extends BaseModelData implements PagingLoadConfig {

    private int offset;
    private int limit;
    private String sortField;
    private Style.SortDir sortDir;

    public DummyLoadConfig(GetListCommand cmd) {
        setSortInfo(cmd.getSortInfo());

        if(cmd instanceof PagingGetCommand) {
            this.offset = ((PagingGetCommand)cmd).getOffset();
            this.limit = ((PagingGetCommand)cmd).getLimit();
        }
    }


    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public int getLimit() {
        return this.limit;
    }

    @Override
    public int getOffset() {
        return this.offset;
    }

    @Override
    public Style.SortDir getSortDir() {
        return this.sortDir;
    }

    @Override
    public String getSortField() {
        return this.sortField;
    }

    @Override
    public SortInfo getSortInfo() {
        return new SortInfo(sortField, sortDir);
    }

    @Override
    public void setSortDir(Style.SortDir sortDir) {
        this.sortDir = sortDir;
    }

    @Override
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    @Override
    public void setSortInfo(SortInfo info) {
        this.sortField = info.getSortField();
        this.sortDir = info.getSortDir();
    }
}
