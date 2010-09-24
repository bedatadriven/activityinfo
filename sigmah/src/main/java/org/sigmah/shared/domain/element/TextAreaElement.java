package org.sigmah.shared.domain.element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Text area element entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "textarea_element")
public class TextAreaElement extends FlexibleElement {

    private static final long serialVersionUID = 1147116003259320146L;

    /**
     * The type of the value expected in this text field. This character defines
     * this type.<br/>
     * See the other attributes which define the expected format for each type
     * of value.
     * <ul>
     * <li><strong>T</strong> ; &quot;Text&quot;: a short text.</li>
     * <li><strong>P</strong> ; &quot;Paragraph&quot;: a long text.</li>
     * <li><strong>D</strong> ; &quot;Date&quot;: a date.</li>
     * <li><strong>N</strong> ; &quot;Number&quot;: a number.</li>
     * </ul>
     */
    private Character type = 'P';

    /**
     * If the type of the value is Number or Date, this attribute defines the
     * min value allowed (stored as a timestamp for a date).<br/>
     * Could be <code>null</code> to avoid this constraint.
     */
    private Long minValue;

    /**
     * If the type of the value is Number or Date, this attribute defines the
     * max value allowed (stored as a timestamp for a date).<br/>
     * Could be <code>null</code> to avoid this constraint.
     */
    private Long maxValue;

    /**
     * If the type of the value is Number, this attribute defines if the number
     * can be a decimal value.
     */
    private Boolean isDecimal;

    /**
     * If the type of the value is Text or Paragraph, this attribute defines the
     * max length allowed for the text.<br/>
     * Could be <code>null</code> to avoid this constraint.
     */
    private Integer length;

    @Column(name = "type", nullable = true)
    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    @Column(name = "min_value", nullable = true)
    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    @Column(name = "max_value", nullable = true)
    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    @Column(name = "is_decimal", nullable = true)
    public Boolean getIsDecimal() {
        return isDecimal;
    }

    public void setIsDecimal(Boolean isDecimal) {
        this.isDecimal = isDecimal;
    }

    @Column(name = "length", nullable = true)
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
