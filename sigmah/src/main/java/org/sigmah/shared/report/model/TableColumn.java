/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import org.sigmah.shared.report.content.TreeNode;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
*/
public class TableColumn extends TreeNode<TableColumn> {

    public enum SortOrder {
        Ascending,
        Descending
    }

    private String label;
    private String property;
    private Integer propertyQualifyingId;
    private SortOrder order;
    private List<TableColumn> children = new ArrayList<TableColumn>(0);

    public TableColumn() {
    }

    public TableColumn(String label) {
        this.label = label;
    }

    public TableColumn(String label, String property) {
        this.label = label;
        this.property = property;
    }

    @Override
    @XmlAttribute
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlTransient
    public boolean isOrderAscending() {
        return order== SortOrder.Ascending;
    }

    @XmlAttribute
    public SortOrder getOrder() {
        return order;
    }

    public void setOrder(SortOrder order) {
        this.order = order;
    }

    @Override
    @XmlElement(name="column")
    public List<TableColumn> getChildren() {
        return children;
    }

    public void setChildren(List<TableColumn> children) {
        this.children = children;
    }

    public void addChild(TableColumn column) {
        children.add(column);
    }

    @Override
    @XmlTransient
    public boolean isLeaf() {
        return children.size() == 0;
    }

    @XmlAttribute(name="source")
    public String getProperty() {
        return property;
    }

    @XmlAttribute(name="sourceId")
    public Integer getPropertyQualifyingId() {
        return propertyQualifyingId;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setPropertyQualifyingId(Integer propertyQualifyingId) {
        this.propertyQualifyingId = propertyQualifyingId;
    }
}
