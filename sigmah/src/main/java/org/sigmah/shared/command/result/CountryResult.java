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
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.sigmah.shared.command.result;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import org.sigmah.shared.dto.CountryDTO;

import java.util.ArrayList;
import java.util.List;

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
