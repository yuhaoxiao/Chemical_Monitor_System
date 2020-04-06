package cn.nju.edu.chemical_monitor_system.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Enterprise", schema = "mydb")
public class EnterpriseEntity {
    private int enterpriseId;
    private String name;
    private int enable;

    @Id
    @Column(name = "Enterprise_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnterpriseEntity that = (EnterpriseEntity) o;

        if (enterpriseId != that.enterpriseId) return false;
        if (!Objects.equals(name, that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = enterpriseId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
