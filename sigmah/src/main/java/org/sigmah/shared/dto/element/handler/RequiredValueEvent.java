package org.sigmah.shared.dto.element.handler;

import org.sigmah.client.page.project.ProjectPresenter;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event transmitted to the {@link ProjectPresenter} when a <b>required</b> flexible 
 * element value changes.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 *
 */
public class RequiredValueEvent extends GwtEvent<RequiredValueHandler> {
	
	private final static GwtEvent.Type<RequiredValueHandler> TYPE = new GwtEvent.Type<RequiredValueHandler>();
	
	private boolean valueOn;
	
	
	public RequiredValueEvent(boolean valueOn) {
		this.valueOn = valueOn;
	}

	@Override
	protected void dispatch(RequiredValueHandler handler) {
		handler.onRequiredValueChange(this);
	}

	@Override
	public GwtEvent.Type<RequiredValueHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static Type<RequiredValueHandler> getType() {
		return TYPE;
	}
	
	public boolean isValueOn() {
		return valueOn;
	}
	
	public void setValueOn(boolean valueOn) {
		this.valueOn = valueOn;
	}

}
