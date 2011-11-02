/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetAdminEntitiesHandlerTest extends CommandTestCase2 {

    private static final int PROVINCE = 1;


    @Test
    public void testRootLevelQuery() throws Exception {

        GetAdminEntities cmd = new GetAdminEntities(PROVINCE);

        AdminEntityResult result = execute(cmd);

        assertThat(result.getData().size(), equalTo(4));
    }


    @Test
    public void testChildQuery() throws Exception {

        GetAdminEntities cmd = new GetAdminEntities(2, 2);

        AdminEntityResult result = execute(cmd);

        assertThat(result.getData().size(), equalTo(3));
        
        AdminEntityDTO kalehe = result.getData().get(0);
        assertThat(kalehe.getName(), equalTo("Kalehe"));
        assertThat(kalehe.getBounds(), is(not(nullValue())));
        assertThat(kalehe.getBounds().getX1(), equalTo(-44d));
        assertThat(kalehe.getBounds().getY1(), equalTo(-22d));
        assertThat(kalehe.getBounds().getX2(), equalTo(33.5d));
        assertThat(kalehe.getBounds().getY2(), equalTo(40d));
    }

    @Test
    public void testSiteQuery() throws Exception {
        GetAdminEntities cmd = new GetAdminEntities();
        cmd.setLevelId(1);
        cmd.setFilter(Filter.filter().onActivity(2));

        AdminEntityResult result = execute(cmd);

        assertThat(result.getData().size(), equalTo(3));
    }
}
