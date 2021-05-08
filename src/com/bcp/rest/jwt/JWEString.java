package com.bcp.rest.jwt;

import android.content.Context;
import com.newpos.libpay.Logger;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import java.security.SecureRandom;

public class JWEString {

    private GetKeys getKeys;
    private String dataEncrypt;
    private String dataDecrypt;

    public JWEString(boolean isInitTrans) {
        getKeys = new GetKeys(isInitTrans);
    }

    public void encryptedJWE(String data) throws Exception {

        // Create a new Json Web Encryption object
        JsonWebEncryption senderJwe = new JsonWebEncryption();

        // The plaintext of the JWE is the message that we want to encrypt.
        senderJwe.setPlaintext(data);

        // Set the "alg" header, which indicates the key management mode for this JWE.
        // In this example we are using the direct key management mode, which means
        // the given key will be used directly as the content encryption key.
        senderJwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);

        // Set the "enc" header, which indicates the content encryption algorithm to be used.
        // This example is using AES_128_CBC_HMAC_SHA_256 which is a composition of AES CBC
        // and HMAC SHA2 that provides authenticated encryption.
        senderJwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);

        // Set the key on the JWE. In this case, using direct mode, the key will used directly as
        // the content encryption key. AES_128_CBC_HMAC_SHA_256, which is being used to encrypt the
        // content requires a 256 bit key.
        senderJwe.setKey(getKeys.getPublicKey());

        SecureRandom iv = SecureRandom.getInstance("SHA1PRNG");
        senderJwe.setIv(iv.generateSeed(32));

        // Produce the JWE compact serialization, which is where the actual encryption is done.
        // The JWE compact serialization consists of five base64url encoded parts
        // combined with a dot ('.') character in the general format of
        // <header>.<encrypted key>.<initialization vector>.<ciphertext>.<authentication tag>
        // Direct encryption doesn't use an encrypted key so that field will be an empty string
        // in this case.
        dataEncrypt = senderJwe.getCompactSerialization();

        // Do something with the JWE. Like send it to some other party over the clouds
        // and through the interwebs.
        Logger.debug("JWE compact serialization: " + dataEncrypt);
    }

    public void decryptJWE(String data) throws Exception{
        // That other party, the receiver, can then use JsonWebEncryption to decrypt the message.
        JsonWebEncryption receiverJwe = new JsonWebEncryption();

        // Set the algorithm constraints based on what is agreed upon or expected from the sender
        AlgorithmConstraints algConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);
        receiverJwe.setAlgorithmConstraints(algConstraints);
        AlgorithmConstraints encConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
        receiverJwe.setContentEncryptionAlgorithmConstraints(encConstraints);

        SecureRandom iv = SecureRandom.getInstance("SHA1PRNG");
        receiverJwe.setIv(iv.generateSeed(32));

        // Set the compact serialization on new Json Web Encryption object
        receiverJwe.setCompactSerialization(data);

        // Symmetric encryption, like we are doing here, requires that both parties have the same key.
        // The key will have had to have been securely exchanged out-of-band somehow.
        receiverJwe.setKey(getKeys.getPrivateKey());

        // Get the message that was encrypted in the JWE. This step performs the actual decryption steps.
        dataDecrypt = receiverJwe.getPlaintextString();

        // And do whatever you need to do with the clear text message.
        Logger.debug("plaintext: " + dataDecrypt);
    }

    public String getDataEncrypt() {
        return dataEncrypt;
    }

    public String getDataDecrypt() {
        return dataDecrypt;
    }
}
