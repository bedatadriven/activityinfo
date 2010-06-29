package org.sigmah.client.page.common;

import org.sigmah.client.page.PageState;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface GalleryView {

    public void setHeading(String html);

    public void setIntro(String html);

    public void add(String name, String desc, String path, PageState place);


}
