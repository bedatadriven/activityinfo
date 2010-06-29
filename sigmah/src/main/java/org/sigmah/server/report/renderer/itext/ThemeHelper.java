/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

import java.awt.*;

/**
 * Centralizes all style-specific code. At the present, there is a single theme hardcoded
 * here based on the Office 2007 color and font scheme, but this could be refactored in the future
 * to allow
 *
 * @author Alex Bertram
 */
public class ThemeHelper {


    public static Paragraph reportTitle(String title) {
        Paragraph para = new Paragraph(title);
        para.setFont(new Font(Font.TIMES_ROMAN, 26, Font.NORMAL, new Color(23, 54, 93)));
        para.setSpacingAfter(15);

        return para;
    }

    public static Paragraph filterDescription(String text) {
        Paragraph para = new Paragraph(text);
        para.setFont(new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(0, 0, 0)));
        return para;
    }


    public static Paragraph elementTitle(String title) {
        Paragraph para = new Paragraph(title);
        para.setFont(new Font(Font.TIMES_ROMAN, 13, Font.BOLD, new Color(79, 129, 189)));
        para.setSpacingBefore(10);
        return para;
    }

    public static Cell columnHeaderCell(String label, boolean leaf) throws BadElementException {
        return columnHeaderCell(label, leaf, leaf ? Cell.ALIGN_RIGHT : Cell.ALIGN_CENTER);
    }

    public static Cell columnHeaderCell(String label, boolean leaf, int hAlign) throws BadElementException {
        Paragraph para = new Paragraph(label);
        para.setFont(new Font(Font.HELVETICA, 10, Font.NORMAL, Color.WHITE));

        Cell cell = new Cell();
        cell.addElement(para);
        cell.setHorizontalAlignment(hAlign);
        cell.setHeader(true);
        cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
        cell.setBackgroundColor(new Color(55, 96, 145));

        cell.setBorderWidth(0);

        return cell;
    }

    public static Cell cornerCell() {
        Cell cell = new Cell();
        cell.setHeader(true);
        cell.setBorderWidth(0);
        cell.setBackgroundColor(new Color(55, 96, 145));
        return cell;
    }

    public static Cell bodyCell(String label, boolean header, int depth, boolean leaf )
            throws BadElementException {

        return bodyCell(label, header, depth, leaf, header ? Cell.ALIGN_LEFT : Cell.ALIGN_RIGHT);
    }

    /**
     * Renders a Cell for
     *
     * @param label
     * @param header
     * @param depth
     * @param leaf
     * @param horizantalAlignment
     * @return
     * @throws BadElementException
     */
    public static Cell bodyCell(String label, boolean header, int depth, boolean leaf, int horizantalAlignment )
            throws BadElementException {

        Cell cell = new Cell();
        cell.setHorizontalAlignment(horizantalAlignment);

        if(label != null) {
            Paragraph para = new Paragraph(label);
            Font font = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
            if(depth == 0 && !leaf) {
                font.setColor(Color.WHITE);
            }
            para.setFont(font);
            para.setIndentationLeft( 5.4f + (header ? 12 * depth : 0));
            cell.addElement(para);
        }

        cell.setBorderWidthLeft(0f);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);

        if(!leaf &&depth == 0) {
            cell.setBackgroundColor(new Color(149, 179, 215)); // #95B3D7
            cell.setBorderWidthBottom(0.5f);
            cell.setBorderColorBottom(new Color(219, 229, 241)); // #DBE5F1
        } else if(!leaf && depth == 1) {
            cell.setBackgroundColor(new Color(219, 229, 241));
            cell.setBorderWidthBottom(0.5f);
            cell.setBorderColorBottom(new Color(79, 129, 189));
        } else {
            cell.setBorderWidthBottom(0.5f);
            cell.setBorderColorBottom(new Color(219, 229, 241));
            cell.setBorderWidthTop(0.5f);
            cell.setBorderColorTop(new Color(219, 229, 241));
        }

        return cell;
    }
}
