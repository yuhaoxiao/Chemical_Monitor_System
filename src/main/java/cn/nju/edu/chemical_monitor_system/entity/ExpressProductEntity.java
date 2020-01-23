package cn.nju.edu.chemical_monitor_system.entity;

import javax.persistence.*;

@Entity
@Table(name = "ExpressProduct", schema = "mydb")
public class ExpressProductEntity {
    private int expressProductId;
    private int expressId;
    private int productId;
    private Double number;

    @Id
    @Column(name = "ExpressProduct_id")
    public int getExpressProductId() {
        return expressProductId;
    }

    public void setExpressProductId(int expressProductId) {
        this.expressProductId = expressProductId;
    }

    @Basic
    @Column(name = "Express_id")
    public int getExpressId() {
        return expressId;
    }

    public void setExpressId(int expressId) {
        this.expressId = expressId;
    }

    @Basic
    @Column(name = "Product_id")
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

        ExpressProductEntity that = (ExpressProductEntity) o;

        if (expressProductId != that.expressProductId) return false;
        if (expressId != that.expressId) return false;
        if (productId != that.productId) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = expressProductId;
        result = 31 * result + expressId;
        result = 31 * result + productId;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }
}
