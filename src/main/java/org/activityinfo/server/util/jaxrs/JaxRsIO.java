package org.activityinfo.server.util.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

public class JaxRsIO {
    public static Response stream(final InputStream is) {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                try {
                    int n;
                    byte[] buffer = new byte[1024];
                    while ((n = is.read(buffer)) > -1) {
                        os.write(buffer, 0, n);
                    }
                    os.close();
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        return Response.ok(stream).build();
    }
}
