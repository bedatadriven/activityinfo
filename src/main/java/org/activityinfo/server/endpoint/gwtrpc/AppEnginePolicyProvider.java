package org.activityinfo.server.endpoint.gwtrpc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.common.io.ByteStreams;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;

/**
 * Provides SerializationPolicy from archives in Google Cloud Storage.
 * 
 * <p>GWT uses a file called STRONGNAME.gwt.rpc to determine which types are allowed
 * to be deserialized on the server. This is to stop bad actors from being able to
 * instantiate whatever java objects they want via GWT-RPC.
 * 
 * <p>STRONGNAME is the MD5 hash of the compiled javascript, unique to a specific version 
 * and browser/language combination. 
 * 
 * <p>Because we use the AppCache to aggressively cache the client on the 
 * user's browser, it is very common that when the user opens the client after
 * a few days away, the server has a new version (we are pushing out updates
 * several times a week).
 * 
 * <p>Unfortunately, this means that the server will not be able to find the permutation's
 * corresponding .gwt.rpc file, and the RPC command will fail with a IncompatibleRemoteException.
 * This forces the user to download the new version of the app before continuing.
 * 
 * <p>Ideally what we want is the user to be able to use the application while the AppCache fetches
 * the new version in the background. 
 * 
 * <p>To accomplish this, we archive any gwt.rpc file that ever gets requested in Google Cloud Storage,
 * and then check this archive each time a gwt.rpc file is requested. This way, even 
 */
public class AppEnginePolicyProvider {


	private static final Logger LOGGER = Logger.getLogger(AppEnginePolicyProvider.class.getName());
	
	private ServletContext servletContext;
		
	public AppEnginePolicyProvider(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public SerializationPolicy getSerializationPolicy(String moduleBaseURL,
			String strongName)  {
		
		// try to load first from cloud storage
		FileService fileService = FileServiceFactory.getFileService();
        AppEngineFile file = pathForStrongName(strongName) ;
        FileReadChannel readChannel;
		try {
			readChannel = fileService.openReadChannel(file, false);
	        InputStream in = Channels.newInputStream(readChannel);
	        return SerializationPolicyLoader.loadFromStream(in, null);
		} catch (IOException e) {
			return readFromDeployment(moduleBaseURL, strongName);
			
		} catch (ParseException e) {
			LOGGER.log(Level.SEVERE, "Error parsing serialization policy from GCS cache", e);
			return readFromDeployment(moduleBaseURL, strongName);
		}
	}

	private SerializationPolicy readFromDeployment(String moduleBaseURL,
			String strongName)  {
		
		// Read the serialization policy from the 
		// deployed application files
		byte[] bytes;
		SerializationPolicy policy;

		try {
			String file = deploymentPath(moduleBaseURL, strongName);
			InputStream in = servletContext.getResourceAsStream(file);
			bytes = ByteStreams.toByteArray(in);
			in.close();
	        policy = SerializationPolicyLoader.loadFromStream(new ByteArrayInputStream(bytes), null);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to read serialization policy from deployment", e);
			return null;
		}
		
		// store to GCS
		try { 
			cacheToCloudStorage(strongName, bytes);
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, "Failed to cache serialization policy to cloud storage", e);
		}
		
		return policy;
	}

	private void cacheToCloudStorage(String strongName,
			byte[] bytes) throws IOException {

		GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
		  .setBucket("activityinfo-gwt-rpc")
		  .setKey(strongName + ".gwt.rpc")
		  .setMimeType("text/plain");
		  
		FileService fileService = FileServiceFactory.getFileService();
		AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
		FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, true);
		writeChannel.write(ByteBuffer.wrap(bytes));
		writeChannel.closeFinally();	
	}

	private String deploymentPath(String moduleBaseURL, String strongName) throws MalformedURLException {
		String modulePath = new URL(moduleBaseURL).getPath();
		return modulePath + strongName + ".gwt.rpc";
	}

	private AppEngineFile pathForStrongName(String strongName) {
		return new AppEngineFile("/gs/activityinfo-gwt-rpc/" + strongName + ".gwt.rpc");
	}
	

}
