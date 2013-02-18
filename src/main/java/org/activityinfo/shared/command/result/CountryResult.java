/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.dto.CountryDTO;

import com.extjs.gxt.ui.client.data.ListLoadResult;

public class CountryResult implements CommandResult, ListLoadResult<CountryDTO> {
    private List<CountryDTO> data;


    /** Required for serialization */
    public CountryResult() {
        
    }

    public CountryResult(ArrayList<CountryDTO> data) {
        this.data = data;
    }
    @Override
    public List<CountryDTO> getData() {
        return data;
    }
    public void setData(List<CountryDTO> data) {
        this.data = data;
    }
}
