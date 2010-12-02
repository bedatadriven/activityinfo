/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.report;

import java.io.Serializable;

/**
 * Represents a key question associated with a project report section.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class KeyQuestionDTO implements Serializable {
    private final static long serialVersionUID = 1L;

    private Integer id;
    private String label;
    private RichTextElementDTO richTextElementDTO;
    
    private int number;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public RichTextElementDTO getRichTextElementDTO() {
        return richTextElementDTO;
    }

    public void setRichTextElementDTO(RichTextElementDTO richTextElementDTO) {
        this.richTextElementDTO = richTextElementDTO;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
