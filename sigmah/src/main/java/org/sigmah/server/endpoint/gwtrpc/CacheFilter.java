/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;


import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Instructs browsers to cache application ressources "until the sun explodes" (or actually a year)
 * Exceptions are made for files containing the token ".nocache."
 * <p/>
 * <p/>
 * See http://www.infoq.com/articles/gwt-high-ajax
 */
@Singleton
public class CacheFilter implements Filter {

    private FilterConfig filterConfig;

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestURI = httpRequest.getRequestURI();
        if (requestURI.contains(".cache.") || requestURI.contains("/gxt")) {
            long today = new Date().getTime();
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setDateHeader("Expires", today + 31536000000L);

        } else if (requestURI.contains(".nocache.")) {

            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.addHeader("Pragma", "no-cache");
            httpResponse.setDateHeader("Expires", -1);
            httpResponse.setHeader("Cache-Control", "no-cache");
        }
        filterChain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }
}
