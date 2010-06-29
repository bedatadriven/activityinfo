/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.test;

import com.google.inject.ScopeAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@ScopeAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface TestScoped {
}
