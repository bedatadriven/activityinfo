/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Organization {
    private int id;
    private String name;
    private OrgUnit root;
    private List<OrgUnit> orgUnit;

    public Organization() {

    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne
    public OrgUnit getRoot() {
        return root;
    }

    public void setRoot(OrgUnit root) {
        this.root = root;
    }

    @OneToMany(mappedBy = "organization")
    public List<OrgUnit> getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(List<OrgUnit> orgUnit) {
        this.orgUnit = orgUnit;
    }
}
