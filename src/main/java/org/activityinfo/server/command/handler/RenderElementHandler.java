/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.output.ImageStorage;
import org.activityinfo.server.report.output.ImageStorageProvider;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.UrlResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DateRange;

import com.google.common.base.Strings;
import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.RenderElement
 */
public class RenderElementHandler implements CommandHandler<RenderElement> {

    private final RendererFactory rendererFactory;
    private final ReportGenerator generator;
	private ImageStorageProvider storageProvider;


    @Inject
    public RenderElementHandler(RendererFactory rendererFactory, ReportGenerator generator, ImageStorageProvider storageProvider) {
        this.rendererFactory = rendererFactory;
        this.generator = generator;
        this.storageProvider = storageProvider;
    }

    public CommandResult execute(RenderElement cmd, User user) throws CommandException {
		try {
			Renderer renderer = rendererFactory.get(cmd.getFormat());
			ImageStorage storage = storageProvider.getImageUrl(renderer.getFileSuffix());
			try {
		        generator.generateElement(user, cmd.getElement(), new Filter(), new DateRange());
				renderer.render(cmd.getElement(), storage.getOutputStream());      
			} finally {
		        storage.getOutputStream().close();
			}
	
				
	        return new UrlResult(storage.getUrl());
		} catch(Exception e) {
			throw new RuntimeException("Exception generating export", e);
		}
    }

	private String composeFilename(RenderElement cmd, Renderer renderer) {
		if(Strings.isNullOrEmpty(cmd.getElement().getTitle())) {
			return "Report" + renderer.getFileSuffix();
		} else {
			return cmd.getElement().getTitle() +  renderer.getFileSuffix();
		}
	}
}
