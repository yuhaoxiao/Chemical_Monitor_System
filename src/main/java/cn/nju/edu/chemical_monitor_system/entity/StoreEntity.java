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
    private String port;
    private int enable;
    private List<StoreProductEntity> storeProductEntities;
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

    @Basic
    @Column(name = "Enable")
    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    @Basic
    @Column(name = "Port")
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storeEntity", fetch = FetchType.LAZY)
    @JsonBackReference(value = "storeProductEntities")
    public List<StoreProductEntity> getStoreProductEntities() {
        return storeProductEntities;
    }
    public void setStoreProductEntities(List<StoreProductEntity> storeProductEntities) {
        this.storeProductEntities = storeProductEntities;
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
