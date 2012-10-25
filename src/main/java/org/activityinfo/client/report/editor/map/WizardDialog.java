package org.activityinfo.client.report.editor.map;


import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WizardDialog extends Window {
	
	private Button prevButton;
	private Button nextButton;
	private Button cancelButton;
	private Button finishButton;
	private WizardPage[] pages;
	private Wizard wizard;
	private CardLayout cardLayout;
	private int currentPageIndex;
	private WizardCallback callback = new WizardCallback();

	public WizardDialog(Wizard wizard) {
		this.wizard = wizard;
		this.pages = wizard.getPages();
		
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		setWidth(450);
		setHeight(350);
		setHeading(wizard.getTitle());
		
		Listener<ComponentEvent> pageListener = new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent be) {
				enableButtons();
			}
		};
		
		for(WizardPage page : pages) {
			page.addListener(Events.Enable, pageListener);
			page.addListener(Events.Disable, pageListener);
			add(page);
		}
		
		addButton(cancelButton = new Button("Cancel", new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				onCancel();
			}
		}));
		addButton(prevButton = new Button("&laquo; Back", new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				onBack();
			}
		}));
		addButton(nextButton = new Button("Next &raquo;", new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				onNext();
			}
		}));
		addButton(finishButton = new Button("Finish", new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				onFinish();
			}
		}));
		
		setPage(0);
	}
	
	private void enableForm(boolean enabled) {
		prevButton.setEnabled(enabled);
		nextButton.setEnabled(enabled);
		finishButton.setEnabled(enabled);
		cancelButton.setEnabled(enabled);
	}
	
	protected void onFinish() {
		enableForm(false);
		this.callback.finish(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				enableForm(true);
				WizardDialog.this.hide();
				callback.onFinished();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				enableForm(true);
			}
		});
	}

	protected void onNext() {
		setPage(nextPageIndex());
	}

	protected void onBack() {
		setPage(prevPageIndex());
	}

	protected void onCancel() {
		this.hide();
	}
	
	public void show(WizardCallback callback) {
		this.callback = callback;
		super.show();
	}

	private void setPage(int pageIndex) {
		finishButton.setEnabled(nextPageIndex()+1 == pages.length);
		cardLayout.setActiveItem(pages[pageIndex]);

		currentPageIndex = pageIndex;
		enableButtons();
	}

	private void enableButtons() {
		prevButton.setEnabled(prevPageIndex() >= 0);
		nextButton.setEnabled(nextPageIndex() < pages.length);
	}
	
	private int prevPageIndex() {
		int i=currentPageIndex-1;
		if(i >= 0 && !pages[i].isEnabled()) {
			i--;
		}
		return i;
	}
	
	private int nextPageIndex() {
		int i=currentPageIndex+1;
		if(i<pages.length && !pages[i].isEnabled()) {
			i++;
		}
		return i;
	}
}
