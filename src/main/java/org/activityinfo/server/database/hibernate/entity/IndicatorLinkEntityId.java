package org.activityinfo.server.database.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IndicatorLinkEntityId implements Serializable {
    private static final long serialVersionUID = 6224197691532121470L;
    
    private Integer sourceIndicatorId;
    private Integer destinationIndicatorId;

    public IndicatorLinkEntityId() {
    }

    public IndicatorLinkEntityId(Integer sourceIndicatorId, Integer destinationIndicatorId) {
        this.sourceIndicatorId = sourceIndicatorId;
        this.destinationIndicatorId = destinationIndicatorId;
    }

    @Column(name = "SourceIndicatorId")
    public Integer getSourceIndicatorId() {
        return sourceIndicatorId;
    }

    public void setSourceIndicatorId(Integer sourceIndicatorId) {
        this.sourceIndicatorId = sourceIndicatorId;
    }

    @Column(name = "DestinationIndicatorId")
    public Integer getDestinationIndicatorId() {
        return destinationIndicatorId;
    }

    public void setDestinationIndicatorId(Integer destinationIndicatorId) {
        this.destinationIndicatorId = destinationIndicatorId;
    }
}