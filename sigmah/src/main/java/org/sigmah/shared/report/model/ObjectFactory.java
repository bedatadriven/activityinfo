/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * @author Alex Bertram
 */
@XmlRegistry
public class ObjectFactory {

    public Report createReport() { return new Report(); }
    
}
