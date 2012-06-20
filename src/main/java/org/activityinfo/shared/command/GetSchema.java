/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import org.activityinfo.shared.dto.SchemaDTO;


/**
 * Returns a {@link org.activityinfo.shared.dto.SchemaDTO} data transfer object that
 * includes the definitions of a databases visible to the authenticated user.
 *
 * @author Alex Bertram
 */
public class GetSchema implements Command<SchemaDTO> {
    
	private int newElement;
	private String foobar;
	private double thirdElement;
	private double forthElement;
	private int foofoo;
	
	
	
	
	
	
	public int getFoofoo() {
		return foofoo;
	}

	public void setFoofoo(int foofoo) {
		this.foofoo = foofoo;
	}

	public double getForthElement() {
		return forthElement;
	}

	public void setForthElement(double forthElement) {
		this.forthElement = forthElement;
	}

	public double getThirdElement() {
		return thirdElement;
	}

	public void setThirdElement(double thirdElement) {
		this.thirdElement = thirdElement;
	}

	public int getNewElement() {
		return newElement;
	}

	public void setNewElement(int newElement) {
		this.newElement = newElement;
	}

	public String getFoobar() {
		return foobar;
	}

	public void setFoobar(String foobar) {
		this.foobar = foobar;
	}

	@Override
    public String toString() {
        return "GetSchema";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        return obj instanceof GetSchema;
    }
}
