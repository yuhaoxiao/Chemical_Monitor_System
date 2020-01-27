package cn.nju.edu.chemical_monitor_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CAS", schema = "mydb")
public class CasEntity {
    private int casId;
    private String name;
    private List<ProductEntity> productEntities;

    @Id
    @Column(name = "CAS_id")
    public int getCasId() {
        return casId;
    }

    public void setCasId(int casId) {
        this.casId = casId;
    }

    @Basic
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "casEntity", fetch = FetchType.LAZY)
    @JsonBackReference
    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CasEntity casEntity = (CasEntity) o;

        if (casId != casEntity.casId) return false;
        if (!Objects.equals(name, casEntity.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = casId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
