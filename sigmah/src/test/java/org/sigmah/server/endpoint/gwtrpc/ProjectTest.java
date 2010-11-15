/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.server.endpoint.gwtrpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetProject;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.PhaseDTO;
import org.sigmah.shared.dto.PhaseModelDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.ProjectModelDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.QuestionChoiceElementDTO;
import org.sigmah.shared.dto.element.QuestionElementDTO;
import org.sigmah.shared.dto.layout.LayoutConstraintDTO;
import org.sigmah.shared.dto.layout.LayoutDTO;
import org.sigmah.shared.dto.layout.LayoutGroupDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/projects.db.xml")
public class ProjectTest extends CommandTestCase {

    @Test
    public void getProjects() throws CommandException {

        setUser(1);

        GetProjects cmd = new GetProjects();

        ProjectListResult result = execute(cmd);

        Assert.assertThat(result.getList().size(), CoreMatchers.equalTo(2));
    }

    @Test
    public void createProject() throws CommandException {

        setUser(1);

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("name", "First p name");
        properties.put("fullName", "First p full");
        properties.put("budget", 150000.0);
        properties.put("modelId", 1L);
        properties.put("countryId", 1);
        properties.put("calendarName", "events");

        CreateEntity cmd = new CreateEntity("Project", properties);

        CreateResult createResult = execute(cmd);

        Assert.assertThat(createResult.getEntity(), CoreMatchers.not(CoreMatchers.nullValue()));

    }

    @Test
    public void projectsWithOrgRightsShouldBeVisible() throws CommandException {

        setUser(3);

        GetProjects cmd = new GetProjects();

        ProjectListResult result = execute(cmd);

        Assert.assertThat(result.getList().size(), CoreMatchers.equalTo(1));
    }

    @Test
    public void phaseModelDisplayOrderTest() throws CommandException {
        setUser(1);

        final GetProject cmd = new GetProject(1);
        final ProjectDTO project = execute(cmd);

        final ProjectModelDTO projectModel = project.getProjectModelDTO();
        Assert.assertThat(projectModel, CoreMatchers.notNullValue());

        final List<PhaseModelDTO> phaseModels = projectModel.getPhaseModelsDTO();
        Assert.assertThat(phaseModels, CoreMatchers.notNullValue());

        // Verifying the phases display order
        Assert.assertThat(phaseModels.get(2).getName(), CoreMatchers.equalTo("Step 2"));
    }

    @Test
    public void flexibleElementsTest() throws CommandException {
        final int projectId = 1;
        setUser(1);

        // Flexible elements defined in the test database
        final ElementDefinition[] definitions = new ElementDefinition[] {
            new ElementDefinition("Success", false, "element.MessageElement"),
            new ElementDefinition("Check this", true, "element.CheckboxElement", new CustomVerificator() {
                @Override
                public void verify(FlexibleElementDTO element, ValueResult valueResult) {
                    Assert.assertThat(element.isCorrectRequiredValue(valueResult), CoreMatchers.equalTo(true));
                }
            }),
            new ElementDefinition("Question 1", false, "element.QuestionElement", new CustomVerificator() {
                @Override
                public void verify(FlexibleElementDTO element, ValueResult valueResult) {
                    final QuestionElementDTO questionElementDTO = (QuestionElementDTO) element;
                    final List<QuestionChoiceElementDTO> choices = questionElementDTO.getChoicesDTO();
                    Assert.assertThat(choices, CoreMatchers.notNullValue());
                    Assert.assertThat(choices.size(), CoreMatchers.equalTo(2));
                }
            }),
            new ElementDefinition("Question 2", true, "element.QuestionElement", new CustomVerificator() {
                @Override
                public void verify(FlexibleElementDTO element, ValueResult valueResult) {
                    final QuestionElementDTO questionElementDTO = (QuestionElementDTO) element;
                    final List<QuestionChoiceElementDTO> choices = questionElementDTO.getChoicesDTO();
                    Assert.assertThat(choices, CoreMatchers.notNullValue());
                    Assert.assertThat(choices.size(), CoreMatchers.equalTo(3));

                    // Sort order
                    final QuestionChoiceElementDTO third = choices.get(2);
                    Assert.assertThat(third, CoreMatchers.notNullValue());
                    Assert.assertThat(third.getLabel(), CoreMatchers.equalTo("Answer 3"));
                }
            }),
            new ElementDefinition("Comments", false, "element.TextAreaElement", new CustomVerificator() {
                @Override
                public void verify(FlexibleElementDTO element, ValueResult valueResult) {
                    Assert.assertThat((String)valueResult.getValueObject(), CoreMatchers.equalTo("Something"));
                }
            })
        };

        // Retrieving the project
        final GetProject cmd = new GetProject(projectId);
        final ProjectDTO project = execute(cmd);

        Assert.assertThat(project.getId(), CoreMatchers.equalTo(projectId));

        // Retrieving the layout groups
        final ProjectModelDTO projectModel = project.getProjectModelDTO();
        Assert.assertThat(projectModel, CoreMatchers.notNullValue());

        final List<PhaseModelDTO> phaseModels = projectModel.getPhaseModelsDTO();
        Assert.assertThat(phaseModels, CoreMatchers.notNullValue());
        Assert.assertThat(phaseModels.size(), CoreMatchers.equalTo(4));

        final List<PhaseDTO> phases = project.getPhasesDTO();
        Assert.assertThat(phases, CoreMatchers.notNullValue());
        Assert.assertThat(phases.size(), CoreMatchers.equalTo(4));

        final PhaseDTO phase = phases.get(0);
        Assert.assertThat(phase, CoreMatchers.notNullValue());

        final PhaseModelDTO phaseModel = phase.getPhaseModelDTO();
        final LayoutDTO layout = phaseModel.getLayoutDTO();
        Assert.assertThat(layout, CoreMatchers.notNullValue());

        final List<LayoutGroupDTO> groups = layout.getLayoutGroupsDTO();
        Assert.assertThat(groups, CoreMatchers.notNullValue());

        // Testing the flexible elements
        int index = 0;
        for (final LayoutGroupDTO group : groups) {
            Assert.assertThat(group, CoreMatchers.notNullValue());

            final List<LayoutConstraintDTO> constraints = group.getLayoutConstraintsDTO();
            Assert.assertThat(constraints, CoreMatchers.notNullValue());
            for (final LayoutConstraintDTO constraint : constraints) {
                Assert.assertThat(constraint, CoreMatchers.notNullValue());

                final FlexibleElementDTO element = constraint.getFlexibleElementDTO();
                Assert.assertThat(element, CoreMatchers.notNullValue());

                final GetValue command = new GetValue(projectId, element.getId(), element.getEntityName());
                final ValueResult result = execute(command);

                element.setCurrentProjectDTO(project);
                element.assignValue(result);

                final ElementDefinition definition = definitions[index];

                Assert.assertThat(element.getLabel(), CoreMatchers.equalTo(definition.getLabel()));
                Assert.assertThat(element.getValidates(), CoreMatchers.equalTo(definition.isValidates()));
                Assert.assertThat(element.getEntityName(), CoreMatchers.equalTo(definition.getEntityName()));

                if(definition.getCustomVerificator() != null)
                    definition.getCustomVerificator().verify(element, result);

                index++;
            }
        }

        Assert.assertThat(index, CoreMatchers.equalTo(definitions.length));
    }

    private static interface CustomVerificator {
        void verify(FlexibleElementDTO element, ValueResult valueResult);
    }

    private static class ElementDefinition {
        private final String label;
        private final boolean validates;
        private final String entityName;
        private final CustomVerificator customVerificator;

        public ElementDefinition(String label, boolean validates, String entityName) {
            this.label = label;
            this.validates = validates;
            this.entityName = entityName;
            this.customVerificator = null;
        }

        public ElementDefinition(String label, boolean validates, String entityName, CustomVerificator customVerificator) {
            this.label = label;
            this.validates = validates;
            this.entityName = entityName;
            this.customVerificator = customVerificator;
        }

        public String getEntityName() {
            return entityName;
        }

        public String getLabel() {
            return label;
        }

        public boolean isValidates() {
            return validates;
        }

        public CustomVerificator getCustomVerificator() {
            return customVerificator;
        }
    }
}
