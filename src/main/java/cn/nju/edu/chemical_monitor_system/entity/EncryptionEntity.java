package cn.nju.edu.chemical_monitor_system.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "encryption", schema = "mydb")
public class EncryptionEntity {
    private int id;
    private Integer inputStoreId;
    private Integer outputStoreId;
    private String publicKey;
    private String privateKey;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "InputStore_id")
    public Integer getInputStoreId() {
        return inputStoreId;
    }

    public void setInputStoreId(Integer inputStoreId) {
        this.inputStoreId = inputStoreId;
    }

    @Basic
    @Column(name = "OutputStore_id")
    public Integer getOutputStoreId() {
        return outputStoreId;
    }

    public void setOutputStoreId(Integer outputStoreId) {
        this.outputStoreId = outputStoreId;
    }

    @Basic
    @Column(name = "PublicKey")
    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Basic
    @Column(name = "PrivateKey")
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EncryptionEntity that = (EncryptionEntity) o;

        if (id != that.id) return false;
        if (!Objects.equals(inputStoreId, that.inputStoreId)) return false;
        if (!Objects.equals(outputStoreId, that.outputStoreId))
            return false;
        if (!Objects.equals(publicKey, that.publicKey)) return false;
        if (!Objects.equals(privateKey, that.privateKey)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (inputStoreId != null ? inputStoreId.hashCode() : 0);
        result = 31 * result + (outputStoreId != null ? outputStoreId.hashCode() : 0);
        result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
        result = 31 * result + (privateKey != null ? privateKey.hashCode() : 0);
        return result;
    }
}
