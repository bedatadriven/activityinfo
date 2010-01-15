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
import org.activityinfo.server.auth.SecureTokenGenerator;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.RenderResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.RenderElement
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

        // Assure that the temp folder exists
        try {
            File tempFolder = new File(tempPath);
            tempFolder.mkdirs();
        } catch (SecurityException e) {
            throw new RuntimeException("Could not create the temporary folder (your_context\temp). You may need to change " +
                    "some file permissions.");
        }
    }

    public CommandResult execute(RenderElement cmd, User user) throws CommandException {

        // generate the content

        generator.execute(new GenerateElement(cmd.getElement()), user);

        // create the renderer
        Renderer renderer = rendererFactory.get(cmd.getFormat());

        // compose temporary file name
        String filename = SecureTokenGenerator.generate() + renderer.getFileSuffix();
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
