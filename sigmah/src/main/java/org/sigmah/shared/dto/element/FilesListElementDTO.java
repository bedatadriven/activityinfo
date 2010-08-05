/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import org.sigmah.shared.command.result.ValueResult;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 *
 */
public class FilesListElementDTO extends FlexibleElementDTO {
    
	private static final long serialVersionUID = 8520711106031085130L;

	@Override
	public String getEntityName() {
		return "Flexible Element > files list";
	}

	@Override
	public Component getComponent(ValueResult valueResult) {
		FileUploadField uploadField = new FileUploadField();
		uploadField.setFieldLabel(getLabel());
		
		return uploadField;
	}


    
}
