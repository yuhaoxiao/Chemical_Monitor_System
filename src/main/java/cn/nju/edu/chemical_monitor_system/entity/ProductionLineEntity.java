package cn.nju.edu.chemical_monitor_system.entity;

import javax.persistence.*;

@Entity
@Table(name = "ProductionLine", schema = "mydb")
public class ProductionLineEntity {
    private int productionLineId;
    private int enterpriseId;

    @Id
    @Column(name = "ProductionLine_id")
    public int getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(int productionLineId) {
        this.productionLineId = productionLineId;
    }

    @Basic
    @Column(name = "Enterprise_id")
    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductionLineEntity that = (ProductionLineEntity) o;

        if (productionLineId != that.productionLineId) return false;
        if (enterpriseId != that.enterpriseId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = productionLineId;
        result = 31 * result + enterpriseId;
        return result;
    }
}
