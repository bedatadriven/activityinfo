/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import com.extjs.gxt.ui.client.data.RpcMap;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.EntityDTO;

import java.util.Map;

/**
 *
 * Updates a domain entity on the server.
 *
 * Some entities require specialized commands to create or update. See:
 * <ul>
 * <li>{@link org.sigmah.shared.command.AddPartner}</li>
 * <li>{@link UpdateUserPermissions}</li>
 * <li>{@link org.sigmah.shared.command.UpdateSubscription}</li>
 * </ul>
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class UpdateEntity implements Command<VoidResult> {

	private int id;
	private String entityName;
	private RpcMap changes;

    protected UpdateEntity() {
		
	}

    public UpdateEntity(String entityName, int id, Map<String, Object> changes) {
        this.entityName = entityName;
        this.id = id;
        this.changes = new RpcMap();
        this.changes.putAll(changes);
    }

	public UpdateEntity(EntityDTO model, Map<String, Object> changes) {
		this.id = model.getId();
        this.entityName = model.getEntityName();
		this.changes = new RpcMap();
		this.changes.putAll(changes);
	}

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public RpcMap getChanges() {
		return changes;
	}

	public void setChanges(RpcMap changes) {
		this.changes = changes;
	}


}
