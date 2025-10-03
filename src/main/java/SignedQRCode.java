import java.security.KeyPair;

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
     */
    public static SignedQRCode create(String data, KeyPair keyPair) throws Exception {
        byte[] signatureBytes = EdDSAUtil.sign(data, keyPair.getPrivate());
        String signatureBase64 = EdDSAUtil.signatureToBase64(signatureBytes);
        String publicKeyBase64 = EdDSAUtil.publicKeyToBase64(keyPair.getPublic());
        
        return new SignedQRCode(data, signatureBase64, publicKeyBase64);
    }

    /**
     * Converts the signed QR code to a JSON string that can be encoded in a QR code
     */
    public String toJSON() {
        return String.format("{\"data\":\"%s\",\"signature\":\"%s\",\"publicKey\":\"%s\"}", 
                            data, signature, publicKey);
    }

    /**
     * Generates a QR code image containing the signed data
     */
    public void generateQRCode(String filePath) throws Exception {
        QRCodeGenerator.generateQRCode(toJSON(), filePath);
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
