package org.activityinfo.shared.report.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.activityinfo.shared.report.content.TableContent;
import org.activityinfo.shared.report.content.TreeNode;


public class TableElement extends ReportElement {


	public static class Column extends TreeNode<Column>  {
		
		
		private boolean orderAscending;
		private String label;
        private String property;
        private int propertyQualifyingId;

        private List<Column> children = null;

        public Column() {

        }

        public Column(String label) {
            this.label = label;
        }

        @Override
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
		
		public boolean isOrderAscending() {
			return orderAscending;
		}

		public void setOrderAscending(boolean orderAscending) {
			this.orderAscending = orderAscending;
		}

        @Override
        public List<Column> getChildren() {
            if(children == null) {
                return Collections.emptyList();
            } else {
                return children;
            }
        }

        public void addChild(Column column) {
            if(children == null) {
                children = new ArrayList<Column>();
            }
            children.add(column);
        }

        @Override
        public boolean isLeaf() {
            return children == null;
        }

        public String getProperty() {
            return property;
        }

        public int getPropertyQualifyingId() {
            return propertyQualifyingId;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public void setPropertyQualifyingId(int propertyQualifyingId) {
            this.propertyQualifyingId = propertyQualifyingId;
        }
    }

	private Column rootColumn = new Column();
	private List<Column> sortBy = new ArrayList<Column>();
	private List<Column> panelBy = new ArrayList<Column>();
	private int frozenColumns = 0;
    private TableContent content;
	
	public Column getRootColumn() {
		return rootColumn;
	}
	
	public List<Column> getSortBy() {
		return sortBy;
	}
	
	public List<Column> getPanelBy() {
		return panelBy;
	}

	/**
	 * 
	 * @return The number of left-most columns that should be frozen 
	 * when the table is presented as a spreadsheet
	 */
	public int getFrozenColumns() {
		return frozenColumns;
	}

	public void setFrozenColumns(int frozenColumns) {
		this.frozenColumns = frozenColumns;
	}

    public TableContent getContent() {
        return content;
    }

    public void setContent(TableContent content) {
        this.content = content;
    }
}
