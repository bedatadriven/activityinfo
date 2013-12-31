package org.activityinfo.server.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.PivotResult;
import org.activityinfo.shared.command.RequestChange;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.test.InjectionSupport;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class ProjectTest extends CommandTestCase {
    
    @Test
    public void deleteProject() {
        
        
        setUser(1);
        
        long originalDatabaseVersion = lookupDbVersion(1);
        
        int projectId = 2;
        
        execute(RequestChange.delete("Project", projectId));
        
        SchemaDTO schema = execute(new GetSchema());
        assertThat(schema.getProjectById(projectId), nullValue());
        
        // make sure it's gone from sites
        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Site, 3);
        SiteResult sites = execute(new GetSites(filter));
        
        assertThat(sites.getData().get(0).getProject(), is(nullValue()));
        
        // and doesn't show up in pivoting...
        PivotSites pivot = new PivotSites();
        Dimension projectDimension = new Dimension(DimensionType.Project);
        pivot.setDimensions(projectDimension);
        pivot.setFilter(filter);
        
        PivotResult buckets = execute(pivot);
        assertThat(buckets.getBuckets().size(), equalTo(1));
        assertThat(buckets.getBuckets().get(0).getCategory(projectDimension), is(nullValue()));
        
        // make sure the version number of the database is updated
        assertThat(lookupDbVersion(1), not(equalTo(originalDatabaseVersion)));
    
    }


    @Test
    public void updateProject() {
        setUser(1);
        
        SchemaDTO schema = execute(new GetSchema());
        
        ProjectDTO project = schema.getProjectById(2);
        project.setName("RRMP II");
        project.setDescription("RRMP The Next Generation");
        
        execute(RequestChange.update(project, "name", "description"));
        
        schema = execute(new GetSchema());
        
        assertThat(schema.getProjectById(2).getName(), equalTo("RRMP II"));   
        assertThat(schema.getProjectById(2).getDescription(), equalTo("RRMP The Next Generation"));   
        
    }

    private long lookupDbVersion(int dbId) {
        return em.find(UserDatabase.class, dbId).getVersion();
    }
    
}
