/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.itext;

import com.google.code.appengine.awt.Color;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

/**
 * Centralizes all style-specific code. At the present, there is a single theme hardcoded
 * here based on the Office 2007 color and font scheme, but this could be refactored in the future
 * to allow
 *
 * @author Alex Bertram
 */
public final class ThemeHelper {

	private static final float LEFT_INDENT = 5.4f;
	
	private static final int TITLE_FONT_SIZE = 26;
	private static final int HEADER2_FONT_SIZE = 13;
	private static final int HEADER3_FONT_SIZE = 12;
	private static final int BODY_FONT_SIZE = 10;
	
	private static final float THIN_BORDER_WIDTH = 0.5f;
		
	private static final Color BLUE = new Color(23, 54, 93);
	private static final Color BLUE2 = new Color(79, 129, 189);
	private static final Color BLUE3 = new Color(55, 96, 145);
	private static final Color BLUE4 = new Color(149, 179, 215);
	private static final Color BLUE5 = new Color(219, 229, 241);

	private ThemeHelper() {}

    public static Paragraph reportTitle(String title) {
        Paragraph para = new Paragraph(title);
        para.setFont(new Font(Font.TIMES_ROMAN, TITLE_FONT_SIZE, Font.NORMAL, BLUE));
        para.setSpacingAfter(15);

        return para;
    }

    public static Paragraph filterDescription(String text) {
        Paragraph para = new Paragraph(text);
        para.setFont(new Font(Font.HELVETICA, HEADER3_FONT_SIZE, Font.NORMAL, Color.BLACK));
        return para;
    }

    public static Paragraph legendText(String text) {
    	Paragraph para = new Paragraph();
    	para.setFont(bodyFont());
    	return para;
    }

    public static Paragraph elementTitle(String title) {
        Paragraph para = new Paragraph(title);
        para.setFont(new Font(Font.TIMES_ROMAN, HEADER2_FONT_SIZE, Font.BOLD, BLUE2));
        para.setSpacingBefore(BODY_FONT_SIZE);
        return para;
    }
    
    public static Paragraph legendTitle(String title) {
        Paragraph para = new Paragraph(title);
        para.setFont(new Font(Font.TIMES_ROMAN, BODY_FONT_SIZE, Font.BOLD, BLUE2));
        para.setSpacingBefore(BODY_FONT_SIZE);
        return para;
    }
    
    
    public static Font footerFont() {
    	return new Font(Font.TIMES_ROMAN, BODY_FONT_SIZE, Font.BOLD, Color.BLACK); 
    }

    public static Cell columnHeaderCell(String label, boolean leaf) throws BadElementException {
        return columnHeaderCell(label, leaf, leaf ? Cell.ALIGN_RIGHT : Cell.ALIGN_CENTER);
    }

    public static Cell columnHeaderCell(String label, boolean leaf, int hAlign) throws BadElementException {
        Paragraph para = new Paragraph(label);
        para.setFont(new Font(Font.HELVETICA, BODY_FONT_SIZE, Font.NORMAL, Color.WHITE));

        Cell cell = new Cell();
        cell.addElement(para);
        cell.setHorizontalAlignment(hAlign);
        cell.setHeader(true);
        cell.setVerticalAlignment(Cell.ALIGN_BOTTOM);
        cell.setBackgroundColor(BLUE3);

        cell.setBorderWidth(0);

        return cell;
    }
    
    public static Cell cornerCell() {
        Cell cell = new Cell();
        cell.setHeader(true);
        cell.setBorderWidth(0);
        cell.setBackgroundColor(BLUE3);
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
            Font font = bodyFont();
            if(depth == 0 && !leaf) {
                font.setColor(Color.WHITE);
            }
            para.setFont(font);
            para.setIndentationLeft( LEFT_INDENT + (header ? HEADER3_FONT_SIZE * depth : 0));
            cell.addElement(para);
        }

        cell.setBorderWidthLeft(0f);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);

        if(!leaf &&depth == 0) {
            cell.setBackgroundColor(BLUE4); // #95B3D7
            cell.setBorderWidthBottom(THIN_BORDER_WIDTH);
            cell.setBorderColorBottom(BLUE5); // #DBE5F1
        } else if(!leaf && depth == 1) {
            cell.setBackgroundColor(BLUE5);
            cell.setBorderWidthBottom(THIN_BORDER_WIDTH);
            cell.setBorderColorBottom(BLUE2);
        } else {
            cell.setBorderWidthBottom(THIN_BORDER_WIDTH);
            cell.setBorderColorBottom(BLUE5);
            cell.setBorderWidthTop(THIN_BORDER_WIDTH);
            cell.setBorderColorTop(BLUE5);
        }

        return cell;
    }

	private static Font bodyFont() {
		return new Font(Font.HELVETICA, BODY_FONT_SIZE, Font.NORMAL, Color.BLACK);
	}
}
