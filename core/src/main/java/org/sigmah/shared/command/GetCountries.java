/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CountryResult;

public class GetCountries implements Command<CountryResult> {
    private boolean containingProjects;
    
    public GetCountries() {
    }
    
    public GetCountries(boolean containingProjects) {
        this.containingProjects = containingProjects;
    }

    public boolean isContainingProjects() {
        return containingProjects;
    }

    public void setContainingProjects(boolean containingProjects) {
        this.containingProjects = containingProjects;
    }
}
