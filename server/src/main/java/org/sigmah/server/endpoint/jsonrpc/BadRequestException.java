/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.jsonrpc;

import javax.servlet.http.HttpServletResponse;

public class BadRequestException extends JsonRpcException {

    public BadRequestException(String message) {
        super(HttpServletResponse.SC_BAD_REQUEST, message);
    }
}
