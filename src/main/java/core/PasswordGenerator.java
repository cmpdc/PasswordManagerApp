package core;

import core.utils.Constants;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PasswordGenerator {
    private boolean isUpperIncluded;
    private boolean isLowerIncluded;
    private boolean isNumIncluded;
    private boolean isSpecialCharsIncluded;
    private int passLength;

    public PasswordGenerator(){}

    public PasswordGenerator(int passLength,boolean useUpper, boolean useLower, boolean useNums, boolean useSymbols){
        this.setPassLength(passLength);
        this.setUpperIncluded(useUpper);
        this.setLowerIncluded(useLower);
        this.setNumIncluded(useNums);
        this.setSpecialCharsIncluded(useSymbols);
    }

    public boolean isUpperIncluded() {return isUpperIncluded;}
    public boolean isLowerIncluded() {return isLowerIncluded;}
    public boolean isNumIncluded() {return isNumIncluded;}
    public boolean isSpecialCharsIncluded() {return isSpecialCharsIncluded;}
    public int getPassLength() {return passLength;}

    public void setUpperIncluded(boolean upperIncluded) {isUpperIncluded = upperIncluded;}
    public void setLowerIncluded(boolean lowerIncluded) {isLowerIncluded = lowerIncluded;}
    public void setNumIncluded(boolean numIncluded) {isNumIncluded = numIncluded;}
    public void setSpecialCharsIncluded(boolean specialCharsIncluded) {isSpecialCharsIncluded = specialCharsIncluded;}
    public void setPassLength(int passLength) {this.passLength = passLength;}


    public String generatePassword(){

        // check for the length
        if (passLength <= 0) return "";

        StringBuilder password = new StringBuilder(passLength);
        Random random = new Random();

        int passwordCategoriesNum = 4;
        List<String> passwordSpecifics = new ArrayList<>(passwordCategoriesNum);

        if (isUpperIncluded()) passwordSpecifics.add(Constants.UPPERCASES);
        if (isLowerIncluded()) passwordSpecifics.add(Constants.LOWERCASES);
        if (isNumIncluded()) passwordSpecifics.add(Constants.DIGITS);
        if (isSpecialCharsIncluded()) passwordSpecifics.add(Constants.PUNCS);

        for (int i = 0; i < passLength; i++){

            // randomize each character inside the arrayList
            String passCharCategory = passwordSpecifics.get(random.nextInt(passwordSpecifics.size()));
            int index = random.nextInt(passCharCategory.length());

            password.append(passCharCategory.charAt(index));
        }

        return new String(password);
    }

    /*
        Encrypting/Decrypting - all credits to https://www.baeldung.com/java-cipher-class
        and https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/
        Both should be same in aspect
        NOTE: Master key and generated salt should be the same when encrypting and decrypting
    */

    public String encodePassword(String generatedPassword, String givenMasterKey, String generatedSalt){

        PBEKeySpec keySpec = new PBEKeySpec(
                givenMasterKey.toCharArray(),
                generatedSalt.getBytes(),
                Constants.PASSWORD_KEY_ITERATIONS,
                Constants.PASSWORD_KEY_LENGTH
        );

        try{

            // instantiating a secret hash keys that will be produced in large bits
            SecretKeyFactory secretKeys = SecretKeyFactory.getInstance(Constants.PASSWORD_KEY_FACTORY_ALGORITHM);
            SecretKey tempKey = secretKeys.generateSecret(keySpec);
            SecretKeySpec generatedSecretKey = new SecretKeySpec(tempKey.getEncoded(), Constants.PASSWORD_KEY_ALGORITHM);

            Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generatedSecretKey, new IvParameterSpec(new byte[16]));

            byte[] passwordByte = generatedPassword.getBytes(StandardCharsets.UTF_8);
            return Base64.getUrlEncoder().encodeToString(cipher.doFinal(passwordByte));

        }

        catch (Exception e) {
            throw new AssertionError("Error while encrypting: " + e.getMessage(), e);
        }

        finally {
            keySpec.clearPassword();
        }
    }

    public String decryptPassword(String encodedPassword, String givenMasterKey, String generatedSalt){
        PBEKeySpec keySpec = new PBEKeySpec(
                givenMasterKey.toCharArray(),
                generatedSalt.getBytes(),
                Constants.PASSWORD_KEY_ITERATIONS,
                Constants.PASSWORD_KEY_LENGTH
        );

        try{
            SecretKeyFactory secretKeys = SecretKeyFactory.getInstance(Constants.PASSWORD_KEY_FACTORY_ALGORITHM);
            SecretKey tempKey = secretKeys.generateSecret(keySpec);
            SecretKeySpec generatedSecretKey = new SecretKeySpec(tempKey.getEncoded(), Constants.PASSWORD_KEY_ALGORITHM);

            Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generatedSecretKey, new IvParameterSpec(new byte[16]));

            byte[] passwordByte = encodedPassword.getBytes(StandardCharsets.UTF_8);

            return new String(cipher.doFinal(Base64.getUrlDecoder().decode(passwordByte)));
        }

        catch (Exception e) {
            throw new AssertionError("Error while decrypting: " + e.getMessage(), e);
        }

        finally {
            keySpec.clearPassword();
        }
    }

    public boolean isPasswordVerified(String password, String encodedPassword, String masterKey, String saltKey){

        // Generate new secure password with the same key
        String newEncodedPassword = this.encodePassword(password, masterKey, saltKey);

        // Validate if both passed password value and new generated secure password value are equal
        return newEncodedPassword.equalsIgnoreCase(encodedPassword);
    }





}
