package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.UploadAttachment;
import org.sigmah.shared.command.UploadAttachment.UploadAttachmentResult;
import org.sigmah.shared.dto.AttachmentDTO;

import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 1. Client uploads image to elasticbeanstalk s3 bucket
 * 2. Client adds attachment to the database
 */
public class UploadAttachmentHandler implements CommandHandlerAsync<UploadAttachment, UploadAttachmentResult>{
	@Override
	public void execute(UploadAttachment command, ExecutionContext context, AsyncCallback<UploadAttachmentResult> callback) {
		AttachmentDTO attachment = command.getAttachment();
		SqlInsert.insertInto("Attachment")
				 .value("attachmentId", attachment.getId())
				 .value("createdDate", attachment.getCreatedDate())
				 .value("sizeInKb", attachment.getSizeInKb())
				 .value("extension", attachment.getExtension())
				 .value("siteId", attachment.getSiteId())
				 
				 .execute(context.getTransaction());
	}

}
