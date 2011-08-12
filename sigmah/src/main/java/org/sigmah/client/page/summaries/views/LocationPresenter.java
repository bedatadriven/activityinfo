package org.sigmah.client.page.summaries.views;

import java.util.HashSet;
import java.util.Set;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.mvp.View;
import org.sigmah.client.page.entry.editor.MapView;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;

public class LocationPresenter {
	public interface LocationView extends View<LocationDTO> {
		public void setActivity(ActivityDTO activity);
	}
	
	public class LocationViewImpl extends LayoutContainer implements LocationView {
		private FieldSet fieldsetLocation;
		private LayoutContainer left;
		private LayoutContainer right;
		private ActivityDTO activity;
		private Set<LabelField> adminLevelLabels = new HashSet<LabelField>(); 
		private MapLocationPresenter mapPresenter;
		private MapView mapLocationView;
		private LocationDTO location;
		private LabelField labelLocationName;
		private LabelField labelLocationAxe;
		
		public LocationViewImpl() {
			initializeComponent();
			
			// Right column
			createFields();
			
			// Left column
			createMapLocationView();
		}

		private void createFields() {
			labelLocationName = new LabelField();
			labelLocationName.setFieldLabel(I18N.CONSTANTS.location());
			right.add(labelLocationName);

			labelLocationAxe = new LabelField();
			labelLocationAxe.setFieldLabel(I18N.CONSTANTS.axe());
			right.add(labelLocationAxe);
		}

		private void createMapLocationView() {
			mapLocationView = new MapLocationView(new CountryDTO()); 
			mapPresenter = new MapLocationPresenter(null, null, mapLocationView);
			
			right.add(mapLocationView.asWidget());
		}

		private void initializeComponent() {

			// Put all into a big fieldset
			fieldsetLocation = new FieldSet();
			fieldsetLocation.setHeading(I18N.CONSTANTS.location());
			fieldsetLocation.setLayout(new ColumnLayout());
			add(fieldsetLocation);
		
			// let the fieldset have 2 even columns, one for fields, one for a map
			left = new LayoutContainer();
			right = new LayoutContainer();
			fieldsetLocation.add(left, new ColumnData(.5));
			fieldsetLocation.add(right, new ColumnData(.5));
		}

		@Override
		public void initialize() {
		}

		@Override
		public AsyncMonitor getLoadingMonitor() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setValue(LocationDTO value) {
			this.location = value;
			labelLocationName.setText(location.getName());
			labelLocationAxe.setText(location.getAxe());
		}

		@Override
		public LocationDTO getValue() {
			return location;
		}

		@Override
		public void setActivity(ActivityDTO activity) {
			this.activity = activity;
			
			createAdminLevelsUI();
			//mapPresenter.setLocation(site.getl)
		}

		private void createAdminLevelsUI() {
			removeOldLabels();
			populateAdminLevelLabels();
		}

		private void populateAdminLevelLabels() {
			for (AdminLevelDTO adminLevel : activity.getAdminLevels()) {
				LabelField labelAdminLevel = new LabelField();
				labelAdminLevel.setFieldLabel(adminLevel.getName());
				labelAdminLevel.setText(adminLevel.getPropertyName());
				left.add(labelAdminLevel);
				adminLevelLabels.add(labelAdminLevel);
			}
		}

		private void removeOldLabels() {
			for (LabelField labelAdminLevel : adminLevelLabels) {
				left.remove(labelAdminLevel);
			}
			
			adminLevelLabels.clear();
		}
		
	}
}
