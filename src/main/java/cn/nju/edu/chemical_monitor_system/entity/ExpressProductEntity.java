package cn.nju.edu.chemical_monitor_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "expressproduct", schema = "mydb")
public class ExpressProductEntity {
    private int expressProductId;
    private ProductEntity productEntity;
    private double number;
    private ExpressEntity expressEntity;
    private int Status;
    private double inputNumber;
    private double outputNumber;

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
    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    @Basic
    @Column(name = "inputnumber")
    public double getInputNumber() {
        return inputNumber;
    }

    public void setInputNumber(double inputNumber) {
        this.inputNumber = inputNumber;
    }

    @Basic
    @Column(name = "outputnumber")
    public double getOutputNumber() {
        return outputNumber;
    }

    public void setOutputNumber(double outputNumber) {
        this.outputNumber = outputNumber;
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
        return expressProductId == that.expressProductId &&
                Double.compare(that.number, number) == 0 &&
                Status == that.Status &&
                Double.compare(that.inputNumber, inputNumber) == 0 &&
                Double.compare(that.outputNumber, outputNumber) == 0 &&
                Objects.equals(productEntity, that.productEntity) &&
                Objects.equals(expressEntity, that.expressEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressProductId, productEntity, number, expressEntity, Status, inputNumber, outputNumber);
    }
}
