/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dao.SqlAdminDAO;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class GetAdminEntitiesHandler implements CommandHandler<GetAdminEntities> {

    private final HibernateEntityManager entityManager;
    
    @Inject
    public GetAdminEntitiesHandler(HibernateEntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    public CommandResult execute(final GetAdminEntities cmd, User user) throws CommandException {
    	
    	final AdminEntityResult result[] = new AdminEntityResult[1];
    	
    	entityManager.getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				result[0] = new AdminEntityResult(SqlAdminDAO.query(connection, cmd));
			}
		});
    	return result[0];
    }
}
