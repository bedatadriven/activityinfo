/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dispatch.remote;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Indicates that the Dispatcher should connect directly to the
 * remote service without caching, batching, retrying, etc.
 */
@BindingAnnotation
@Target({ FIELD, PARAMETER, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Direct {
    
}
