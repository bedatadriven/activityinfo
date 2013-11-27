package org.activityinfo.server.endpoint.export;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.LocationTypeDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import com.teklabs.gwt.i18n.server.LocaleProxy;

public class SiteExporterTest {

    @Test
    public void sheetNameTest() {
        
        LocaleProxy.initialize();
        
        CountryDTO somalia = new CountryDTO(1, "Somalia");
        somalia.getLocationTypes().add(new LocationTypeDTO(1, "Village"));
        
        UserDatabaseDTO syli = new UserDatabaseDTO();
        syli.setName("SYLI");
        syli.setCountry(somalia);
        
        ActivityDTO activity = new ActivityDTO();
        activity.setId(1);
        activity.setDatabase(syli);
        activity.setName("Construction/Rehabilitation of Sec. Schools");
        activity.setLocationTypeId(1);
        
        ActivityDTO activity2 = new ActivityDTO();
        activity2.setId(2);
        activity2.setDatabase(syli);
        activity2.setName("Construction/Rehabilitation of Primary Schools");
        activity2.setLocationTypeId(1);
        
        ActivityDTO activity3 = new ActivityDTO();
        activity3.setId(3);
        activity3.setDatabase(syli);
        activity3.setName("Construction Rehabil (2)");
        activity3.setLocationTypeId(1);
        
        
        DispatcherSync dispatcher = createMock(DispatcherSync.class);
        expect(dispatcher.execute(isA(GetSites.class))).andReturn(new SiteResult(new ArrayList<SiteDTO>())).anyTimes();
        replay(dispatcher);
        
        Filter filter = new Filter();
        
        SiteExporter exporter = new SiteExporter(dispatcher);
        exporter.export(activity, filter);
        exporter.export(activity2, filter);
        exporter.export(activity3, filter);
        HSSFWorkbook book = exporter.getBook();
        
        assertThat(book.getSheetAt(0).getSheetName(), equalTo("SYLI - Construction Rehabilitat"));
        assertThat(book.getSheetAt(1).getSheetName(), equalTo("SYLI - Construction Rehabil (2)"));
        assertThat(book.getSheetAt(2).getSheetName(), equalTo("SYLI - Construction Rehabil 2"));        
    }
}
