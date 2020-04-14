package cn.nju.edu.chemical_monitor_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "store_product", schema = "mydb")
public class StoreProductEntity {
    private int id;
    private StoreEntity storeEntity;
    private ProductEntity productEntity;
    private Double number;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Basic
    @Column(name = "number")
    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    @ManyToOne
    @JoinColumn(name = "productid")
    @JsonBackReference(value = "productEntity")
    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    @ManyToOne
    @JoinColumn(name = "storeid")
    @JsonBackReference(value = "storeEntity")
    public StoreEntity getStoreEntity() {
        return storeEntity;
    }

    public void setStoreEntity(StoreEntity storeEntity) {
        this.storeEntity = storeEntity;
    }
}
