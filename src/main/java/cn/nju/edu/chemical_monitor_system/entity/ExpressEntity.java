package cn.nju.edu.chemical_monitor_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Express", schema = "mydb")
public class ExpressEntity {
    private int expressId;
    private Timestamp outputTime;
    private Timestamp inputTime;
    private int status;
    private Integer inputUserId;
    private Integer outputUserId;
    private int inputStoreId;
    private int outputStoreId;
    private List<ExpressProductEntity> expressProductEntities;

    @Id
    @Column(name = "Express_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getExpressId() {
        return expressId;
    }

    public void setExpressId(int expressId) {
        this.expressId = expressId;
    }

    @Basic
    @Column(name = "Output_time")
    public Timestamp getOutputTime() {
        return outputTime;
    }

    public void setOutputTime(Timestamp outputTime) {
        this.outputTime = outputTime;
    }

    @Basic
    @Column(name = "Input_time")
    public Timestamp getInputTime() {
        return inputTime;
    }

    public void setInputTime(Timestamp inputTime) {
        this.inputTime = inputTime;
    }


    @Basic
    @Column(name = "Status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @JoinColumn(name = "Input_User_id")
    public Integer getInputUserId() {
        return inputUserId;
    }

    public void setInputUserId(Integer inputUserId) {
        this.inputUserId = inputUserId;
    }

    @JoinColumn(name = "Output_User_id")
    public Integer getOutputUserId() {
        return outputUserId;
    }

    public void setOutputUserId(Integer outputUserId) {
        this.outputUserId = outputUserId;
    }

    @JoinColumn(name = "Input_Store_id")
    public Integer getInputStoreId() {
        return inputStoreId;
    }

    public void setInputStoreId(Integer inputStoreId) {
        this.inputStoreId = inputStoreId;
    }

    @JoinColumn(name = "Output_Store_id")
    public int getOutputStoreId() {
        return outputStoreId;
    }

    public void setOutputStoreId(Integer outputStoreId) {
        this.outputStoreId = outputStoreId;
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expressEntity", fetch = FetchType.LAZY)
    @JsonBackReference(value = "ExpressProductEntities")
    public List<ExpressProductEntity> getExpressProductEntities() {
        return expressProductEntities;
    }

    public void setExpressProductEntities(List<ExpressProductEntity> expressProductEntities) {
        this.expressProductEntities = expressProductEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressEntity that = (ExpressEntity) o;

        if (expressId != that.expressId) return false;
        if (inputUserId != (that.getInputUserId())) return false;
        if (outputUserId != that.getOutputUserId()) return false;
        if (inputStoreId != that.getInputStoreId()) return false;
        if (outputStoreId != that.getOutputStoreId()) return false;
        if (!Objects.equals(outputTime, that.outputTime)) return false;
        if (!Objects.equals(inputTime, that.inputTime)) return false;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = expressId;
        result = 31 * result + (outputTime != null ? outputTime.hashCode() : 0);
        result = 31 * result + (inputTime != null ? inputTime.hashCode() : 0);
        result = 31 * result + (status == 0 ? Integer.valueOf(status).hashCode() : 0);
        result = 31 * result + inputUserId;
        result = 31 * result + outputUserId;
        result = 31 * result + inputStoreId;
        result = 31 * result + outputStoreId;
        return result;
    }
}
