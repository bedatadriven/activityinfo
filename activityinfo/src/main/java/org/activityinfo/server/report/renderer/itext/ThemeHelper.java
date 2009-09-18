package org.activityinfo.server.report.renderer.itext;

import com.lowagie.text.Paragraph;
import com.lowagie.text.Font;
import com.lowagie.text.Cell;
import com.lowagie.text.BadElementException;

import java.awt.*;
/*
 * @author Alex Bertram
 */

public class ThemeHelper {


    public static Paragraph reportTitle(String title) {
        Paragraph para = new Paragraph(title);
        para.setFont(new Font(Font.TIMES_ROMAN, 26, Font.NORMAL, new Color(23, 54, 93)));
        para.setSpacingAfter(15);

        return para;
    }

    public static Paragraph elementTitle(String title) {
        Paragraph para = new Paragraph(title);
        para.setFont(new Font(Font.TIMES_ROMAN, 13, Font.BOLD, new Color(79, 129, 189)));
        para.setSpacingBefore(10);
        return para;
    }


    public static Cell columnHeaderCell(String label, boolean leaf) throws BadElementException {
        Paragraph para = new Paragraph(label);
        para.setFont(new Font(Font.HELVETICA, 10, Font.NORMAL, Color.WHITE));

        Cell cell = new Cell();
        cell.addElement(para);
        cell.setHorizontalAlignment(leaf ? Cell.ALIGN_RIGHT : Cell.ALIGN_CENTER);
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

    public static Cell bodyCell(String label, boolean header, int depth, boolean leaf) throws BadElementException {

        Cell cell = new Cell();
        cell.setHorizontalAlignment(header ? Cell.ALIGN_LEFT : Cell.ALIGN_RIGHT);

        if(label != null) {
            Paragraph para = new Paragraph(label);
            Font font = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
            if(depth == 0 && !leaf)
                font.setColor(Color.WHITE);
            para.setFont(font);
            para.setIndentationLeft( 5.4f + (header ? 12 * depth : 0));
            cell.addElement(para);
        }

        cell.setBorderWidthLeft(0f);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);

        if(!leaf &&depth == 0) {
            cell.setBackgroundColor(new Color(149, 179, 215));
            cell.setBorderWidthBottom(0.5f);
            cell.setBorderColorBottom(new Color(219, 229, 241));
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
