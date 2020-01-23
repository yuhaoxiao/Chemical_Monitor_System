package cn.nju.edu.chemical_monitor_system.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Express", schema = "mydb")
public class ExpressEntity {
    private int expressId;
    private Timestamp outputTime;
    private Timestamp inputTime;
    private Byte status;
    private int inputUserId;
    private int outputUserId;
    private int inputStoreId;
    private int outputStoreId;

    @Id
    @Column(name = "Express_id")
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
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "Input_User_id")
    public int getInputUserId() {
        return inputUserId;
    }

    public void setInputUserId(int inputUserId) {
        this.inputUserId = inputUserId;
    }

    @Basic
    @Column(name = "Output_User_id")
    public int getOutputUserId() {
        return outputUserId;
    }

    public void setOutputUserId(int outputUserId) {
        this.outputUserId = outputUserId;
    }

    @Basic
    @Column(name = "Input_Store_id")
    public int getInputStoreId() {
        return inputStoreId;
    }

    public void setInputStoreId(int inputStoreId) {
        this.inputStoreId = inputStoreId;
    }

    @Basic
    @Column(name = "Output_Store_id")
    public int getOutputStoreId() {
        return outputStoreId;
    }

    public void setOutputStoreId(int outputStoreId) {
        this.outputStoreId = outputStoreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressEntity that = (ExpressEntity) o;

        if (expressId != that.expressId) return false;
        if (inputUserId != that.inputUserId) return false;
        if (outputUserId != that.outputUserId) return false;
        if (inputStoreId != that.inputStoreId) return false;
        if (outputStoreId != that.outputStoreId) return false;
        if (outputTime != null ? !outputTime.equals(that.outputTime) : that.outputTime != null) return false;
        if (inputTime != null ? !inputTime.equals(that.inputTime) : that.inputTime != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = expressId;
        result = 31 * result + (outputTime != null ? outputTime.hashCode() : 0);
        result = 31 * result + (inputTime != null ? inputTime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + inputUserId;
        result = 31 * result + outputUserId;
        result = 31 * result + inputStoreId;
        result = 31 * result + outputStoreId;
        return result;
    }
}
