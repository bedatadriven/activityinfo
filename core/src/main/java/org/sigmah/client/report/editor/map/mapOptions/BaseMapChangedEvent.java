package org.sigmah.client.report.editor.map.mapOptions;

import org.sigmah.shared.map.BaseMap;

import com.google.gwt.event.shared.GwtEvent;


public class BaseMapChangedEvent extends GwtEvent<BasemapChangedEventHandler> {
	public static final Type<BasemapChangedEventHandler> TYPE = new Type<BasemapChangedEventHandler>();
	
	private BaseMap baseMap;
	

	public BaseMapChangedEvent(BaseMap baseMap) {
		super();
		this.baseMap = baseMap;
	}

	public BaseMap getBaseMap() {
		return baseMap;
	}

	public void setBaseMap(BaseMap baseMap) {
		this.baseMap = baseMap;
	}

	@Override
	public Type getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(BasemapChangedEventHandler handler) {
		handler.onBaseMapChanged(this);
	}
	
}
