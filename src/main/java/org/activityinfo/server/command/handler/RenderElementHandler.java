package org.activityinfo.server.command.handler;

import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.util.EntropicToken;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.excel.ExcelReportRenderer;
import org.activityinfo.server.report.renderer.excel.ExcelMapDataExporter;
import org.activityinfo.server.report.renderer.image.ImageReportRenderer;
import org.activityinfo.server.report.renderer.ppt.PPTRenderer;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.RenderResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.activityinfo.shared.report.model.MapElement;

import javax.servlet.ServletContext;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Injector;
/*
 * @author Alex Bertram
 */

public class RenderElementHandler implements CommandHandler<RenderElement> {

    private final Injector injector;
    private final GenerateElementHandler generator;
    private final String tempPath;



    @Inject
    public RenderElementHandler(Injector injector, ServletContext context, GenerateElementHandler generator) {

        this.injector = injector;
        this.tempPath = context.getRealPath("/temp");
        this.generator = generator;

    }

    public CommandResult execute(RenderElement cmd, User user) throws CommandException {

        String extension;
        Renderer renderer;

        if(cmd.getFormat() == RenderElement.Format.PNG) {
            extension = ".png";
            renderer = injector.getInstance(ImageReportRenderer.class);

        } else if(cmd.getFormat() == RenderElement.Format.Excel) {
            extension = ".xls";
            renderer = injector.getInstance(ExcelReportRenderer.class);

        } else if(cmd.getFormat() == RenderElement.Format.Excel_Data &&
                cmd.getElement() instanceof MapElement) {

            extension = ".xls";
            renderer = injector.getInstance(ExcelMapDataExporter.class);


        } else if(cmd.getFormat() == RenderElement.Format.PowerPoint) {
            extension = ".ppt";
            renderer = injector.getInstance(PPTRenderer.class);

        } else {
            throw new UnexpectedCommandException();
        }

        generator.execute(new GenerateElement(cmd.getElement()), user);

        // compose temporary file name

        String filename = EntropicToken.generate() + extension;

		String path = tempPath + "/" + filename;

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
