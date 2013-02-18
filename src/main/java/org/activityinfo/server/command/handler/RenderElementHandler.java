/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.output.StorageProvider;
import org.activityinfo.server.report.output.TempStorage;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.UrlResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DateRange;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.RenderElement
 */
public class RenderElementHandler implements CommandHandler<RenderElement> {

	private static final Logger LOGGER = Logger.getLogger(RenderElementHandler.class.getName());
	
    private final RendererFactory rendererFactory;
    private final ReportGenerator generator;
	private StorageProvider storageProvider;


    @Inject
    public RenderElementHandler(RendererFactory rendererFactory, ReportGenerator generator, StorageProvider storageProvider) {
        this.rendererFactory = rendererFactory;
        this.generator = generator;
        this.storageProvider = storageProvider;
    }

    public CommandResult execute(RenderElement cmd, User user) throws CommandException {
    	   	
		try {
			Renderer renderer = rendererFactory.get(cmd.getFormat());
			TempStorage storage = storageProvider.allocateTemporaryFile(
					renderer.getMimeType(), 
					cmd.getFilename() + renderer.getFileSuffix());
			
	    	LOGGER.fine("Rendering element: " + cmd + "\nURL: " + storage.getUrl());

			try {
		        generator.generateElement(user, cmd.getElement(), new Filter(), new DateRange());
				renderer.render(cmd.getElement(), storage.getOutputStream());      
			} finally {
				try {
					storage.getOutputStream().close();
				} catch(Exception e) {
					LOGGER.log(Level.WARNING, "Exception while closing storage: " + e.getMessage(), e);
				}
			}
				
	        return new UrlResult(storage.getUrl());
		} catch(Exception e) {
			throw new RuntimeException("Exception generating export", e);
		}
    }

}
