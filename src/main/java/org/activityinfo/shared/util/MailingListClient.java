package org.activityinfo.shared.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Client that handles subscriptions to the ActivityInfo
 * mailing list
 */
@Singleton
public class MailingListClient {

    private static final Logger LOGGER = Logger.getLogger(MailingListClient.class.getName());

    private final String apiKey;
    private final String listId;
        
    @Inject
    public MailingListClient(DeploymentConfiguration config) {
        this.apiKey = config.getProperty("mailchimp.api.key");
        this.listId = config.getProperty("mailchimp.list.id", "9289430112");
    }
    
    public void subscribe(User user) {
        
        ListSubscribeMethod method = new ListSubscribeMethod();
        method.apiKey = apiKey;
        method.doubleOptIn = false;
        method.emailAddress = user.getEmail();
        method.id = listId;
        method.mergeVars.email = user.getEmail();
        method.mergeVars.firstName = user.getName();
        
        try {
            post(method);
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to subscribe user", e);
        }
    }

    private void post(ListSubscribeMethod method) throws Exception {
        URL url = new URL("http://us4.api.mailchimp.com/1.3/?method=listSubscribe");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
            "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(5 * 60 * 1000);
        conn.setReadTimeout(5 * 60 * 1000);
        ObjectMapper mapper = new ObjectMapper();
        
        OutputStreamWriter writer = new OutputStreamWriter(
            conn.getOutputStream(), Charsets.UTF_8);
        String json = mapper.writeValueAsString(method);
        LOGGER.fine("MailChimp: " + json);
        writer.write(json);
        writer.flush();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            conn.getInputStream()));
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        writer.close();
        reader.close();
    }
    

    // Holds a subscriber's merge_vars info (see http://apidocs.mailchimp.com/api/1.3/listsubscribe.func.php )
    public static class MergeVars {
        
        @JsonProperty("EMAIL")
        private String email;
        
        @JsonProperty("FNAME")
        private String firstName;
        
    }
    
    public static class ListSubscribeMethod {
        
        @JsonProperty("apikey")
        private String apiKey;
        
        @JsonProperty
        private String id;
        
        @JsonProperty("email_address")
        private String emailAddress;
        
        @JsonProperty("double_optin")
        private boolean doubleOptIn;
        
        @JsonProperty("merge_vars")
        private MergeVars mergeVars = new MergeVars();
    }

}
