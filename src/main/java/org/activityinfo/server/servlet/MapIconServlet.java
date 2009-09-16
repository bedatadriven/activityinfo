package org.activityinfo.server.servlet;

import com.google.inject.Singleton;
import com.google.inject.Inject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.*;

import org.activityinfo.server.report.renderer.image.ImageMapRenderer;

@Singleton
public class MapIconServlet extends HttpServlet {


    private ImageMapRenderer renderer;

    @Inject
    public MapIconServlet(ImageMapRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if("bubble".equals(req.getParameter("t"))) {

            int radius = Integer.parseInt(req.getParameter("r"));
            int color;

            try {
                color = Integer.parseInt(req.getParameter("c"));
            } catch(NumberFormatException e) {
                color = Color.decode(req.getParameter("c")).getRGB();
            }


            BufferedImage icon = new BufferedImage(radius*2, radius*2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = icon.createGraphics();
        
            g2d.setPaint(new Color(255,255,255,0));
            g2d.fillRect(0,0,radius*2,radius*2);

            renderer.drawBubble(g2d, color, radius, radius, radius);

            resp.setContentType("image/png");
            ImageIO.write(icon, "PNG", resp.getOutputStream());
        }
    }
}
