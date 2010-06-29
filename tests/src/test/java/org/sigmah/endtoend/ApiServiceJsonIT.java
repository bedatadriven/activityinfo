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
 * Copyright 2009 Alex Bertram and contributors.
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
