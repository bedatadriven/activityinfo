/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.filter;


import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.io.IOException;

/**
 * Instructs browsers to cache application ressources "until the sun explodes" (or actually a year)
 * Exceptions are made for files containing the token ".nocache."
 *
 *
 * See http://www.infoq.com/articles/gwt-high-ajax
 *
 */
@Singleton
public class CacheFilter implements Filter {

    private FilterConfig filterConfig;

    public void doFilter( ServletRequest request, ServletResponse response,
                          FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest)request;

        String requestURI = httpRequest.getRequestURI();
        if( requestURI.contains(".cache.") || requestURI.contains("/gxt")){
            long today = new Date().getTime();
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            httpResponse.setDateHeader("Expires", today+31536000000L);

        } else if(requestURI.contains(".nocache.")) {
                                
            HttpServletResponse httpResponse = (HttpServletResponse)response;
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
