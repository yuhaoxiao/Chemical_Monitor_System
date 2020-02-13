package cn.nju.edu.chemical_monitor_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Product", schema = "mydb")
public class ProductEntity {
    private int productId;
    private int batchId;
    private CasEntity casEntity;
    private Double number;

    @Id
    @Column(name = "Product_id")
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "Batch_id")
    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    @ManyToOne
    @JoinColumn(name = "cas_id")
    @JsonBackReference
    public CasEntity getCasEntity() {
        return casEntity;
    }

    public void setCasEntity(CasEntity casEntity) {
        this.casEntity = casEntity;
    }

    @Basic
    @Column(name = "Number")
    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductEntity that = (ProductEntity) o;

        if (productId != that.productId) return false;
        if (batchId != that.batchId) return false;
        if (casEntity.getCasId() != that.getCasEntity().getCasId()) return false;
        if (!Objects.equals(number, that.number)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = productId;
        result = 31 * result + batchId;
        result = 31 * result + casEntity.getCasId();
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }
}
