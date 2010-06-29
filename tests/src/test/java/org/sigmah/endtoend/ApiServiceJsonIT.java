/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.endtoend;

import junit.framework.Assert;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONException;
import org.json.JSONTokener;
import org.junit.Test;

import java.io.IOException;

/**
 *
 * Integration test that verifies that the API service can be accessed through JSON.
 *
 * @author Alex Bertram
 */
public class ApiServiceJsonIT {

    private static final String ENDPOINT_URL = "http://localhost:9090/activityinfo/api";

    @Test
    public void testAuthentication() throws IOException, JSONException {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(ENDPOINT_URL);
        method.setRequestEntity(new StringRequestEntity(
                "{authenticate: {email: 'akbertram@gmail.com', password: 'mzuri787'}}",
                "application/json",
                "UTF-8"));
        client.executeMethod(method);

        Assert.assertEquals("status code", 200, method.getStatusCode());

        String jsonResult = method.getResponseBodyAsString();

        JSONTokener tokenizer = new JSONTokener(jsonResult);
        String authToken = (String) tokenizer.nextValue();
        System.out.println("token = " + authToken);

        Assert.assertEquals("token length", 32, authToken.length());
    }
}
