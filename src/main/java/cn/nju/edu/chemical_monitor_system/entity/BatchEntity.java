package cn.nju.edu.chemical_monitor_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Batch", schema = "mydb")
public class BatchEntity {
    private int batchId;
    private int productionLineId;
    private Timestamp time;
    private String status;
    private UserEntity userEntity;
    private String type;

    @Id
    @Column(name = "Batch_id")
    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    @Basic
    @Column(name = "Productionline_id")
    public int getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(int productionLineId) {
        this.productionLineId = productionLineId;
    }

    @Basic
    @Column(name = "Time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "Type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "User_id")
    @JsonBackReference(value = "UserEntity")
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BatchEntity that = (BatchEntity) o;

        if (batchId != that.batchId) return false;
        if (productionLineId != that.productionLineId) return false;
        if (userEntity.getUserId() != that.getUserEntity().getUserId()) return false;
        if (!Objects.equals(time, that.time)) return false;
        if (!Objects.equals(status, that.status)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = batchId;
        result = 31 * result + productionLineId;
        result = 31 * result + userEntity.getUserId();
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
