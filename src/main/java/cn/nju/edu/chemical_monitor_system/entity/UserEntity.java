package cn.nju.edu.chemical_monitor_system.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User", schema = "mydb")
public class UserEntity {
    private int userId;
    private String password;
    private String type;
    private List<BatchEntity> batchEntities;
    private List<ExpressEntity> inExpressEntities;//该操作员作为入库者所绑定的清单
    private List<ExpressEntity> outExpressEntities;//该操作员作为出库者所绑定的清单

    @Id
    @Column(name = "User_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "Type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity", fetch = FetchType.LAZY)
    @JsonBackReference
    public List<BatchEntity> getBatchEntities() {
        return batchEntities;
    }

    public void setBatchEntities(List<BatchEntity> batchEntities) {
        this.batchEntities = batchEntities;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inputUser", fetch = FetchType.LAZY)
    @JsonBackReference
    public List<ExpressEntity> getInExpressEntities() {
        return inExpressEntities;
    }

    public void setInExpressEntities(List<ExpressEntity> inExpressEntities) {
        this.inExpressEntities = inExpressEntities;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "outputUser", fetch = FetchType.LAZY)
    @JsonBackReference
    public List<ExpressEntity> getOutExpressEntities() {
        return outExpressEntities;
    }

    public void setOutExpressEntities(List<ExpressEntity> outExpressEntities) {
        this.outExpressEntities = outExpressEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (userId != that.userId) return false;
        if (!Objects.equals(password, that.password)) return false;
        if (!Objects.equals(type, that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
