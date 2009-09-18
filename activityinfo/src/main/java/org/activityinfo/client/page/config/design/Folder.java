package org.activityinfo.client.page.config.design;

import com.extjs.gxt.ui.client.data.BaseModelData;

import org.activityinfo.shared.dto.ActivityModel;

public class Folder extends BaseModelData {

    private ActivityModel activity;

    public Folder(ActivityModel activity, String name) {
        this.activity = activity;
        set("name", name);
    }

    public ActivityModel getActivity() {
        return activity;
    }
}
