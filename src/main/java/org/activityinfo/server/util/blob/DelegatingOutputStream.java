package org.activityinfo.server.util.blob;

import java.io.IOException;
import java.io.OutputStream;

public abstract class DelegatingOutputStream extends OutputStream {
    
    private final OutputStream out;

    public DelegatingOutputStream(OutputStream out) {
        super();
        this.out = out;
    }

    @Override
    public final void flush() throws IOException {
        out.flush();
    }

    @Override
    public final void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public final void write(byte[] b) throws IOException {
        out.write(b);
    }

    @Override
    public final void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public final void close() throws IOException {
        out.close();
        afterClosed();
    }
    
 
    protected abstract void afterClosed() throws IOException;

}
