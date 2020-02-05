package cn.nju.edu.chemical_monitor_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "expressproduct", schema = "mydb")
public class ExpressProductEntity {
    private int expressProductId;
    private ProductEntity productEntity;
    private Double number;
    private ExpressEntity expressEntity;
    private int Status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Expressproduct_id")
    public int getExpressProductId() {
        return expressProductId;
    }

    public void setExpressProductId(int expressProductId) {
        this.expressProductId = expressProductId;
    }

    @ManyToOne
    @JoinColumn(name = "Product_id")
    @JsonBackReference
    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    @Basic
    @Column(name = "Number")
    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    @ManyToOne
    @JoinColumn(name = "Express_id")
    @JsonBackReference
    public ExpressEntity getExpressEntity() {
        return expressEntity;
    }

    public void setExpressEntity(ExpressEntity expressEntity) {
        this.expressEntity = expressEntity;
    }

    @Basic
    @Column(name = "Status")
    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressProductEntity that = (ExpressProductEntity) o;

        if (expressProductId != that.expressProductId) return false;
        if (expressEntity.getExpressId() != that.getExpressEntity().getExpressId()) return false;
        if (productEntity.getProductId() != that.getProductEntity().getProductId()) return false;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        int result = expressProductId;
        result = 31 * result + expressEntity.getExpressId();
        result = 31 * result + productEntity.getProductId();
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }
}
