/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import java.io.Serializable;
import java.util.ArrayList;

import org.activityinfo.shared.command.result.VoidResult;

public class UpdateMonthlyReports implements Command<VoidResult> {


    public  static class Change implements Serializable {
        private Month month;
        private int indicatorId;
        private Double value;

        public Change() {

        }

        public Change(int indicatorId, Month month, Double value) {
            this.indicatorId = indicatorId;
            this.setMonth(month);
            this.setValue(value);
        }

		public void setMonth(Month month) {
			this.month = month;
		}

		public Month getMonth() {
			return month;
		}

		public void setIndicatorId(int indicatorId) {
			this.indicatorId = indicatorId;
		}

		public int getIndicatorId() {
			return indicatorId;
		}

		public void setValue(Double value) {
			this.value = value;
		}

		public Double getValue() {
			return value;
		}


    }

    private int siteId;
    private ArrayList<Change> changes;


    public UpdateMonthlyReports() {
    }

    public UpdateMonthlyReports(int siteId, ArrayList<Change> changes) {
        this.siteId = siteId;
        this.changes = changes;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public ArrayList<Change> getChanges() {
        return changes;
    }

    public void setChanges(ArrayList<Change> changes) {
        this.changes = changes;
    }
}
