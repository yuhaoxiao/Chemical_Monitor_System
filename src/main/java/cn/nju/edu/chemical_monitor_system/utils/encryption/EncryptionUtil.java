package cn.nju.edu.chemical_monitor_system.utils.encryption;

import cn.nju.edu.chemical_monitor_system.dao.EncryptionDao;
import cn.nju.edu.chemical_monitor_system.entity.EncryptionEntity;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

@Component
public class EncryptionUtil {
    @Autowired
    EncryptionDao encryptionDao;

    private void genKeyPair(int inputStoreId, int outputStoreId) throws NoSuchAlgorithmException {
        Optional<EncryptionEntity> encryptionEntity = encryptionDao.findByInputStoreIdAndOutputStoreId(inputStoreId, outputStoreId);
        if (!encryptionEntity.isPresent()) {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
            String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
            EncryptionEntity newEncryption = new EncryptionEntity();
            newEncryption.setInputStoreId(inputStoreId);
            newEncryption.setOutputStoreId(outputStoreId);
            newEncryption.setPublicKey(publicKeyString);
            newEncryption.setPrivateKey(privateKeyString);
            encryptionDao.saveAndFlush(newEncryption);
        }
    }

    public String encrypt(String str, int inputStoreId, int outputStoreId) throws Exception {
        genKeyPair(inputStoreId, outputStoreId);
        String publicKey = encryptionDao.findByInputStoreIdAndOutputStoreId(inputStoreId, outputStoreId).get().getPublicKey();
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String str, int inputStoreId, int outputStoreId) throws Exception {
        genKeyPair(inputStoreId, outputStoreId);
        String privateKey = encryptionDao.findByInputStoreIdAndOutputStoreId(inputStoreId, outputStoreId).get().getPrivateKey();
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }
}
