/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package com.google.gwt.gears.persistence.client.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Alex Bertram
 */
@Entity
public class Event {

  private int id;
  private Integer attendedCount;
  private Date startTime;
  private Date endTime;

  @Id
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Column(nullable = true)
  public Integer getAttendedCount() {
    return attendedCount;
  }

  public void setAttendedCount(Integer attendedCount) {
    this.attendedCount = attendedCount;
  }

  @Column(nullable = true)
  @Temporal(TemporalType.TIMESTAMP)
  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  @Column(nullable = true)
  @Temporal(TemporalType.DATE)
  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
}
