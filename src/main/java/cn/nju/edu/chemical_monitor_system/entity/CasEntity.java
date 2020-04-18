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
    private double fusionPoint;//熔点
    private double boilingPoint;//沸点
    private int existType;//存在形式 0固体1液体2气体
    private int isOrganic;//0有机物1无机物
    private int oxidation;//氧化性
    private int reducibility;//还原性
    private int inflammability;//易燃性
    private int explosion;//易爆性

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
    @JsonBackReference(value = "ProductEntities")
    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    @Basic
    @Column(name = "Fusionpoint")
    public double getFusionPoint() {
        return fusionPoint;
    }

    public void setFusionPoint(double fusionPoint) {
        this.fusionPoint = fusionPoint;
    }

    @Basic
    @Column(name = "Boilingpoint")
    public double getBoilingPoint() {
        return boilingPoint;
    }

    public void setBoilingPoint(double boilingPoint) {
        this.boilingPoint = boilingPoint;
    }

    @Basic
    @Column(name = "Existtype")
    public int getExistType() {
        return existType;
    }

    public void setExistType(int existType) {
        this.existType = existType;
    }

    @Basic
    @Column(name = "Isorganic")
    public int getIsOrganic() {
        return isOrganic;
    }

    public void setIsOrganic(int isOrganic) {
        this.isOrganic = isOrganic;
    }

    @Basic
    @Column(name = "Oxidation")
    public int getOxidation() {
        return oxidation;
    }

    public void setOxidation(int oxidation) {
        this.oxidation = oxidation;
    }

    @Basic
    @Column(name = "Reducibility")
    public int getReducibility() {
        return reducibility;
    }

    public void setReducibility(int reducibility) {
        this.reducibility = reducibility;
    }

    @Basic
    @Column(name = "Inflammability")
    public int getInflammability() {
        return inflammability;
    }

    public void setInflammability(int inflammability) {
        this.inflammability = inflammability;
    }

    @Basic
    @Column(name = "Explosion")
    public int getExplosion() {
        return explosion;
    }

    public void setExplosion(int explosion) {
        this.explosion = explosion;
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
