/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mock;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Alex Bertram
 */
public class MockServletOutputStream extends ServletOutputStream {

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public void flush() throws IOException {
        baos.flush();
    }

    public void write(byte[] b) throws IOException {
        baos.write(b);
    }

    @Override
    public void write(int b) throws IOException {
        byte[] ba = new byte[1];
        ba[0] = (byte) b;
        baos.write(ba);
    }
}
