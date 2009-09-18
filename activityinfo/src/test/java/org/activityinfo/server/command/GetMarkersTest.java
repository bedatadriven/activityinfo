package org.activityinfo.server.command;

import static org.easymock.EasyMock.*;

import org.activityinfo.server.command.handler.GetMarkersHandler;
import org.activityinfo.server.dao.SiteProjectionBinder;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.GetMarkers;
import org.activityinfo.shared.dto.SiteMarkerCollection;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.content.SiteData;
import org.easymock.EasyMock;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GetMarkersTest {


    @Test
    public void test() throws CommandException {

        // mock user
        User user = new User();

        // mock data
        List<SiteData> list = new ArrayList<SiteData>();
        list.add(new SiteData(new Object[] { 1, 1, "NFI Dist", 1, "PEAR", new Date(), new Date(), 1,
                1, "NRC", "Kisungu", "Bukavu-Hombo", "Localite", "Comments", 28.0, -1.3 }));
        list.add(new SiteData(new Object[] { 2, 1, "NFI Dist", 1, "PEAR", new Date(), new Date(), 1,
                 1, "NRC", "Kisungu", "Bukavu-Hombo", "Localite", "Comments", 28.5, -1.2 }));
        list.add(new SiteData(new Object[] { 3, 1, "NFI Dist", 1, "PEAR", new Date(), new Date(), 1,
                   1, "NRC", "Kisungu", "Bukavu-Hombo", "Localite", "Comments", null, null }));

        // command object
        GetMarkers cmd = new GetMarkers(7);

        // collaborator: DAO
        SiteTableDAO dao = createMock(SiteTableDAO.class);
        expect(dao.query(eq(user), EasyMock.<Criterion>isNull(), EasyMock.<List<Order>>isNull(),
                isA(SiteProjectionBinder.class), anyInt(), anyInt(), anyInt())).andReturn(list);
        replay(dao);

        // CLASS UNDER TEST
        GetMarkersHandler handler = new GetMarkersHandler(dao);

        // VERIFY returns result
        SiteMarkerCollection markers = (SiteMarkerCollection) handler.execute(cmd, user);

        Assert.assertEquals(2, markers.getSites().size());


    }

}
