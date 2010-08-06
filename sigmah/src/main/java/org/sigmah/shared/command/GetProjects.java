/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import java.util.ArrayList;
import java.util.List;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.dto.CountryDTO;

/**
 * Retrieves the list of Projects available to the user
 */
public class GetProjects implements Command<ProjectListResult> {
    private List<CountryDTO> countries;

    public GetProjects() {
        countries = null;
    }

    public GetProjects(List<CountryDTO> countries) {
        this.countries = new ArrayList<CountryDTO>(countries);
    }

    public List<CountryDTO> getCountries() {
        return countries;
    }
    public void setCountries(List<CountryDTO> countries) {
        this.countries = countries;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GetProjects other = (GetProjects) obj;
        if (this.countries != other.countries && (this.countries == null || !this.countries.equals(other.countries))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.countries != null ? this.countries.hashCode() : 0);
        return hash;
    }
}

