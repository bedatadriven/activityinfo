/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap.model;

public abstract class PageModel {

    private static final String SUFFIX = "PageModel";

    public static <T extends PageModel> String getTemplateName(Class<T> pageModelClass) {
        String className = pageModelClass.getSimpleName();
        assert className.endsWith(SUFFIX) : "Page Model classes should end in '" + SUFFIX + "'";

        return "page/" + className.substring(0, className.length() - SUFFIX.length()) + ".ftl";
    }

    public String getTemplateName() {
        return getTemplateName(getClass());
    }
}
