package org.sigmah.client.report.editor.pivotTable;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.DimensionFolder;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.report.model.AdminDimension;
import org.sigmah.shared.report.model.AttributeGroupDimension;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;

class DimensionTreeProxy implements DataProxy<List<ModelData>> {

	private Dispatcher service;

	DimensionTreeProxy(Dispatcher dispatcher) {
		this.service = dispatcher;
	}

	private SchemaDTO schema;

	@Override
	public void load(DataReader<List<ModelData>> listDataReader,
			final Object parent,
			final AsyncCallback<List<ModelData>> callback) {

		if (schema == null) {
			service.execute(new GetSchema(), null,
					new AsyncCallback<SchemaDTO>() {
						@Override
						public void onFailure(Throwable caught) {
							callback.onFailure(caught);
						}

						@Override
						public void onSuccess(SchemaDTO result) {
							schema = result;
							loadChildren(parent, callback);
						}
					});
		} else {
			loadChildren(parent, callback);
		}
	}

	public void loadChildren(Object parent,
			final AsyncCallback<List<ModelData>> callback) {
		if (parent != null && parent instanceof DimensionFolder) {
			DimensionFolder folder = (DimensionFolder) parent;
			DimensionType type = folder.getType();
			final ArrayList<ModelData> dims = new ArrayList<ModelData>();

			if (type == DimensionType.Date) {
				// add time dimension
				int idSeq = 0;
				dims.add(new DateDimension(I18N.CONSTANTS.year(), idSeq++,
						DateUnit.YEAR, null));
				dims.add(new DateDimension(I18N.CONSTANTS.quarter(),
						idSeq++, DateUnit.QUARTER, null));
				dims.add(new DateDimension(I18N.CONSTANTS.month(), idSeq++,
						DateUnit.MONTH, null));

			} else if (type == DimensionType.AdminLevel) {
				// add geo dimensions
				for (CountryDTO country : schema.getCountries()) {
					for (AdminLevelDTO level : country.getAdminLevels()) {
						dims.add(new AdminDimension(level.getName(), level
								.getId()));
					}
				}

			} else if (type == DimensionType.AttributeGroup) {
				if (folder.getDepth() == 0) {
					// folders for database names
					for (UserDatabaseDTO db : schema.getDatabases()) {
						for (ActivityDTO act : db.getActivities()) {
							if (act.getAttributeGroups() != null
									&& act.getAttributeGroups().size() > 0) {
								dims.add(new DimensionFolder(db.getName(),
										DimensionType.AttributeGroup,
										folder.getDepth() + 1, db.getId()));
								break;
							}
						}
					}

				} else if (folder.getDepth() == 1) {
					// folders for activity names
					UserDatabaseDTO db = schema.getDatabaseById(folder
							.getId());
					for (ActivityDTO act : db.getActivities()) {
						if (act.getAttributeGroups() != null
								&& act.getAttributeGroups().size() > 0) {
							dims.add(new DimensionFolder(act.getName(),
									DimensionType.AttributeGroup, folder
											.getDepth() + 1, act.getId()));
							break;
						}
					}

				} else if (folder.getDepth() == 2) {
					// attribute groups
					ActivityDTO act = schema
							.getActivityById(folder.getId());
					for (AttributeGroupDTO attrGroup : act
							.getAttributeGroups()) {
						dims.add(new AttributeGroupDimension(attrGroup
								.getName(), attrGroup.getId()));
					}

				} else {
					assert false;
				}
			} else {
				assert false;
			}
			callback.onSuccess(dims);
		}
	}
}