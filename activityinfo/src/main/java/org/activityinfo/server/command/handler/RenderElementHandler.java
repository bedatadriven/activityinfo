/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.command.handler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.util.EntropicToken;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.activityinfo.server.report.renderer.excel.ExcelMapDataExporter;
import org.activityinfo.server.report.renderer.excel.ExcelReportRenderer;
import org.activityinfo.server.report.renderer.image.ImageReportRenderer;
import org.activityinfo.server.report.renderer.ppt.PPTRenderer;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.RenderResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.activityinfo.shared.report.model.MapElement;

import javax.servlet.ServletContext;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @see org.activityinfo.shared.command.RenderElement
 *
 * @author Alex Bertram
 */
public class RenderElementHandler implements CommandHandler<RenderElement> {

    private final RendererFactory rendererFactory;
    private final GenerateElementHandler generator;
    private final String tempPath;


    @Inject
    public RenderElementHandler(RendererFactory rendererFactory, ServletContext context, GenerateElementHandler generator) {

        this.rendererFactory = rendererFactory;
        this.tempPath = context.getRealPath("/temp");
        this.generator = generator;
    }

    public CommandResult execute(RenderElement cmd, User user) throws CommandException {

        // generate the content

        generator.execute(new GenerateElement(cmd.getElement()), user);

        // create the renderer
        Renderer renderer = rendererFactory.get(cmd.getFormat());

        // compose temporary file name
        String filename = EntropicToken.generate() + renderer.getFileSuffix();
		String path = tempPath + "/" + filename;

        // render to a temporary file
        try {
            FileOutputStream os = new FileOutputStream(path);

            renderer.render(cmd.getElement(), os);
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new UnexpectedCommandException();
        }

        return new RenderResult(filename);
    }
}
