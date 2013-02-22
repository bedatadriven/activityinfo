package org.activityinfo.client.page.common.nav;

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

import org.activityinfo.client.page.PageState;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * A TreeModel that a represents a link to another PageId+PageState
 * 
 * @author Alex Bertram
 */
public final class Link extends BaseTreeModel {
    private PageState pageState;
    private AbstractImagePrototype icon;
    private String key;

    private Link() {
    }

    /**
     * 
     * @return this Link's label
     */
    public String getLabel() {
        return get("name");
    }

    /**
     * 
     * @return the PageState to which to link
     */
    public PageState getPageState() {
        return pageState;
    }

    /**
     * @return this Link's icon
     */
    public AbstractImagePrototype getIcon() {
        return icon;
    }

    /**
     * 
     * @return this Link's key, for use in state management
     */
    public String getKey() {
        return key;
    }

    /**
     * Creates a new link to a given PageState
     */
    public static Builder to(PageState pageState) {
        return new Builder().to(pageState);
    }

    /**
     * Creates a new Link folder with the given name
     */
    public static Builder folderLabelled(String label) {
        return new Builder().labeled(label);

    }

    /**
     * Builder class for Links
     */
    public static final class Builder {
        private Link link;

        private Builder() {
            link = new Link();
        }

        /**
         * Sets the destination to the given PageState
         */
        private Builder to(PageState pageState) {
            link.pageState = pageState;
            link.key = pageState.getPageId() + "/"
                + pageState.serializeAsHistoryToken();
            return this;
        }

        /**
         * Sets the display label to the given string
         */
        public Builder labeled(String label) {
            link.set("name", label);
            return this;
        }

        /**
         * Sets the icon to the given AbstractImagePrototype
         */
        public Builder withIcon(AbstractImagePrototype icon) {
            link.icon = icon;
            return this;
        }

        /**
         * Sets the key using a composite of the parent and this label's name
         */
        public Builder usingKey(String key) {
            this.link.key = key;
            return this;
        }

        /**
         * @return the Link
         */
        public Link build() {
            assert link.getLabel() != null : "Link models must have a name/label";
            assert link.getKey() != null : "Link models must have a key";
            return link;
        }
    }
}
