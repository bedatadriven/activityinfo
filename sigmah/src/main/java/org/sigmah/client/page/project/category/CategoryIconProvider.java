package org.sigmah.client.page.project.category;

import org.sigmah.shared.dto.category.CategoryElementDTO;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

/**
 * Provides a icon for each category element.
 * 
 * @author tmi
 * 
 */
public final class CategoryIconProvider {

    private CategoryIconProvider() {
    }

    /**
     * Build an image for the given category element.
     * 
     * @param element
     *            The category element.
     * @return The image.
     */
    public static Image getIcon(CategoryElementDTO element) {
        return getIcon(element, true);
    }

    /**
     * Build an image for the given category element.
     * 
     * @param element
     *            The category element.
     * @param tooltip
     *            If a tooltip text must be added.
     * @return The image.
     */
    public static Image getIcon(CategoryElementDTO element, boolean tooltip) {

        if (element == null || element.getParentCategoryDTO() == null
                || element.getParentCategoryDTO().getIcon() == null || element.getColor() == null) {
            return null;
        }

        final AbstractImagePrototype prototype;

        switch (element.getParentCategoryDTO().getIcon()) {
        case CIRCLE:
            prototype = CategoryImageBundle.ICONS.circle();
            break;
        case SQUARE:
            prototype = CategoryImageBundle.ICONS.square();
            break;
        case TRIANGLE:
            prototype = CategoryImageBundle.ICONS.triangle();
            break;
        case CROSS:
            prototype = CategoryImageBundle.ICONS.cross();
            break;
        case DIAMOND:
            prototype = CategoryImageBundle.ICONS.diamond();
            break;
        case STAR:
            prototype = CategoryImageBundle.ICONS.star();
            break;
        default:
            return null;
        }

        final Image img = prototype.createImage();
        DOM.setStyleAttribute(img.getElement(), "backgroundColor", "#" + element.getColor());

        if (tooltip) {
            img.setTitle(element.getParentCategoryDTO().getLabel() + " (" + element.getLabel() + ')');
        }

        return img;
    }

    /**
     * Build an image for the given category element and return its HTML code.
     * 
     * @param element
     *            The category element.
     * @param tooltip
     *            If a tooltip text must be added.
     * @return The image HTML code.
     */
    public static String getIconHtml(CategoryElementDTO element, boolean tooltip) {
        final Image img = getIcon(element, tooltip);
        return img == null ? "" : img.getElement().getString();
    }

    public static native String getComboboxIconTemplate()
    /*-{ 
    return  [ 
    '<tpl for=".">', 
    '<table cellspacing="0" cellpadding="0" width="100%" class="x-combo-list-item">\
    <tr>\
        <td width="16px">{[values.categoryElementDTO.iconHtml]}\</td>\
        <td style="padding-left: 5px;">{[values.label]}</td>\
    </tr>\
    </table>', 
    '</tpl>' 
    ].join(""); 
    }-*/;

}
