/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(InjectionSupport.class)
public class ProjectTest extends CommandTestCase {
    
    @Test
    @OnDataSet("/dbunit/schema1.db.xml")
    public void createProject() throws CommandException {

        setUser(1);

        Map<String,Object> properties = new HashMap<String, Object>();
        properties.put("name", "My first project");
        properties.put("countryId", 1);
        
        CreateEntity cmd = new CreateEntity("Project", properties);

        CreateResult createResult = execute(cmd);

        assertThat(createResult.getNewId(), not(equalTo(0)));

    }

    @Test
    @OnDataSet("/dbunit/projects.db.xml")
    public void getProjects() throws CommandException {

        setUser(1);

        GetProjects cmd = new GetProjects();

        ProjectListResult result = execute(cmd);

        assertThat(result.getList().size(), equalTo(2));
    }


    @Test
    @OnDataSet("/dbunit/projects.db.xml")
    public void projectsWithOrgRightsShouldBeVisible() throws CommandException {

        setUser(3);

        GetProjects cmd = new GetProjects();

        ProjectListResult result = execute(cmd);

        assertThat(result.getList().size(), equalTo(1));
    }
}
