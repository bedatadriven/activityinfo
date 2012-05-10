/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.activityinfo.server.authentication.SecureTokenGenerator;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.RenderResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.Report;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.RenderElement
 */
public class RenderElementHandler implements CommandHandler<RenderElement> {

    private final RendererFactory rendererFactory;
    private final String tempPath;
    private final ReportGenerator generator;


    @Inject
    public RenderElementHandler(RendererFactory rendererFactory, ServletContext context, ReportGenerator generator) {

        this.rendererFactory = rendererFactory;
        this.tempPath = context.getRealPath("/temp");
        this.generator = generator;

        // Assure that the temp folder exists
        try {
            File tempFolder = new File(tempPath);
            tempFolder.mkdirs();
        } catch (SecurityException e) {
            throw new RuntimeException("Could not create the temporary folder (your_context\temp). You may need to change " +
                    "some file permissions.", e);
        }
    }

    public CommandResult execute(RenderElement cmd, User user) throws CommandException {

		Renderer renderer = rendererFactory.get(cmd.getFormat());

            // compose temporary file name
        String filename = SecureTokenGenerator.generate() + renderer.getFileSuffix();
        String path = tempPath + "/" + filename;

        // render to a temporary file
        try {
            FileOutputStream os = new FileOutputStream(path);

            generator.generateElement(user, cmd.getElement(), new Filter(), new DateRange());
    		renderer.render(cmd.getElement(), os);      
            
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new UnexpectedCommandException();
        }

        return new RenderResult(filename);
    }

	private void renderIndividualElement(RenderElement cmd, User user,
			FileOutputStream os) throws IOException {

	}
}
