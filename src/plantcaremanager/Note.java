/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plantcaremanager;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 *
 * @author kelvi
 */
public class Note {
    private String heading;
    private String content;
    private String password;
    private String encryptedContent;
    private String encryptedPassword;
    private boolean isDecrypted;

    public Note(String heading, String encryptedContent, String encryptedPassword) {
        try {
            this.heading = heading;
            this.encryptedContent = encryptedContent;
            this.encryptedPassword = encryptedPassword;
            
            password = decryptString(this.encryptedPassword);
            content = decryptString(this.encryptedContent);
            isDecrypted = false;
            
        } catch(InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public Note(String heading) {
        this.heading = heading;
        password = "1234";
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        try {
            encryptedContent = encryptString(this.content);
        } catch(InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        try {
            encryptedPassword = encryptString(this.password);
        } catch(InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getEncryptedContent() {
        return encryptedContent;
    }

    public void setEncryptedContent(String encryptedContent) {
        this.encryptedContent = encryptedContent;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public boolean getIsDecrypted() {
        return isDecrypted;
    }

    public void setIsDecrypted(boolean isDecrypted) {
        this.isDecrypted = isDecrypted;
    }

    
    //borrowed code  - https://github.com/luke-park/SecureCompatibleEncryptionExamples/blob/master/Java/SCEE.java
    public static String encryptString(String plaintext) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecureRandom rand = new SecureRandom();
        byte[] salt = new byte[16];
        rand.nextBytes(salt);
        PBEKeySpec pwSpec = new PBEKeySpec("6X9*BpRusmWvu@etTtv0".toCharArray(), salt, 32767, 128);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] key = keyFactory.generateSecret(pwSpec).getEncoded();
        byte[] ciphertextAndNonce = encrypt(plaintext.getBytes(StandardCharsets.UTF_8), key);
        byte[] ciphertextAndNonceAndSalt = new byte[salt.length + ciphertextAndNonce.length];
        System.arraycopy(salt, 0, ciphertextAndNonceAndSalt, 0, salt.length);
        System.arraycopy(ciphertextAndNonce, 0, ciphertextAndNonceAndSalt, salt.length, ciphertextAndNonce.length);
        return Base64.getEncoder().encodeToString(ciphertextAndNonceAndSalt);
    }
    
    //borrowed code  - https://github.com/luke-park/SecureCompatibleEncryptionExamples/blob/master/Java/SCEE.java
    public static String decryptString(String base64CiphertextAndNonceAndSalt) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        byte[] ciphertextAndNonceAndSalt = Base64.getDecoder().decode(base64CiphertextAndNonceAndSalt);
        byte[] salt = new byte[16];
        byte[] ciphertextAndNonce = new byte[ciphertextAndNonceAndSalt.length - 16];
        System.arraycopy(ciphertextAndNonceAndSalt, 0, salt, 0, salt.length);
        System.arraycopy(ciphertextAndNonceAndSalt, salt.length, ciphertextAndNonce, 0, ciphertextAndNonce.length);
        PBEKeySpec pwSpec = new PBEKeySpec("6X9*BpRusmWvu@etTtv0".toCharArray(), salt, 32767, 128);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] key = keyFactory.generateSecret(pwSpec).getEncoded();
        return new String(decrypt(ciphertextAndNonce, key), StandardCharsets.UTF_8);
    }

    //borrowed code  - https://github.com/luke-park/SecureCompatibleEncryptionExamples/blob/master/Java/SCEE.java
    public static byte[] encrypt(byte[] plaintext, byte[] key) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecureRandom rand = new SecureRandom();
        byte[] nonce = new byte[12];
        rand.nextBytes(nonce);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, nonce));
        byte[] ciphertext = cipher.doFinal(plaintext);
        byte[] ciphertextAndNonce = new byte[nonce.length + ciphertext.length];
        System.arraycopy(nonce, 0, ciphertextAndNonce, 0, nonce.length);
        System.arraycopy(ciphertext, 0, ciphertextAndNonce, nonce.length, ciphertext.length);
        return ciphertextAndNonce;
    }

    //borrowed code  - https://github.com/luke-park/SecureCompatibleEncryptionExamples/blob/master/Java/SCEE.java
    public static byte[] decrypt(byte[] ciphertextAndNonce, byte[] key) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] nonce = new byte[12];
        byte[] ciphertext = new byte[ciphertextAndNonce.length - 12];
        System.arraycopy(ciphertextAndNonce, 0, nonce, 0, nonce.length);
        System.arraycopy(ciphertextAndNonce, nonce.length, ciphertext, 0, ciphertext.length);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, nonce));
        return cipher.doFinal(ciphertext);
    }
    
    public String toString() {
        String passwords = encryptedPassword;
        String contents = encryptedContent;
        if (encryptedPassword.isBlank()) {
            passwords = "empty";
        } if (encryptedContent.isBlank()) {
            contents = "empty";
        }
        return heading + ";;" + contents + ";;" + passwords;
    }
    
}
