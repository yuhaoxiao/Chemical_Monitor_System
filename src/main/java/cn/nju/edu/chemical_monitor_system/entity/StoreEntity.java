package cn.nju.edu.chemical_monitor_system.entity;

import javax.persistence.*;

@Entity
@Table(name = "Store", schema = "mydb")
public class StoreEntity {
    private int storeId;
    private int enterpriseId;
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoreEntity that = (StoreEntity) o;

        if (storeId != that.storeId) return false;
        if (enterpriseId != that.enterpriseId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

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
