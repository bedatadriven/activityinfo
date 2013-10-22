package org.activityinfo.server.endpoint.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.entity.IndicatorValue;
import org.activityinfo.test.InjectionSupport;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class SitesResourcesTest extends CommandTestCase2 {
    
    private static final int DATABASE_OWNER = 1;

    @Test
    public void indicatorsArePresent() throws IOException {

        setUser(DATABASE_OWNER);
        
        SitesResources resource = new SitesResources(getDispatcherSync());
        List<Integer> activityIds = Lists.newArrayList();
        List<Integer> databaseIds = Lists.newArrayList(2);
        String format = null;
        String json = resource.query(activityIds, databaseIds, format); 
        
        System.out.println(json);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getJsonFactory();
        JsonParser jp = factory.createJsonParser(json);
        ArrayNode array = (ArrayNode) mapper.readTree(jp);
        
        
        assertThat(array.size(), equalTo(3));
        
        JsonNode site6 = getSiteById(array, 6);
        assertThat(site6.path("id").asInt(), equalTo(6));
        assertThat(site6.path("activity").asInt(), equalTo(4));
        
        double indicatorValue = site6.path("indicatorValues").path("6").asDouble();
        assertThat(indicatorValue, equalTo(70d));
        
    }

    protected JsonNode getSiteById(ArrayNode array, int siteId) {
        for(JsonNode node : array) {
            if(node.get("id").asInt() == siteId) {
                return node;
            }
        }
        throw new AssertionError("No site json object with id " + siteId);
    }
}
