package org.activityinfo.server.mail;

public class MessageAttachment {
    private String contentType;
    private String filename;
    private byte[] content;
    
    public String getContentType() {
        return contentType;
    }
    
    public MessageAttachment setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    public String getFilename() {
        return filename;
    }
    public MessageAttachment withFileName(String filename) {
        this.filename = filename;
        return this;
    }
    public byte[] getContent() {
        return content;
    }
    
    public MessageAttachment setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public MessageAttachment withContent(byte[] content, String contentType) {
        this.content = content;
        this.contentType = contentType;
        return this;
    }
    
    
}
