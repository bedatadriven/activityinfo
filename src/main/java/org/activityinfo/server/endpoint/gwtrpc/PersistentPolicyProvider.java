package org.activityinfo.server.endpoint.gwtrpc;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.activityinfo.server.util.blob.BlobService;

import com.google.appengine.labs.repackaged.com.google.common.io.Closeables;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;
import com.google.inject.Inject;

/**
 * Provides SerializationPolicy from archives in Google Cloud Storage.
 * 
 * <p>
 * GWT uses a file called STRONGNAME.gwt.rpc to determine which types are
 * allowed to be deserialized on the server. This is to stop bad actors from
 * being able to instantiate whatever java objects they want via GWT-RPC.
 * 
 * <p>
 * STRONGNAME is the MD5 hash of the compiled javascript, unique to a specific
 * version and browser/language combination.
 * 
 * <p>
 * Because we use the AppCache to aggressively cache the client on the user's
 * browser, it is very common that when the user opens the client after a few
 * days away, the server has a new version (we are pushing out updates several
 * times a week).
 * 
 * <p>
 * Unfortunately, this means that the server will not be able to find the
 * permutation's corresponding .gwt.rpc file, and the RPC command will fail with
 * a IncompatibleRemoteException. This forces the user to download the new
 * version of the app before continuing.
 * 
 * <p>
 * Ideally what we want is the user to be able to use the application while the
 * AppCache fetches the new version in the background.
 * 
 * <p>
 * To accomplish this, we archive any gwt.rpc file that ever gets requested in
 * Google Cloud Storage, and then check this archive each time a gwt.rpc file is
 * requested. This way, even
 */
public class PersistentPolicyProvider {

    private static final Logger LOGGER = Logger
        .getLogger(PersistentPolicyProvider.class.getName());

    private ServletContext servletContext;
    private BlobService blobService;

    @Inject
    public PersistentPolicyProvider(BlobService blobService, ServletContext servletContext) {
        this.blobService = blobService;
        this.servletContext = servletContext;
    }

    public SerializationPolicy getSerializationPolicy(String moduleBaseURL,
        String strongName) {

        SerializationPolicy policy = readFromDeployment(moduleBaseURL, strongName);

        if(policy == null) {
            // try to fetch from storage
            try {
                InputSupplier<? extends InputStream> inputSupplier = blobService.get("/gwt-rpc/" + strongName);
                InputStream in = inputSupplier.getInput();
                try {
                    return SerializationPolicyLoader.loadFromStream(in, null);
                } finally {
                    Closeables.closeQuietly(in);
                }

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Could not load serialization policy from cache", e);
            }
        }
        
        return null;
    }

    private SerializationPolicy readFromDeployment(String moduleBaseURL,
        String strongName) {

        // Read the serialization policy from the
        // deployed application files
        byte[] bytes;
        SerializationPolicy policy;

        try {
            String file = deploymentPath(moduleBaseURL, strongName);
            InputStream in = servletContext.getResourceAsStream(file);
            bytes = ByteStreams.toByteArray(in);
            in.close();
            policy = SerializationPolicyLoader.loadFromStream(
                new ByteArrayInputStream(bytes), null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                "Failed to read serialization policy from deployment", e);
            return null;
        }

        // cache to blob service for later
        try {
            blobService.put("/gwt-rpc/" + strongName, ByteStreams.newInputStreamSupplier(bytes));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not cache serialization policy", e);
        }

        return policy;
    }


    private String deploymentPath(String moduleBaseURL, String strongName)
        throws MalformedURLException {
        String modulePath = new URL(moduleBaseURL).getPath();
        return modulePath + strongName + ".gwt.rpc";
    }
}
