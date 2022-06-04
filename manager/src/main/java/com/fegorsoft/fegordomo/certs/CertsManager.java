package com.fegorsoft.fegordomo.certs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.fegorsoft.fegordomo.utils.HashUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.text.RandomStringGenerator;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
@EnableScheduling
public class CertsManager implements CryptographicService {
    private static final Logger log = LoggerFactory.getLogger(CertsManager.class);

    private static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SHA256_WITH_RSA_ALGORITHM = "SHA256withRSA";
    private static final int RSA_KEYSIZE = 2048;
    private static final int AES_KEYSIZE = 256;

    private Cipher cipherRSA;
    private Cipher cipherAES;
    private KeyStore keystore = null;

    // Para RSA
    private PrivateKey privateKey;
    private PublicKey publicKey;

    // Para AES
    private SecretKey key;

    public CertsManager() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipherRSA = Cipher.getInstance(RSA_ALGORITHM);
        cipherAES = Cipher.getInstance(AES_ALGORITHM);
    }

    public String plainToMD5(String message) {
        return this.messageDigest(message, MessageDigestAlgorithms.MD5);
    }

    public String plainToSHA256(String message) {
        return this.messageDigest(message, MessageDigestAlgorithms.SHA_256);
    }

    /**
     * @param message
     * @param algorithm
     * @return
     */
    private String messageDigest(String message, String algorithm) {
        String result = null;
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance(algorithm);

        } catch (NoSuchAlgorithmException e) {
            log.error("Message digest error!, ", e);
        }

        if (md != null) {
            md.update(message.getBytes());
            byte[] digest = md.digest();
            byte[] encoded = Base64.encodeBase64(digest);
            result = new String(encoded);
        }

        return result;
    }

    public void generateRSAKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(RSA_KEYSIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public byte[] encryptRSA(String text, PublicKey publicKey)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipherRSA.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipherRSA.doFinal(text.getBytes());
    }

    public String decryptRSA(byte[] encrypt, PrivateKey privateKey)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipherRSA.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipherRSA.doFinal(encrypt));
    }

    public PublicKey loadPublicKey(String fileName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fis = null;
        byte[] bytes;

        try {
            fis = new FileInputStream(fileName);
            int numBtyes = fis.available();
            bytes = new byte[numBtyes];
            fis.read(bytes);

        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpec = new X509EncodedKeySpec(bytes);
        this.publicKey = keyFactory.generatePublic(keySpec);

        return this.publicKey;
    }

    public PrivateKey loadPrivateKey(String fileName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fis = null;
        byte[] bytes;

        try {
            fis = new FileInputStream(fileName);
            int numBtyes = fis.available();
            bytes = new byte[numBtyes];
            fis.read(bytes);

        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        this.privateKey = keyFactory.generatePrivate(keySpec);

        return this.privateKey;
    }

    public void saveKey(Key key, String fileName) throws IOException {
        FileOutputStream fos = null;
        byte[] publicKeyBytes = key.getEncoded();

        try {
            fos = new FileOutputStream(fileName);
            fos.write(publicKeyBytes);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public byte[] sign(String text, PrivateKey privateKey) {
        return sign(text.getBytes(), privateKey);
    }

    public byte[] sign(byte[] source, PrivateKey privateKey) {
        Signature signature;
        byte[] signed = null;

        try {
            signature = Signature.getInstance(SHA256_WITH_RSA_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(source);
            signed = signature.sign();

        } catch (NoSuchAlgorithmException e) {
            log.error("Error in sign algorithm: {}", e);

        } catch (InvalidKeyException e) {
            log.error("Error in key: {}", e);

        } catch (SignatureException e) {
            log.error("Error in signature: {}", e);
        }

        return signed;
    }

    public boolean verifySign(String text, byte[] textSigned, PublicKey publicKey) {
        return verifySign(text.getBytes(), textSigned, publicKey);
    }

    public boolean verifySign(byte[] original, byte[] signed, PublicKey publicKey) {
        Signature signature;
        boolean verify = false;

        try {
            signature = Signature.getInstance(SHA256_WITH_RSA_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(original);
            verify = signature.verify(signed);

        } catch (NoSuchAlgorithmException e) {
            log.error("Error in sign algorithm: {}", e);

        } catch (InvalidKeyException e) {
            log.error("Error in key: {}", e);

        } catch (SignatureException e) {
            log.error("Error in signature: {}", e);
        }

        return verify;
    }

    public void generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEYSIZE);
        key = keyGenerator.generateKey();
    }

    public void generateAESKey(String pwd) {
        this.key = new SecretKeySpec(pwd.getBytes(), "AES");
    }

    public void generateAESKeyPBKDF2(String pwd) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(pwd.toCharArray(), new byte[16], 65536, 128); // AES-256
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = f.generateSecret(spec).getEncoded();
        this.key = new SecretKeySpec(key, "AES");
    }

    public byte[] encryptAES(String text) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        try {
            cipherAES.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
        } catch (InvalidAlgorithmParameterException e) {
            log.error("Error in AES algorithm: {}", e);
        }

        return cipherAES.doFinal(text.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] encryptAES(byte[] content) {
        byte[] contentEncrypted = null;

        try {
            cipherAES.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));

        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            log.error("Error in AES algorithm or Key: {}", e);
        }

        try {
            contentEncrypted = cipherAES.doFinal(content);

        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("Error in block size or bad padding: {}", e);
        }

        return contentEncrypted;
    }

    public byte[] decryptAES(byte[] encrypt, SecretKey key)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        try {
            cipherAES.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));

        } catch (InvalidAlgorithmParameterException e) {
            log.error("Error in AES algorithm: {}", e);
        }

        return cipherAES.doFinal(encrypt);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getPrivateKeyBase64() {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    public String getPublicKeyBase64() {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    public SecretKey getKey() {
        return key;
    }

    public String getKeyBase64() {
        return Base64.encodeBase64String(key.getEncoded());
    }

    public HashMap<String, Certificate> listCertificatesFromKeyStore(String fileName, String pwd) {
        HashMap<String, Certificate> listCerts = new HashMap<>();

        if (this.keystore == null) {
            this.loadKeyStore(fileName, pwd);
        }

        Enumeration<String> enumeration = null;

        try {
            enumeration = keystore.aliases();

            while (enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                Certificate certificate = null;
                certificate = keystore.getCertificate(alias);
                listCerts.put(alias, certificate);
            }

        } catch (KeyStoreException e) {
            log.error("Exception error in KeyStore: {}", e);
        }

        return listCerts;
    }

    public KeyStore getKeystore() {
        return keystore;
    }

    public String generateRandomStringPassword(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange('0', 'z').build();
        return pwdGenerator.generate(length);
    }

    public void loadKeyStore(String fileName, String pwd) {
        InputStream is = null;

        try {
            Resource resource = new ClassPathResource(fileName);
            is = resource.getInputStream();
        } catch (Exception e) {
            log.error("KeyStore file not found!");
        }

        try {
            this.keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e1) {
            log.error("KeyStore exception!");
        }

        String password = pwd;

        try {
            keystore.load(is, password.toCharArray());
        } catch (NoSuchAlgorithmException | CertificateException | IOException e2) {
            log.error("No such algorithm, certificate or I/O exception!");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("Clossing error! {}", e);
            }
        }
    }

    public String computeHash(Object o) throws NoSuchAlgorithmException, IOException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(HashUtil.convertToBytes(o));
        return HashUtil.bytesToHex(md.digest());
    }

    public static Certificate selfSign(KeyPair keyPair, String subjectDN)
            throws OperatorCreationException, CertificateException, IOException {
        Provider bcProvider = new BouncyCastleProvider();
        Security.addProvider(bcProvider);

        long now = System.currentTimeMillis();
        Date startDate = new Date(now);

        X500Name dnName = new X500Name(subjectDN);
        BigInteger certSerialNumber = new BigInteger(Long.toString(now)); // <-- Using the current timestamp as the
                                                                          // certificate serial number

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, 1); // <-- 1 Yr validity

        Date endDate = calendar.getTime();

        String signatureAlgorithm = "SHA256WithRSA"; // <-- Use appropriate signature algorithm based on your keyPair
                                                     // algorithm.

        ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, certSerialNumber, startDate,
                endDate, dnName, keyPair.getPublic());

        // Extensions --------------------------

        // Basic Constraint
        BasicConstraints basicConstraints = new BasicConstraints(true); // <-- true for CA, false for EndEntity

        certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), true, basicConstraints); // Basic Constraints is
                                                                                                 // usually marked as
                                                                                                 // critical.

        // -------------------------------------

        return new JcaX509CertificateConverter().setProvider(bcProvider)
                .getCertificate(certBuilder.build(contentSigner));
    }
}
