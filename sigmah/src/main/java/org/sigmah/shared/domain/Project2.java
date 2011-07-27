package org.sigmah.shared.domain;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Project2 {

    private int id;


    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
