package org.sigmah.shared.report.model.layers;

/**
 * Map Layer which tries to visualize all available map data optimized for usability. 
 */
public class AutoMapLayer extends AbstractMapLayer {

	@Override
	public boolean supportsMultipleIndicators() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		return "Automatic";
	}

	@Override
	public String getInternationalizedName() {
		return "Automatic";
	}

}
