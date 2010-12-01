package org.sigmah.shared.domain.reminder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "monitored_point_list")
public class MonitoredPointList implements Serializable {

    private static final long serialVersionUID = 3500189016079098270L;

    private Integer id;
    private List<MonitoredPoint> points = new ArrayList<MonitoredPoint>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_monitored_point_list")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "parentList", cascade = CascadeType.ALL)
    public List<MonitoredPoint> getPoints() {
        return points;
    }

    public void setPoints(List<MonitoredPoint> points) {
        this.points = points;
    }

    public void addMonitoredPoint(MonitoredPoint point) {
        if (point == null) {
            return;
        }
        point.setParentList(this);
        points.add(point);
    }
}
