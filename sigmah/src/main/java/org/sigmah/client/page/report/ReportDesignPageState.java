package org.sigmah.client.page.report;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.shared.dto.ReportDefinitionDTO;

public class ReportDesignPageState implements PageState {
	
	public int reportId;
	
	public ReportDesignPageState(){
		
	}
	
	public ReportDesignPageState(int reportId){
		this.reportId = reportId;
	}

	@Override
	public String serializeAsHistoryToken() {
		return null;
	}

	@Override
	public PageId getPageId() {
		return ReportDesignPresenter.PAGE_ID;
	}

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(ReportDesignPresenter.PAGE_ID);
	}
	
	 public static class Parser implements PageStateParser {
	        @Override
	        public PageState parse(String token) {
	            return new ReportDesignPageState();
	        }
	    }

}
