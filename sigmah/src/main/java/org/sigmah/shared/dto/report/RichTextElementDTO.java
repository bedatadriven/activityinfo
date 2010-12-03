/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.report;

/**
 * Editable html field of a project report.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class RichTextElementDTO implements ProjectReportContent {
    public final static long serialVersionUID = 1L;
    
    private Integer id;
    private String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
