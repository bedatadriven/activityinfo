package org.sigmah.client.page.dashboard.portlets;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.mvp.CanRefresh;
import org.sigmah.shared.dto.portlets.PortletDTO;

import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

/*
 * Convenience base class for an AI portlet
 */
public abstract class BasePortlet<P extends PortletDTO> extends Portlet implements PortletView<P> {
	protected P portlet;
	protected EventBus eventBus = new SimpleEventBus();
	protected AsyncMonitor loadingMonitor = new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
	protected ToolButton toolbuttonClose;
	protected ToolButton toolbuttonRefresh;
	
	public BasePortlet(P portlet) {
		super();
		
		this.portlet = portlet;
		
		initializeComponent();
		
		if (supportsClose()) {
			createCloseButton();
		}
		if (supportsRefresh()) {
			createRefreshButton();
		}
		if (supportsCollapsing()) {
			setCollapsible(true);
		}
	}

	private void initializeComponent() {
		setLayout(new FitLayout());
	}

	private void createRefreshButton() {
		toolbuttonRefresh = new ToolButton("x-tool-refresh",
			new SelectionListener<IconButtonEvent>() {
				@Override
				public void componentSelected(IconButtonEvent ce) {
					eventBus.fireEvent(new RefreshEvent());
				}
			});
		getHeader().addTool(toolbuttonRefresh);
	}

	private void createCloseButton() {
		toolbuttonClose =  new ToolButton("x-tool-close",
			new SelectionListener<IconButtonEvent>() {
				@Override
				public void componentSelected(IconButtonEvent ce) {
					removeFromParent();
				}
			}
		);
		getHeader().addTool(toolbuttonClose);
	}

	@Override
	public P getValue() {
		return portlet;
	}

	@Override
	public void setValue(P value) {
	}

	@Override
	public void setValue(P value, boolean fireEvents) {
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<P> handler) {
		return eventBus.addHandler(ValueChangeEvent.getType(), handler);
	}

	@Override
	public HandlerRegistration addRefreshHandler(CanRefresh.RefreshHandler handler) {
		return eventBus.addHandler(RefreshEvent.TYPE, handler);
	}

	@Override
	public Portlet asPortlet() {
		return this;
	}

	@Override
	public AsyncMonitor loadingMonitor() {
		return loadingMonitor;
	}

	@Override
	public void setRefreshEnabled(boolean canRefresh) {
		if (supportsRefresh()) {
			toolbuttonRefresh.setEnabled(canRefresh);
		}
	}
	
	protected abstract boolean supportsClose();
	protected abstract boolean supportsConfig();
	protected abstract boolean supportsRefresh();
	protected abstract boolean supportsCollapsing();
}
