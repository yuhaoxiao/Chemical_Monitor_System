package cn.nju.edu.chemical_monitor_system.entity;

import javax.persistence.*;

@Entity
@Table(name = "CAS", schema = "mydb")
public class CasEntity {
    private int casId;
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CasEntity casEntity = (CasEntity) o;

        if (casId != casEntity.casId) return false;
        if (name != null ? !name.equals(casEntity.name) : casEntity.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = casId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
