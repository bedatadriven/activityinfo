package org.activityinfo.server.endpoint.adminreport.resource;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activityinfo.server.endpoint.adminreport.Grid;

import com.google.inject.Inject;
import com.google.inject.Provider;

public abstract class GridResource {
    protected static final String PARAM_LIMIT = "limit";
    protected static final String PARAM_LIMIT_DEFAULT = "100";
    protected static final String PARAM_DAYS = "days";
    protected static final String PARAM_DAYS_DEFAULT = "30";

    protected static final String HISTORY_AVAILABLE_FROM = "2012-12-20";
    protected static final String DATE_FORMAT = "yyyy-MM-dd";

    protected static final String SQL_DATE_FORMAT = "'%Y-%m-%d'";
    protected static final String SQL_DATETIME_FORMAT = "'%Y-%m-%d %H:%i:%s'";

    protected static final String SQL_LAST_UPDATE =
        "date_format( from_unixtime( substring( max(h.timecreated), 1,10) ), " + SQL_DATE_FORMAT + " ) lastupdate";

    @Inject
    protected Provider<Grid> gridProvider;

    protected String nameId(String nameSql, String idSql) {
        return "ifnull(concat(" + nameSql + ",'&nbsp;('," + idSql + ",')'), 'unknown'), ";
    }

    protected String format(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
}
