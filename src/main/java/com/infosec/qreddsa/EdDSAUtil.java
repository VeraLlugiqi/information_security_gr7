package com.infosec.qreddsa;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * Klasa utility për operacionet kriptografike EdDSA (Ed25519):
 * gjenerimi i çelësave, nënshkrimi dhe verifikimi
 */
public class EdDSAUtil {
    
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Gjeneron një çift çelësash EdDSA (Ed25519)
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("Ed25519", "BC");
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Nënshkron të dhëna duke përdorur EdDSA me çelësin privat
     */
    public static byte[] sign(String data, PrivateKey privateKey) 
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {
        if (data == null) {
            throw new IllegalArgumentException("Të dhënat për nënshkrim nuk mund të jenë null");
        }
        if (privateKey == null) {
            throw new IllegalArgumentException("Çelësi privat nuk mund të jetë null");
        }
        
        Signature signature = Signature.getInstance("Ed25519", "BC");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

    /**
     * Verifikon një nënshkrim duke përdorur EdDSA me çelësin publik
     */
    public static boolean verify(String data, byte[] signatureBytes, PublicKey publicKey) 
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {
        if (data == null) {
            throw new IllegalArgumentException("Të dhënat për verifikim nuk mund të jenë null");
        }
        if (signatureBytes == null || signatureBytes.length == 0) {
            throw new IllegalArgumentException("Nënshkrimi nuk mund të jetë null ose bosh");
        }
        if (publicKey == null) {
            throw new IllegalArgumentException("Çelësi publik nuk mund të jetë null");
        }
        
        Signature signature = Signature.getInstance("Ed25519", "BC");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(signatureBytes);
    }

    public static String publicKeyToBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public static String signatureToBase64(byte[] signature) {
        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * Rikonstrukton një çelës publik nga një string Base64
     */
    public static PublicKey publicKeyFromBase64(String base64) throws Exception {
        if (base64 == null || base64.trim().isEmpty()) {
            throw new IllegalArgumentException("Stringu i çelësit publik Base64 nuk mund të jetë null ose bosh");
        }
        
        try {
            byte[] decoded = Base64.getDecoder().decode(base64);
            if (decoded.length == 0) {
                throw new IllegalArgumentException("Çelësi publik i dekoduar është bosh");
            }
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("Ed25519", "BC");
            return keyFactory.generatePublic(spec);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Kodimi Base64 i pavlefshëm për çelësin publik: " + e.getMessage(), e);
        }
    }
}
