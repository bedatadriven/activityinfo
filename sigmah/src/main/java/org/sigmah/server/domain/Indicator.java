/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;


import javax.persistence.*;
import java.util.Date;

/**
 * Defines an Indicator, a numeric value that can change over time.
 *
 * @author Alex Bertram
 *
 */
@Entity
@org.hibernate.annotations.Filter(
		name="hideDeleted",
		condition="DateDeleted is null"
	)
public class Indicator implements java.io.Serializable, Orderable, Deleteable, SchemaElement {

	private int id;
    private Date dateDeleted;

	private String name;
	private String units;
	private String description;
	
	private String sector;
	private String category;
	
	private boolean collectIntervention;
	private boolean collectMonitoring;
	
	private Activity activity;
	
	private int aggregation;
	
	private int sortOrder;
	private String listHeader;
	
	private QualityCriterion qualityCriterion;

	public Indicator() {
	}


    /**
     *
     * @return the id of this Indicator
     */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "IndicatorId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

    /**
     * Sets the id of this Indicator
     */
	public void setId(int indicatorId) {
		this.id = indicatorId;
	}

    /**
     * @return the name of this Indicator
     */
    @Column(name = "Name", nullable = false, length = 128)
	public String getName() {
		return this.name;
	}

    /**
     * Sets the name of the Indicator
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * @deprecated No longer used; see the Category property for a more general classification of indicators
     * @return the sector of intervention to which this Indicator belongs (NFI, Watsan, etc)
     */
	@Column(name = "Sector", nullable = true, length = 50)
	@Deprecated
    public String getSector() {
		return this.sector;
	}

    /**
     * Sets the sector of intervention to which this Indicator belongs (NFI, Watsan, etc)
     * @deprecated No loner used; see the Category property for a more general classificaton of indicators
     * @param sector
     */
    @Deprecated
	public void setSector(String sector) {
		this.sector = sector;
	}

    /**
     * Gets a description of the units in which this Indicator is expressed. Note that this
     * is for descriptive purpose only for the user, it does not carry any semantics for our system.
     *
     * @return description of the units in which this indicator is expressed. Examples: "households", "%"
     * "cm"
     */
	@Column(name = "Units", nullable = false, length = 15)
	public String getUnits() {
		return this.units;
	}

    /**
     * Sets the description of the units in which this indicator is expressed.
     * @param units a description of the units
     */
	public void setUnits(String units) {
		this.units = units;
	}

    /**
     * @return a full description of this indicator, containing perhaps detailed instructions on how
     * it is to be collected or calculated.
     */
    @Lob
	public String getDescription() {
		return this.description;
	}

    /**
     * Sets the description of this Indicator.
     */
	public void setDescription(String description) {
		this.description = description;
	}

    /**
     * @return the Activity which is implemented at this Site
     */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ActivityId", nullable = false)
	public Activity getActivity() {
		return this.activity;
	}

    /**
     * Sets the Activity which is implemented at this Site
     */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

    /**
     *
     * @return true if this Indicator is collected during the actual intervention. (Some indicators
     * are only collected afterwords, during the monitoring phase)
     */
	@Column(name = "CollectIntervention", nullable = false)
	public boolean getCollectIntervention() {
		return this.collectIntervention;
	}

    /**
     * Sets whether this Indicator is collected during the actual intervention.
     */
	public void setCollectIntervention(boolean collectIntervention) {
		this.collectIntervention = collectIntervention;
	}

    /**
     * @return  the method by which this Indicator is aggregated
     */
	@Column(name = "Aggregation", nullable = false)
	public int getAggregation() {
		return this.aggregation;
	}

    /**
     * Sets the method by which this Indicator is aggregated.
     */
	public void setAggregation(int aggregation) {
		this.aggregation = aggregation;
	}

    /**
     * @return true if this Indicator is collected during the monitoring phase
     */
	@Column(name = "CollectMonitoring", nullable = false)
	public boolean isCollectMonitoring() {
		return this.collectMonitoring;
	}

    /**
     * Sets whether this Indicator is collected during the monitoring phase
     */
	public void setCollectMonitoring(boolean collectMonitoring) {
		this.collectMonitoring = collectMonitoring;
	}

    /**
     * @return the sort order of this Indicator within its Activity
     */
	@Column(name = "SortOrder", nullable = false)
	public int getSortOrder() {
		return this.sortOrder;
	}

    /**
     * Sets the sort order of this Indicator within its Activity
     */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

    /**
     * @return a short list header that is used when this Indicator's values are displayed in a
     * grid
     */
	@Column(name = "ListHeader", length = 30)
	public String getListHeader() {
		return this.listHeader;
	}

    /**
     * Sets the short list header that is used when this Indicator's values are displayed within
     * a grid
     *
     */
	public void setListHeader(String listHeader) {
		this.listHeader = listHeader;
	}

    /**
     * Gets this Indicator's category. Categories are just strings that are used for organizing the
     * display of Indicators in the user interface.
     *
     * @return the name of the category
     */
	@Column(name = "Category", length = 50)
	public String getCategory() {
		return this.category;
	}

    /**
     * Sets this Indicator's category.
     */
	public void setCategory(String category) {
		this.category = category;
	}

    /**
     * @return the time at which this Indicator was deleted
     */
	@Column
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getDateDeleted() {
		return this.dateDeleted;
	}

    /**
     * Sets the time at which this Indicator was deleted.
     */
	public void setDateDeleted(Date deleteTime) {
		this.dateDeleted = deleteTime;
	}

    /**
     * Marks this Indicator as deleted.
     */
	public void delete() {
		setDateDeleted(new Date());
	}

    /**
     *
     * @return true if this Indicator has been deleted.
     */
	@Override
	@Transient
	public boolean isDeleted() {
		return getDateDeleted() == null;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "id_quality_criterion", nullable = true)
	public QualityCriterion getQualityCriterion() {
		return qualityCriterion;
	}
	
	public void setQualityCriterion(QualityCriterion qualityCriterion) {
		this.qualityCriterion = qualityCriterion;
	}
}
