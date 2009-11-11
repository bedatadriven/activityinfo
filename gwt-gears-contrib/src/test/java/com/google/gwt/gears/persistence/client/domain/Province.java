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

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Alex Bertram
 */
@Entity
public class Province {

  private String code;
  private String name;
  private Bounds bounds;
  private Integer population;

  public Province() {
  }

  public Province(String code, String name, Bounds bounds, Integer population) {
    this.code = code;
    this.name = name;
    this.bounds = bounds;
    this.population = population;
  }

  @Id
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Embedded
  public Bounds getBounds() {
    return bounds;
  }

  public void setBounds(Bounds bounds) {
    this.bounds = bounds;
  }

  public Integer getPopulation() {
    return population;
  }

  public void setPopulation(Integer population) {
    this.population = population;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Province province = (Province) o;

    if (bounds != null ? !bounds.equals(province.bounds) : province.bounds != null) return false;
    if (!code.equals(province.code)) return false;
    if (!name.equals(province.name)) return false;
    if (population != null ? !population.equals(province.population) : province.population != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = code.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + (bounds != null ? bounds.hashCode() : 0);
    result = 31 * result + (population != null ? population.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Province{" +
        "code='" + code + '\'' +
        ", name='" + name + '\'' +
        ", bounds=" + bounds +
        ", population=" + population +
        '}';
  }
}
