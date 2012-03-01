package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

public class UpdateIndicatorLink implements Command<VoidResult> {
	private int sourceIndicator;
	private int destinationIndicators;
	
	private boolean delete;

	public UpdateIndicatorLink() {

	}

	public UpdateIndicatorLink(int sourceIndicator, int destinationIndicators, boolean delete) {
		this.sourceIndicator = sourceIndicator;
		this.destinationIndicators = destinationIndicators;
		this.delete = delete;
	}
	
	public int getSourceIndicator() {
		return sourceIndicator;
	}

	public void setSourceIndicator(int sourceIndicator) {
		this.sourceIndicator = sourceIndicator;
	}

	public int getDestinationIndicators() {
		return destinationIndicators;
	}

	public void setDestinationIndicators(int destinationIndicators) {
		this.destinationIndicators = destinationIndicators;
	}
	
	public void setDelete(boolean delete){
		this.delete = delete;
	}
	
	public boolean isDelete(){
		return this.delete;
	}
}
