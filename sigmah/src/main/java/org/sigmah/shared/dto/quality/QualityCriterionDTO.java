package org.sigmah.shared.dto.quality;

import java.util.List;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity quality.QualityCriterion.
 * 
 * @author tmi
 * 
 */
public class QualityCriterionDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -49281834964182785L;

    private transient CriterionTypeDTO type;

    @Override
    public String getEntityName() {
        return "quality.QualityCriterion";
    }

    // Criterion id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Criterion code.
    public String getCode() {
        return get("code");
    }

    public void setCode(String code) {
        set("code", code);
    }

    // Criterion label.
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    // Quality framework.
    public QualityFrameworkDTO getQualityFramework() {
        return get("qualityFramework");
    }

    public void setQualityFramework(QualityFrameworkDTO qualityFramework) {
        set("qualityFramework", qualityFramework);
    }

    // Criterion parent.
    public QualityCriterionDTO getParentCriterion() {
        return get("parentCriterion");
    }

    public void setParentCriterion(QualityCriterionDTO parentCriterion) {
        set("parentCriterion", parentCriterion);
    }

    // Criterion sub criteria.
    public List<QualityCriterionDTO> getSubCriteria() {
        return get("subCriteria");
    }

    public void setSubCriteria(List<QualityCriterionDTO> subCriteria) {
        set("subCriteria", subCriteria);
    }

    /**
     * Retrieves this criterion type.
     * 
     * @return The criterion type.
     */
    public CriterionTypeDTO getCriterionType() {

        if (type == null) {

            int level = 0;

            // Computes the position of this criterion in the hierarchy of the
            // quality framework.
            QualityCriterionDTO parent = getParentCriterion();
            QualityFrameworkDTO framework = null;
            while (parent != null) {
                level++;
                framework = parent.getQualityFramework();
                parent = parent.getParentCriterion();
            }

            assert framework != null;

            type = framework.getType(level);
        }

        return type;
    }

    /**
     * Returns the info of this criterion as a string.
     * 
     * @return The info string.
     */
    public String getInfo() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getCode());
        sb.append(" - ");
        sb.append(getLabel());
        sb.append(" (");
        final CriterionTypeDTO type = getCriterionType();
        if (type != null) {
            sb.append(getCriterionType().getLabel());
        } else {
            sb.append("...");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[QualityCriterionDTO] ");
        sb.append("code: ");
        sb.append(getCode());
        sb.append(" ; label: ");
        sb.append(getLabel());
        if (getSubCriteria() != null) {
            sb.append("\nchildren:");
            for (final QualityCriterionDTO child : getSubCriteria()) {
                sb.append("\n");
                sb.append(child);
            }
        }
        return sb.toString();
    }
}
