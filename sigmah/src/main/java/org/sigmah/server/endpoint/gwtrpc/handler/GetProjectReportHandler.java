/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.sigmah.shared.command.GetProjectReport;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.report.KeyQuestion;
import org.sigmah.shared.domain.report.ProjectReport;
import org.sigmah.shared.domain.report.ProjectReportModel;
import org.sigmah.shared.domain.report.ProjectReportModelSection;
import org.sigmah.shared.domain.report.ProjectReportVersion;
import org.sigmah.shared.domain.report.RichTextElement;
import org.sigmah.shared.dto.report.KeyQuestionDTO;
import org.sigmah.shared.dto.report.ProjectReportContent;
import org.sigmah.shared.dto.report.ProjectReportDTO;
import org.sigmah.shared.dto.report.ProjectReportSectionDTO;
import org.sigmah.shared.dto.report.RichTextElementDTO;
import org.sigmah.shared.exception.CommandException;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetProjectReportHandler implements CommandHandler<GetProjectReport> {
    private EntityManager em;

    @Inject
    public GetProjectReportHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(GetProjectReport cmd, User user) throws CommandException {
        Query query = em.createQuery("SELECT r FROM ProjectReport r WHERE r.id = :reportId");
        query.setParameter("reportId", cmd.getReportId());

        ProjectReportDTO reportDTO = null;

        try {
            final ProjectReport report = (ProjectReport) query.getSingleResult();
            ProjectReportVersion version = report.getCurrentVersion();

            // Looking for a draft
            query = em.createQuery("SELECT v FROM ProjectReportVersion v WHERE v.report.id = :reportId AND v.editor = :user AND v.version IS NULL");
            query.setParameter("reportId", cmd.getReportId());
            query.setParameter("user", user);

            try {
                version = (ProjectReportVersion) query.getSingleResult();
            } catch (NoResultException e) {
                // No draft for the current user
            }

            reportDTO = toDTO(report, version);

        } catch (NoResultException e) {
            // Bad report id
        }

        return reportDTO;
    }

    /**
     * Convert the given report into a ProjectReportDTO.
     * @param report A project report.
     * @param version A version of this report.
     * @return A ProjectReportDTO.
     */
    public static ProjectReportDTO toDTO(ProjectReport report, ProjectReportVersion version) {
        final ProjectReportDTO reportDTO = new ProjectReportDTO();
        
        reportDTO.setId(report.getId());
        reportDTO.setVersionId(version.getId());
        reportDTO.setName(report.getName());
        reportDTO.setPhaseName(version.getPhaseName());
        reportDTO.setDraft(version.getVersion() == null);
        reportDTO.setLastEditDate(version.getEditDate());
        reportDTO.setEditorName(version.getEditorShortName());

        if(report.getProject() != null)
                reportDTO.setProjectId(report.getProject().getId());

        final ProjectReportModel model = report.getModel();

        final List<ProjectReportModelSection> sectionModels = model.getSections();
        final HashMap<Integer, List<RichTextElement>> richTextElements = organizeElementsBySection(version.getTexts());

        final ArrayList<ProjectReportSectionDTO> sectionDTOs = new ArrayList<ProjectReportSectionDTO>();
        for(ProjectReportModelSection sectionModel : sectionModels)
            sectionDTOs.add(iterateOnSection(sectionModel, richTextElements));

        reportDTO.setSections(sectionDTOs);

        return reportDTO;
    }

    /**
     * Order the rich text elements by section.
     * @param elements Rich text elements.
     * @return A map containing lists of rich text elements.
     */
    private static HashMap<Integer, List<RichTextElement>> organizeElementsBySection(List<RichTextElement> elements) {
        final HashMap<Integer, List<RichTextElement>> map = new HashMap<Integer, List<RichTextElement>>();

        for(final RichTextElement element : elements) {
            List<RichTextElement> list = map.get(element.getSectionId());

            if(list == null) {
                list = new ArrayList<RichTextElement>();
                map.put(element.getSectionId(), list);
            }

            list.add(element);
        }

        return map;
    }

    private static ProjectReportSectionDTO iterateOnSection(ProjectReportModelSection sectionModel, HashMap<Integer, List<RichTextElement>> richTextElements) {
       final ProjectReportSectionDTO sectionDTO = new ProjectReportSectionDTO();
       sectionDTO.setId(sectionModel.getId());
       sectionDTO.setName(sectionModel.getName());

       List<RichTextElement> elementList = richTextElements.get(sectionModel.getId());
       if(elementList == null)
           elementList = Collections.emptyList();

       final Iterator<RichTextElement> elementIterator = elementList.iterator();
       final Iterator<KeyQuestion> keyQuestionIterator = sectionModel.getKeyQuestions().iterator();
       final Iterator<ProjectReportModelSection> subSectionIterator = sectionModel.getSubSections().iterator();

       // Children of this section
       final ArrayList<ProjectReportContent> children = new ArrayList<ProjectReportContent>();

       // Next rich text element
       RichTextElement nextElement;
       if(elementIterator.hasNext())
           nextElement = elementIterator.next();
       else
           nextElement = null;

       // Key questions
       int keys = 0;
       while(keyQuestionIterator.hasNext()) {
           final KeyQuestion keyQuestion = keyQuestionIterator.next();
           final KeyQuestionDTO keyQuestionDTO = new KeyQuestionDTO();

           keyQuestionDTO.setId(keyQuestion.getId());
           keyQuestionDTO.setLabel(keyQuestion.getLabel());

           final RichTextElementDTO elementDTO = new RichTextElementDTO();
           elementDTO.setId(nextElement.getId());
           elementDTO.setText(nextElement.getText());

           keyQuestionDTO.setRichTextElementDTO(elementDTO);

           if(elementIterator.hasNext())
               nextElement = elementIterator.next();
           else
               nextElement = null;

           children.add(keyQuestionDTO);
           keys++;
       }

       // Sub sections
       while(subSectionIterator.hasNext()) {
           final ProjectReportModelSection subSectionModel = subSectionIterator.next();
           
           while(nextElement != null && nextElement.getIndex()-keys < subSectionModel.getIndex()) {
               final RichTextElementDTO elementDTO = new RichTextElementDTO();
               elementDTO.setId(nextElement.getId());
               elementDTO.setText(nextElement.getText());
               children.add(elementDTO);

               if(elementIterator.hasNext())
                   nextElement = elementIterator.next();
               else
                   nextElement = null;
           }

           final ProjectReportSectionDTO subSectionDTO = iterateOnSection(subSectionModel, richTextElements);
           children.add(subSectionDTO);
       }

       // Remaining elements
       while(nextElement != null) {
           final RichTextElementDTO elementDTO = new RichTextElementDTO();
           elementDTO.setId(nextElement.getId());
           elementDTO.setText(nextElement.getText());
           children.add(elementDTO);

           if(elementIterator.hasNext())
               nextElement = elementIterator.next();
           else
               nextElement = null;
       }

       sectionDTO.setChildren(children);

       return sectionDTO;
    }
}
