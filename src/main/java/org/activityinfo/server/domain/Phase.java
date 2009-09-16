package org.activityinfo.server.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/*
 * @author Alex Bertram
 */

@Entity
public class Phase implements Serializable {

    private int id;
    private UserDatabase database;
    private String name;
    private Date date1;
    private Date date2;

    @Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "PhaseId", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DatabaseId", nullable = false)
    public UserDatabase getDatabase() {
        return database;
    }

    public void setDatabase(UserDatabase database) {
        this.database = database;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Temporal(value = TemporalType.DATE)
    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    @Temporal(value = TemporalType.DATE)
    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }
}
