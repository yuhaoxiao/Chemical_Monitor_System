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
    private UserEntity inputUser;
    private UserEntity outputUser;
    private StoreEntity inputStore;
    private StoreEntity outputStore;
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

    @ManyToOne
    @JoinColumn(name = "Output_User_id")
    @JsonBackReference
    public UserEntity getOutputUser() {
        return outputUser;
    }

    public void setOutputUser(UserEntity outputUser) {
        this.outputUser = outputUser;
    }

    @ManyToOne
    @JoinColumn(name = "Input_User_id")
    @JsonBackReference
    public UserEntity getInputUser() {
        return inputUser;
    }

    public void setInputUser(UserEntity inputUser) {
        this.inputUser = inputUser;
    }

    @ManyToOne
    @JoinColumn(name = "Input_Store_id")
    @JsonBackReference
    public StoreEntity getInputStore() {
        return inputStore;
    }

    public void setInputStore(StoreEntity inputStore) {
        this.inputStore = inputStore;
    }


    @ManyToOne
    @JoinColumn(name = "Output_Store_id")
    @JsonBackReference
    public StoreEntity getOutputStore() {
        return outputStore;
    }

    public void setOutputStore(StoreEntity outputStore) {
        this.outputStore = outputStore;
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expressEntity", fetch = FetchType.LAZY)
    @JsonBackReference
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
        if (inputUser.getUserId() != that.getInputUser().getUserId()) return false;
        if (outputUser.getUserId() != that.getOutputUser().getUserId()) return false;
        if (inputStore.getStoreId() != that.getInputStore().getStoreId()) return false;
        if (outputStore.getStoreId() != that.getOutputStore().getStoreId()) return false;
        if (!Objects.equals(outputTime, that.outputTime)) return false;
        if (!Objects.equals(inputTime, that.inputTime)) return false;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = expressId;
        result = 31 * result + (outputTime != null ? outputTime.hashCode() : 0);
        result = 31 * result + (inputTime != null ? inputTime.hashCode() : 0);
        result = 31 * result + (status ==0 ? Integer.valueOf(status).hashCode() : 0);
        result = 31 * result + inputUser.getUserId();
        result = 31 * result + outputUser.getUserId();
        result = 31 * result + inputStore.getStoreId();
        result = 31 * result + outputStore.getStoreId();
        return result;
    }
}
