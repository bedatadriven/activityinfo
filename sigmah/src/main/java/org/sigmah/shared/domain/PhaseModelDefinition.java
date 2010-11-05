package org.sigmah.shared.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Defines a global definition/concept for all phase models. This concept
 * permits to aggregate phases which belongs to the same definition.
 * 
 * @author tmi
 * 
 */
@Entity
@Table(name = "phase_model_definition")
public class PhaseModelDefinition {

    private Integer id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_phase_model_definition")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
