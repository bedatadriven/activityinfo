package org.sigmah.shared.report.model.typeadapter;

@Deprecated
public enum NumberingType {

    /**
     * Symbols will not be numbered
     */
    None,

    /**
     * Bubbles will be labeled 1, 2, 3 ...
     */
    ArabicNumerals,

    /**
     * Bubbles will be labed A...Z, AA..AZ, etc
     */
    LatinAlphabet
}