package com.infosec.qreddsa;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;
import com.google.gson.Gson;

/**
 * Represents a digitally signed QR code that includes the original data,
 * the EdDSA signature, and the public key for verification
 */
public class SignedQRCode {
    private String data;
    private String signature;
    private String publicKey;

    public SignedQRCode(String data, String signature, String publicKey) {
        this.data = data;
        this.signature = signature;
        this.publicKey = publicKey;
    }

    /**
     * Creates a signed QR code by signing the data with the private key
     * 
     * @param data The data to sign (must not be null or empty)
     * @param keyPair The key pair for signing (must not be null)
     * @return A SignedQRCode object containing the signed data
     * @throws IllegalArgumentException if data or keyPair is null/invalid
     * @throws Exception if signing fails
     */
    public static SignedQRCode create(String data, KeyPair keyPair) throws Exception {
        // Input validation
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Data to sign cannot be null or empty");
        }
        if (data.length() > 2900) { // Leave room for JSON overhead (~50 chars)
            throw new IllegalArgumentException("Data is too long for QR code encoding (max ~2900 characters)");
        }
        if (keyPair == null) {
            throw new IllegalArgumentException("Key pair cannot be null");
        }
        if (keyPair.getPrivate() == null || keyPair.getPublic() == null) {
            throw new IllegalArgumentException("Key pair must have both private and public keys");
        }
        
        byte[] signatureBytes = EdDSAUtil.sign(data, keyPair.getPrivate());
        String signatureBase64 = EdDSAUtil.signatureToBase64(signatureBytes);
        String publicKeyBase64 = EdDSAUtil.publicKeyToBase64(keyPair.getPublic());
        
        return new SignedQRCode(data, signatureBase64, publicKeyBase64);
    }

    /**
     * Converts the signed QR code to a JSON string that can be encoded in a QR code
     */
    public String toJSON() {
        // Use Gson to produce a correct JSON representation
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    /**
     * Verifies the signature using the embedded public key
     * 
     * @return true if the signature is valid, false otherwise
     * @throws Exception if verification fails due to invalid keys or signature format
     */
    public boolean verify() throws Exception {
        byte[] signatureBytes = Base64.getDecoder().decode(this.signature);
        PublicKey publicKey = EdDSAUtil.publicKeyFromBase64(this.publicKey);
        return EdDSAUtil.verify(this.data, signatureBytes, publicKey);
    }

    /**
     * Parse a SignedQRCode from a JSON string produced by {@link #toJSON()}.
     * 
     * @param json The JSON string to parse
     * @return A SignedQRCode object
     * @throws IllegalArgumentException if json is null, empty, or invalid
     */
    public static SignedQRCode fromJSON(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON string cannot be null or empty");
        }
        
        try {
            Gson gson = new Gson();
            SignedQRCode result = gson.fromJson(json, SignedQRCode.class);
            
            // Validate parsed object
            if (result == null) {
                throw new IllegalArgumentException("Failed to parse JSON: result is null");
            }
            if (result.data == null || result.signature == null || result.publicKey == null) {
                throw new IllegalArgumentException("Invalid SignedQRCode: missing required fields (data, signature, or publicKey)");
            }
            if (result.data.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid SignedQRCode: data field is empty");
            }
            
            return result;
        } catch (com.google.gson.JsonSyntaxException e) {
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage(), e);
        }
    }

    /**
     * Generates a QR code image containing the signed data with default error correction
     */
    public void generateQRCode(String filePath) throws Exception {
        generateQRCode(filePath, QRCodeGenerator.ErrorCorrection.M);
    }
    
    /**
     * Generates a QR code image containing the signed data with specified error correction level
     * 
     * @param filePath The path where the QR code should be saved
     * @param errorCorrection The error correction level (L, M, Q, H)
     * @throws Exception if QR code generation fails
     */
    public void generateQRCode(String filePath, QRCodeGenerator.ErrorCorrection errorCorrection) throws Exception {
        QRCodeGenerator.generateQRCode(toJSON(), filePath, errorCorrection);
    }

    // Getters
    public String getData() {
        return data;
    }

    public String getSignature() {
        return signature;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
