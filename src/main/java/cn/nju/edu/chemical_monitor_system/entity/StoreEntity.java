package cn.nju.edu.chemical_monitor_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Store", schema = "mydb")
public class StoreEntity {
    private int storeId;
    private int enterpriseId;
    private String name;
    private List<ExpressEntity> inExpressEntities;//该仓库作为入库所绑定的清单
    private List<ExpressEntity> outExpressEntities;//该仓库作为出库所绑定的清单
    private String port="8088";

    @Id
    @Column(name = "Store_id")
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Basic
    @Column(name = "Enterprise_id")
    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Basic
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inputStore", fetch = FetchType.LAZY)
    @JsonBackReference
    public List<ExpressEntity> getInExpressEntities() {
        return inExpressEntities;
    }

    public void setInExpressEntities(List<ExpressEntity> inExpressEntities) {
        this.inExpressEntities = inExpressEntities;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "outputStore", fetch = FetchType.LAZY)
    @JsonBackReference
    public List<ExpressEntity> getOutExpressEntities() {
        return outExpressEntities;
    }

    public void setOutExpressEntities(List<ExpressEntity> outExpressEntities) {
        this.outExpressEntities = outExpressEntities;
    }


    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoreEntity that = (StoreEntity) o;

        if (storeId != that.storeId) return false;
        if (enterpriseId != that.enterpriseId) return false;
        if (!Objects.equals(name, that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = storeId;
        result = 31 * result + enterpriseId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
