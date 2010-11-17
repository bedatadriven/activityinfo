/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.policy;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sigmah.server.dao.ProjectReportDAO;
import org.sigmah.server.dao.Transactional;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.report.ProjectReport;
import org.sigmah.shared.domain.report.ProjectReportModel;
import org.sigmah.shared.domain.report.ProjectReportModelSection;
import org.sigmah.shared.domain.report.RichTextElement;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectReportPolicy implements EntityPolicy<ProjectReport> {
    final ProjectReportDAO dao;

    @Inject
    public ProjectReportPolicy(ProjectReportDAO dao) {
        this.dao = dao;
    }

    @Override
    public Object create(User user, PropertyMap properties) {
        return createReport(user, properties).getId();
    }

    private void iterateOnSection(ProjectReportModelSection section, List<RichTextElement> elements, ProjectReport report) {
        int index = 0;
        int areaCount = section.getNumberOfTextarea();

        List<ProjectReportModelSection> subSections = section.getSubSections();
        if(subSections == null)
            subSections = Collections.emptyList();

        for(final ProjectReportModelSection subSection : subSections) {
            while(index < subSection.getIndex() && areaCount > 0) {
                // New rich text element
                final RichTextElement element = new RichTextElement();
                element.setIndex(index);
                element.setSectionId(section.getId());
                element.setReport(report);
                elements.add(element);

                index++;
                areaCount--;
            }
            
            iterateOnSection(subSection, elements, report);
        }

        while(areaCount > 0) {
            // New rich text element
            final RichTextElement element = new RichTextElement();
            element.setIndex(index);
            element.setSectionId(section.getId());
            element.setReport(report);
            elements.add(element);

            index++;
            areaCount--;
        }
    }

    @Transactional
    public ProjectReport createReport(User user, PropertyMap properties) {
        final ProjectReport report = new ProjectReport();

        // Defining the common properties
        report.setEditor(user);
        report.setName((String) properties.get("name"));
        report.setLastEditDate(new Date());
        report.setPhaseName((String) properties.get("phaseName"));

        final ProjectReportModel model = dao.findModelById((Integer) properties.get("reportModelId"));
        report.setModel(model);

        final Project project = new Project();
        project.setId((Integer) properties.get("projectId"));
        report.setProject(project);

        // RichTextElements
        final ArrayList<RichTextElement> elements = new ArrayList<RichTextElement>();

        for(final ProjectReportModelSection section : model.getSections())
            iterateOnSection(section, elements, report);

        report.setTexts(elements);

        // Saving
        dao.persist(report);

        return report;
    }

    @Override
    public void update(User user, Object entityId, PropertyMap changes) {
        for(Map.Entry<String, Object> entry : changes.entrySet()) {
            final RichTextElement element = dao.findRichTextElementById(new Integer(entry.getKey()));
            element.setText((String) entry.getValue());
            dao.merge(element);
        }
    }
    
}
