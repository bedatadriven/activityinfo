/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.sync;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;
import org.json.JSONException;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.shared.domain.ReportingPeriod;
import org.sigmah.shared.domain.Site;
import org.sigmah.shared.domain.User;

public class SiteTableUpdateBuilder implements UpdateBuilder {

    private final JpaUpdateBuilder builder = new JpaUpdateBuilder();

    @Override
    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request) throws JSONException {
        SyncRegionUpdate update = new SyncRegionUpdate();
        update.setComplete(true);
        update.setVersion("1");

        if(request.getLocalVersion() == null ) {

            builder.createTableIfNotExists(Site.class);
            builder.createTableIfNotExists(ReportingPeriod.class);
            // TODO: fix rebar to handle these types of classes correctly
            builder.executeStatement("create table if not exists AttributeValue (SiteId integer, AttributeId integer, Value integer)");
            builder.executeStatement("create table if not exists IndicatorValue (ReportingPeriodId integer, IndicatorId integer, Value real)");
            update.setSql(builder.asJson());
        }

        return update;
    }
}
