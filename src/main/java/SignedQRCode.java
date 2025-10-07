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
        // Use Gson to produce a correct JSON representation
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    /**
     * Verifies the signature using the embedded public key. Returns true if valid.
     */
    public boolean verify() throws Exception {
        byte[] signatureBytes = Base64.getDecoder().decode(this.signature);
        PublicKey pub = EdDSAUtil.publicKeyFromBase64(this.publicKey);
        return EdDSAUtil.verify(this.data, signatureBytes, pub);
    }

    /**
     * Parse a SignedQRCode from a JSON string produced by {@link #toJSON()}.
     */
    public static SignedQRCode fromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SignedQRCode.class);
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
