/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

import javax.persistence.*;

@Entity
public class OrgUnitPermission {
    private int id;
    private OrgUnit unit;
    private User user;
    private boolean viewAll;
    private boolean editAll;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    public OrgUnit getUnit() {
        return unit;
    }

    public void setUnit(OrgUnit unit) {
        this.unit = unit;
    }

    @OneToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isViewAll() {
        return viewAll;
    }

    public void setViewAll(boolean viewAll) {
        this.viewAll = viewAll;
    }

    public boolean isEditAll() {
        return editAll;
    }

    public void setEditAll(boolean editAll) {
        this.editAll = editAll;
    }
}
