package org.activityinfo.shared.report.model.layers;

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

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlTransient;

import org.activityinfo.shared.report.model.clustering.AdministrativeLevelClustering;
import org.activityinfo.shared.report.model.clustering.AutomaticClustering;
import org.activityinfo.shared.report.model.clustering.Clustering;
import org.activityinfo.shared.report.model.clustering.NoClustering;
import org.activityinfo.shared.report.model.labeling.ArabicNumberSequence;
import org.activityinfo.shared.report.model.labeling.LabelSequence;
import org.activityinfo.shared.report.model.labeling.LatinAlphaSequence;

public abstract class PointMapLayer extends AbstractMapLayer {

    private LabelSequence labelSequence;
    private Clustering clustering = new NoClustering();

    @XmlElementRefs({ @XmlElementRef(type = ArabicNumberSequence.class),
        @XmlElementRef(type = LatinAlphaSequence.class) })
    public LabelSequence getLabelSequence() {
        return labelSequence;
    }

    public void setLabelSequence(LabelSequence labelSequence) {
        this.labelSequence = labelSequence;
    }

    @XmlTransient
    public boolean isClustered() {
        return clustering.isClustered();
    }

    @XmlElementRefs({
        @XmlElementRef(type = AdministrativeLevelClustering.class),
        @XmlElementRef(type = NoClustering.class),
        @XmlElementRef(type = AutomaticClustering.class) })
    public Clustering getClustering() {
        return clustering;
    }

    public void setClustering(Clustering clustering) {
        this.clustering = clustering;
    }

}
