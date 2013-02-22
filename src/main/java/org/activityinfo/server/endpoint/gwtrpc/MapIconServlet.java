package org.activityinfo.server.endpoint.gwtrpc;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.server.util.ColorUtil;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.content.PieMapMarker.SliceValue;

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.image.BufferedImage;
import com.google.code.appengine.imageio.ImageIO;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Servlet that generates map icons for use in a Google Maps
 * {@link com.google.gwt.maps.client.overlay.Icon}
 * <p/>
 * The query parameter "t" indicates the type of icon to be generated:
 * <p/>
 * <b>t=bubble</b>: Generates a semi-transparent circle
 * 
 * TODO: security checks. Manual parsing of arguments just screams for some sec
 * flaws.
 * 
 * @author Alex Bertram
 * @param r
 *            Radius, in pixels, of the bubble
 * @param c
 *            Color, as an RGB integer, of the bubble
 */
@Singleton
public class MapIconServlet extends HttpServlet {

    private ImageMapRenderer renderer;

    @Inject
    public MapIconServlet(ImageMapRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        // Cache forever
        resp.setHeader("Cache-Control", "max-age=31556926, public");

        if (req.getParameter("t").equals("bubble")) {

            int radius = Integer.parseInt(req.getParameter("r"));
            Color color = ColorUtil.colorFromString(req.getParameter("c"));

            BufferedImage icon = new BufferedImage(radius * 2, radius * 2,
                BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = icon.createGraphics();

            g2d.setPaint(new Color(255, 255, 255, 0));
            g2d.fillRect(0, 0, radius * 2, radius * 2);

            renderer.drawBubble(g2d, color, radius, radius, radius);

            resp.setContentType("image/png");
            ImageIO.write(icon, "PNG", resp.getOutputStream());
        } else {
            if (req.getParameter("t").equals("piechart")) {

                int radius = Integer.parseInt(req.getParameter("r"));

                BufferedImage icon = new BufferedImage(radius * 2, radius * 2,
                    BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = icon.createGraphics();

                PieMapMarker pmm = new PieMapMarker();
                pmm.setRadius(radius);
                pmm.setX(radius);
                pmm.setY(radius);

                String[] values = req.getParameterValues("value");
                String[] colors = req.getParameterValues("color");

                if (colors.length != values.length) {
                    String error = "Expected same amount of colors & values. Amount of Colors: [{0}]. Amount of values: [{1}].";
                    error = String.format(error, colors.length, values.length);
                    throw new ServletException(error);
                }

                for (int i = 0; i < colors.length; i++) {
                    String color;
                    double value = 0.0;

                    color = colors[i];
                    try {
                        value = Double.parseDouble(values[i]);
                    } catch (NumberFormatException e) {
                        // color = Color.decode(colors[i]).getRGB();
                    }
                    SliceValue slice = new SliceValue();
                    slice.setColor(color);
                    slice.setValue(value);
                    pmm.getSlices().add(slice);
                }

                g2d.setPaint(new Color(255, 255, 255, 0));
                g2d.fillRect(0, 0, radius * 2, radius * 2);

                renderer.drawPieMarker(g2d, pmm);

                resp.setContentType("image/png");
                ImageIO.write(icon, "PNG", resp.getOutputStream());
            }
        }
    }
}