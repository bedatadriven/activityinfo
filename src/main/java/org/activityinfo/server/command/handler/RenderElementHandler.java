/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Date;

import org.activityinfo.server.authentication.SecureTokenGenerator;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.UrlResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DateRange;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.google.common.base.Strings;
import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.RenderElement
 */
public class RenderElementHandler implements CommandHandler<RenderElement> {

	private AWSCredentials credentials;
	private DeploymentConfiguration config;
    private final RendererFactory rendererFactory;
    private final ReportGenerator generator;


    @Inject
    public RenderElementHandler(RendererFactory rendererFactory, DeploymentConfiguration config, AWSCredentials credentials, ReportGenerator generator) {
    	this.credentials = credentials;
    	this.config = config;
        this.rendererFactory = rendererFactory;
        this.generator = generator;
    }

    public CommandResult execute(RenderElement cmd, User user) throws CommandException {
		try {
			Renderer renderer = rendererFactory.get(cmd.getFormat());
	
	        // render first to a temporary file on the local fs
			File tempFile = File.createTempFile("report", "activityinfo");
		    FileOutputStream os = new FileOutputStream(tempFile);
			try {
		        generator.generateElement(user, cmd.getElement(), new Filter(), new DateRange());
				renderer.render(cmd.getElement(), os);      
			} finally {
		        os.close();
		        tempFile.deleteOnExit();
			}
	
	        // now upload to S3
			AmazonS3Client client = new AmazonS3Client(credentials);
			String bucket = config.getProperty("exports.bucket", "ai-generated");
			String key = SecureTokenGenerator.generate();
			
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(renderer.getMimeType());
			metadata.setContentDisposition("attachment; filename=\"" + composeFilename(cmd, renderer));
			
			PutObjectRequest put = new PutObjectRequest(bucket, key, tempFile);
			put.setStorageClass(StorageClass.ReducedRedundancy);
			put.setMetadata(metadata);
			
			client.putObject(put);
			
			URL url = client.generatePresignedUrl(bucket, key, new Date( new Date().getTime() + 1000 * 60 * 120 ) );
			
	        return new UrlResult(url.toString());
		} catch(Exception e) {
			throw new RuntimeException("Exception generating export", e);
		}
    }

	private String composeFilename(RenderElement cmd, Renderer renderer) {
		if(Strings.isNullOrEmpty(cmd.getElement().getTitle())) {
			return "Report" + renderer.getFileSuffix();
		} else {
			return cmd.getElement().getTitle() +  renderer.getFileSuffix();
		}
	}
}
