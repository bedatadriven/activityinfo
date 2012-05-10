/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activityinfo.shared.report.model.Dimension;

public class PivotTableData implements Serializable {

	private Axis rootRow;
	private Axis rootColumn;


    public interface CellVisitor {
        void onVisit(Axis row, Axis column, Cell cell);
    }

	public PivotTableData() {
        rootRow = new Axis();
		rootColumn = new Axis();
	}

	
	public boolean isEmpty() {
		return rootRow.isLeaf() && rootColumn.isLeaf();
	}

	public Axis getRootRow() {
		return rootRow;
	}

	public Axis getRootColumn() {
		return rootColumn;
	}
	
	public Axis getRootCategory() {
		return getRootRow();
	}
	
	public Axis getRootSeries() {
		return getRootColumn();
	}

    public static class Cell implements Serializable {
		private Double value;

        /**
         * Required for GWT serialization
         */
        private Cell() {
        }
		
		public Cell(Double value) {
			this.value = value;
		}
		
		public Double getValue() {
			return value;
		}

        public void setValue(Double value) {
            this.value = value;
        }
    }


    public static List<String> flattenLabels(List<Axis> list) {
        List<String> labels = new ArrayList<String>();
        for(Axis axis : list) {
            labels.add(axis.flattenLabel());
        }
        return labels;
    }


    public void visitAllCells(CellVisitor visitor) {
        rootRow.visitAllCells(visitor);
    }
	
	public static class Axis extends TreeNode<Axis> implements Serializable {

		private Axis parent;
		private Dimension dimension;
		private DimensionCategory category;
        private String label;

		private Map<DimensionCategory, Axis> childMap = new HashMap<DimensionCategory, Axis>();
		private Map<Axis, Cell> cells = new HashMap<Axis, Cell>();
		
		private List<Axis> children = new ArrayList<Axis>();

        public Axis() {

		}
		
		public Axis(Axis parent, Dimension dimension, DimensionCategory category, String label) {
			this.parent = parent;
			this.dimension = dimension;
			this.category = category;
            this.label = label;
		}

        public Axis getChild(DimensionCategory category) {
            return childMap.get(category);
        }

        public Axis addChild(Dimension childDimension, DimensionCategory category, String categoryLabel,
                             Comparator<Axis> comparator) {

            Axis child = new Axis(this, childDimension, category, categoryLabel);

            childMap.put(category, child);

            if(comparator == null) {
                children.add(child);
            } else {
                insertChildSorted(child, comparator);
            }
            return child;            
        }

        private void insertChildSorted(Axis child, Comparator<Axis> comparator) {
            for(int i=0; i!=children.size(); ++i) {
                if(comparator.compare(child, children.get(i)) < 0) {
                    children.add(i, child);
                    return;
                }
            }
            children.add(child);
        }

		public Axis nextSibling() {
			if(parent == null) {
                return null;
            }

            int i = parent.children.indexOf(this);

            if(i < 1) {
                return null;
            } else {
                return parent.children.get(i-1);
            }
		}
		
		public Axis prevSibling() {
			if(parent == null) {
				return null;
			}

            int i = parent.children.indexOf(this);

            if(i == parent.children.size()-1) {
                return null;
            } else {
                return parent.children.get(i+1);
            }
		}

		public Axis firstChild() {
			return children.get(0);
		}
		
		public Axis lastChild() {
			return children.get(children.size()-1);
		}
						
		public void setValue(Axis column, Double value) {
			cells.put(column, new Cell(value));
		}
		
		public Cell getCell(Axis column) {
			return cells.get(column);
		}
		
		public Dimension getDimension() {
			return dimension;
		}

		public DimensionCategory getCategory() {
			return category;
		}

        @Override
        public String getLabel() {
            return label;
        }

        public Map<Axis, Cell> getCells() {
			return cells;
		}

		public int getChildCount() {
			return childMap.size();
		}

		public Axis getParent() {
			return parent;
		}

		@Override
		public List<Axis> getChildren() {
			return children;
		}

        public String flattenLabel() {
            StringBuilder sb =  new StringBuilder();
            Axis axis = this;
            do {
                if(axis.getLabel() != null) {
                    if(sb.length()!=0) {
                        sb.append(" ");
                    }

                    sb.append(axis.getLabel());
                }
                axis = axis.getParent();

            } while(axis != null);

            return sb.toString();
        }

        public void toString(int depth, StringBuilder sb) {
        	for(int i=0;i!=depth;++i) {
        		sb.append("  ");
        	}
        	sb.append(dimension).append(":").append(label);

        	for(Entry<Axis, Cell> column : cells.entrySet()) {
        		sb.append(" | ");
        		sb.append(column.getKey().label).append("=").append(column.getValue().getValue());
        	}
        	sb.append("\n");
        	for(Axis child : getChildren()) {
        		child.toString(depth+1, sb);
        	}

        }

        protected void visitAllCells(CellVisitor visitor) {
            for(Map.Entry<Axis, Cell> entry : cells.entrySet()) {
                visitor.onVisit(this, entry.getKey(), entry.getValue());
            }
            for(Axis childRow : this.children) {
                childRow.visitAllCells(visitor);
            }
        }

        private Map<DimensionCategory, Axis> getChildMap() {
            return childMap;
        }

        private void setChildMap(Map<DimensionCategory, Axis> childMap) {
            this.childMap = childMap;
        }

        private void setParent(Axis parent) {
            this.parent = parent;
        }

        private void setDimension(Dimension dimension) {
            this.dimension = dimension;
        }

        private void setCategory(DimensionCategory category) {
            this.category = category;
        }

        private void setLabel(String label) {
            this.label = label;
        }

        private void setCells(Map<Axis, Cell> cells) {
            this.cells = cells;
        }

        private void setChildren(List<Axis> children) {
            this.children = children;
        }

        public double getMaxValue() {
            return findMaxValue(0.0);
        }

        private double findMaxValue(double max) {
            for(Cell cell : cells.values()) {
                if(cell.getValue()!=null && cell.getValue() > max) {
                    max=cell.getValue();
                }
            }
            for(Axis child : children) {
                max = child.findMaxValue(max);
            }

            return max;
        }
    }

    public static class RangeCalculator implements CellVisitor {
        private double minValue = Double.MAX_VALUE;
        private double maxValue = -Double.MAX_VALUE;

        @Override
        public void onVisit(Axis row, Axis column, Cell cell) {
            if(cell.getValue() != null) {
                if(cell.getValue() < minValue) {
                    minValue = cell.getValue();
                }
                if(cell.getValue() > maxValue) {
                    maxValue = cell.getValue();
                }
            }
        }

        public double getMinValue() {
            return minValue;
        }

        public double getMaxValue() {
            return maxValue;
        }
    }
    
    @Override 
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(" COLUMNS:\n");
        for(Axis col : rootColumn.getChildren()) {
                col.toString(1,sb);
        }
        sb.append(" ROWS:\n");
        for(Axis row : rootRow.getChildren()) {
                row.toString(1,sb);
        }
        return sb.toString();
    }
}

