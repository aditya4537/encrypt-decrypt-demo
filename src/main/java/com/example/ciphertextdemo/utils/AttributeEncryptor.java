package com.example.ciphertextdemo.utils;

import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final String AES = "AES/ECB/PKCS5Padding";
    private static final String SECRET = "PusZhVfmRE3BnaFc"; // Your 16-character key

    private final Key key;
    private final Cipher cipher;

    public AttributeEncryptor() throws Exception {
        key = new SecretKeySpec(SECRET.getBytes(), "AES");
        cipher = Cipher.getInstance(AES);
    }

    /**
     * Associated encryption function for postgresql
     * CREATE OR REPLACE FUNCTION encrypt_aes(data TEXT, secret TEXT)
     *     RETURNS TEXT AS $$
     * DECLARE
     *     key BYTEA = decode(secret, 'escape');
     * BEGIN
     *     IF data = '' THEN
     *         RETURN data;
     *     ELSE
     *         RETURN replace(encode(encrypt(data::bytea, key, 'aes-ecb/pad:pkcs'), 'base64'), E'\n', '');
     *     END IF;
     * END;
     * $$ LANGUAGE plpgsql;
     * @param attribute the entity attribute value to be converted
     * @return encrypted data by AES algorithm
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            if(attribute == null || attribute.isEmpty())
                return attribute;
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(attribute.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Error encrypting", e);
        }
    }

    /**
     * Associated decryption function for postgresql
     * CREATE OR REPLACE FUNCTION decrypt_aes(encrypted_data TEXT, secret TEXT)
     *     RETURNS TEXT AS $$
     * DECLARE
     *     key BYTEA = decode(secret, 'escape');
     * BEGIN
     *     IF encrypted_data = '' THEN
     *         RETURN encrypted_data;
     *     ELSE
     *         RETURN convert_from(decrypt(decode(encrypted_data, 'base64'), key, 'aes-ecb/pad:pkcs'), 'UTF8');
     *     END IF;
     * END;
     * $$ LANGUAGE plpgsql;
     * @param dbData the data from the database column to be converted
     * @return decrypted data by AES algorithm
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            if(dbData == null || dbData.isEmpty())
                return dbData;
            // Check if the data is already decrypted
            if (isBase64(dbData)) {
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(dbData));
                return new String(decryptedBytes);
            } else {
                // Data is already decrypted, return as is
                return dbData;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error decrypting", e);
        }
    }

    private boolean isBase64(String str) {

        try {
            // Check if the string matches the Base64 pattern
            if (!str.matches("^[A-Za-z0-9+/]*={0,2}$")) {
                return false;
            }

            // Attempt to decode
            byte[] decodedBytes = Base64.getDecoder().decode(str);

            // Check if the decoded string can be encoded back to the original string
            String reEncoded = Base64.getEncoder().encodeToString(decodedBytes);
            return str.equals(reEncoded);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}