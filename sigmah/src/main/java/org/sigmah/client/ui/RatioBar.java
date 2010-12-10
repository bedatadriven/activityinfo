package org.sigmah.client.ui;

import org.sigmah.client.util.NumberUtils;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

public class RatioBar extends Widget {

    private final DivElement innerDiv;
    private final DivElement outterDiv;

    public RatioBar(final double ratio) {

        innerDiv = Document.get().createDivElement();
        final int r = updateRatioStyle(ratio);

        outterDiv = Document.get().createDivElement();
        outterDiv.addClassName("blockStat");
        outterDiv.appendChild(innerDiv);

        setElement(outterDiv);
        setTitle(r + "%");
    }

    private int updateRatioStyle(double ratio) {

        // Adjusts the ration.
        ratio = NumberUtils.adjustRatio(ratio);

        // Computes the style name.
        final String className;
        if (ratio > 60 && ratio < 80) {
            className = "blockStatBgYellow";
        } else if (ratio >= 80 && ratio < 100) {
            className = "blockStatBgOrange";
        } else if (ratio >= 100) {
            className = "blockStatBgRed";
        } else {
            className = "blockStatBgBlack";
        }

        final int ratioAsInt = new Double(ratio).intValue();

        // Styles.
        innerDiv.setClassName(className);
        innerDiv.getStyle().setProperty("width", (ratioAsInt > 100 ? 100 : ratioAsInt) + "%");

        if (ratio >= 100) {
            innerDiv.setInnerText(ratioAsInt + "%");
        } else {
            innerDiv.setInnerText("");
        }

        return ratioAsInt;
    }
}
