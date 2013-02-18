/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.google.inject.ScopeAnnotation;


@ScopeAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface TestScoped {
}
