package org.activityinfo.server.attachment;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;

public interface AttachmentService {

	void serveAttachment(String blobId, HttpServletResponse response) throws IOException;
	
	void upload(String key, FileItem fileItem, InputStream uploadingStream);
	
	FileItemFactory createFileItemFactory();
}
