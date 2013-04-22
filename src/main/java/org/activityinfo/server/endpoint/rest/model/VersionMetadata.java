package org.activityinfo.server.endpoint.rest.model;

public class VersionMetadata {
    private String message;
    private String sourceFilename;
    private String sourceUrl;
    private String sourceMD5;
    private String sourceMetadata;

    /**
     * @return a message describing the change
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return The filename of the source file
     */
    public String getSourceFilename() {
        return sourceFilename;
    }

    public void setSourceFilename(String sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    /**
     * @return the URL of the source file
     */
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    /**
     * 
     * @return the MD5 hash of the source file
     */
    public String getSourceMD5() {
        return sourceMD5;
    }

    public void setSourceMD5(String sourceMD5) {
        this.sourceMD5 = sourceMD5;
    }

    public String getSourceMetadata() {
        return sourceMetadata;
    }

    public void setSourceMetadata(String sourceMetadata) {
        this.sourceMetadata = sourceMetadata;
    }

}
