package cn.nju.edu.chemical_monitor_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "productionline", schema = "mydb")
public class ProductionLineEntity {
    private int productionLineId;
    private EnterpriseEntity enterpriseEntity;
    private int enable;

    @Id
    @Column(name = "Productionline_id")
    public int getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(int productionLineId) {
        this.productionLineId = productionLineId;
    }

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    @JsonBackReference(value = "EnterpriseEntity")
    public EnterpriseEntity getEnterpriseEntity() {
        return enterpriseEntity;
    }

    public void setEnterpriseEntity(EnterpriseEntity enterpriseEntity) {
        this.enterpriseEntity = enterpriseEntity;
    }

    @Basic
    @Column(name = "Enable")
    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductionLineEntity that = (ProductionLineEntity) o;

        if (productionLineId != that.productionLineId) return false;
        if (enterpriseEntity.getEnterpriseId() != that.getEnterpriseEntity().getEnterpriseId()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = productionLineId;
        result = 31 * result + enterpriseEntity.getEnterpriseId();
        return result;
    }
}
