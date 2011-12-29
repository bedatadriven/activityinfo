package org.sigmah.client.page.entry.column;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.entry.grouping.AdminGroupingModel;
import org.sigmah.client.page.entry.grouping.GroupingModel;
import org.sigmah.client.page.entry.grouping.NullGroupingModel;
import org.sigmah.client.page.entry.grouping.TimeGroupingModel;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DefaultColumnModelProvider implements ColumnModelProvider {

	private final Dispatcher dispatcher;

	public DefaultColumnModelProvider(Dispatcher dispatcher) {
		super();
		this.dispatcher = dispatcher;
	}

	@Override
	public void get(final Filter filter, final GroupingModel grouping,
			final AsyncCallback<ColumnModel> callback) {

		dispatcher.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(SchemaDTO result) {
				if(filter.isDimensionRestrictedToSingleCategory(DimensionType.Activity)) {
					ActivityDTO singleActivity = result.getActivityById( filter.getRestrictedCategory(DimensionType.Activity));
					callback.onSuccess(forSingleActivity(grouping, singleActivity ));
				} else if(filter.isDimensionRestrictedToSingleCategory(DimensionType.Database)) {
					UserDatabaseDTO singleDatabase = result.getDatabaseById( filter.getRestrictedCategory(DimensionType.Database));
					callback.onSuccess(forSingleDatabase( grouping, singleDatabase ));
				} else {
					callback.onSuccess(forMultipleDatabases(grouping, result) );
				}
			}
		});
	}

	/**
	 * Builds
	 * @param activity
	 */
	private ColumnModel forSingleActivity(GroupingModel grouping, ActivityDTO activity) {
		if(grouping == NullGroupingModel.INSTANCE) {
			return new ColumnModelBuilder()
			.addMapColumn()
			.maybeAddLockColumn(activity)
			.maybeAddDateColumn(activity)
			.maybeAddPartnerColumn(activity.getDatabase())
			.maybeAddProjectColumn(activity.getDatabase())
			.addKeyIndicatorColumns(activity)
			.maybeAddTwoLineLocationColumn(activity)
			.addAdminLevelColumns(activity)
			.build();
		} else if(grouping instanceof AdminGroupingModel) {

			return new ColumnModelBuilder()
			.maybeAddLockColumn(activity)
			.addTreeNameColumn()
			.maybeAddDateColumn(activity)
			.maybeAddPartnerColumn(activity.getDatabase())
			.maybeAddProjectColumn(activity.getDatabase())

			.build();
		} else if(grouping instanceof TimeGroupingModel) {

			return new ColumnModelBuilder()
			.maybeAddLockColumn(activity)
			.addTreeNameColumn()
			.maybeAddDateColumn(activity)
			.maybeAddPartnerColumn(activity.getDatabase())
			.maybeAddProjectColumn(activity.getDatabase())
			.maybeAddSingleLineLocationColumn(activity)
			.addAdminLevelColumns(activity)
			.build();
		} else {
			throw new IllegalArgumentException(grouping.toString());
		}
	}

	private ColumnModel forSingleDatabase(GroupingModel grouping, UserDatabaseDTO database) {
		if(grouping == NullGroupingModel.INSTANCE) {
			return new ColumnModelBuilder()
			.addMapColumn()
			.addActivityColumn(database)
			.addLocationColumn()
			.maybeAddPartnerColumn(database)
			.maybeAddProjectColumn(database)
			.addAdminLevelColumns(database)
			.build();

		} else if(grouping instanceof AdminGroupingModel) {

			return new ColumnModelBuilder()
			.addTreeNameColumn()
			.addActivityColumn(database)
			.maybeAddPartnerColumn(database)
			.maybeAddProjectColumn(database)
			.build();

		} else if(grouping instanceof TimeGroupingModel) {

			return new ColumnModelBuilder()
			.addTreeNameColumn()
			.addActivityColumn(database)
			.maybeAddPartnerColumn(database)
			.maybeAddProjectColumn(database)
			.addLocationColumn()
			.addAdminLevelColumns(database)
			.build();
		} else {
			throw new IllegalArgumentException(grouping.toString());
		}
	}

	private ColumnModel forMultipleDatabases(GroupingModel grouping, SchemaDTO schema) {
		if(grouping == NullGroupingModel.INSTANCE) {
			return new ColumnModelBuilder()
			.addMapColumn()
			.addDatabaseColumn(schema)
			.addActivityColumn(schema)
			.addLocationColumn()
			.addPartnerColumn()
			.build();

		} else if(grouping instanceof AdminGroupingModel) {

			return new ColumnModelBuilder()
			.addTreeNameColumn()
			.addPartnerColumn()
			.build();

		} else if(grouping instanceof TimeGroupingModel) {

			return new ColumnModelBuilder()
			.addTreeNameColumn()
			.addPartnerColumn()
			.addLocationColumn()
			.build();
		} else {
			throw new IllegalArgumentException(grouping.toString());
		}
	}

}
