/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.Observable;
import com.extjs.gxt.ui.client.store.ListStore;
import org.junit.Before;
import org.junit.Test;
import org.sigmah.client.mock.DispatcherStub;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.result.ProjectList;
import org.sigmah.shared.dto.ProjectDTO;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class ProjectListPresenterTest {

    private ProjectViewStub view = new ProjectViewStub();
    private DispatcherStub dispatcher = new DispatcherStub();
    private List<ProjectDTO> projects = new ArrayList<ProjectDTO>();

    @Before
    public void setUp() {
        addProject(1, "Project 1");
        addProject(2, "Project 2");
    }

    private void addProject(int id, String name) {
        ProjectDTO project = new ProjectDTO();
        project.setId(id);
        project.setName(name);
        projects.add(project);
    }

    @Test
    public void storeIsPopulatedOnLoad() {

        dispatcher.setResult(new GetProjects(), new ProjectList(projects));

        ProjectListPresenter presenter = new ProjectListPresenter(dispatcher, view);
        presenter.navigate(new ProjectListState());

        assertThat(view.getStore().getCount(), equalTo(2));

    }

    private class ProjectViewStub implements ProjectListPresenter.View {
        private BaseObservable grid = new BaseObservable();
        private ListStore store = new ListStore();

        @Override
        public Observable getGrid() {
            return grid;
        }

        @Override
        public ListStore getStore() {
            return store;
        }
    }



}
