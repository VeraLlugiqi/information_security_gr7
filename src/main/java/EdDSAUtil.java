import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class EdDSAUtil {
    
    static {
        // Add Bouncy Castle as a security provider
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Generates an EdDSA key pair (Ed25519)
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("Ed25519", "BC");
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Signs data using EdDSA with the provided private key
     * 
     * @param data The data to sign (must not be null)
     * @param privateKey The private key for signing (must not be null)
     * @return The signature bytes
     * @throws IllegalArgumentException if data or privateKey is null
     */
    public static byte[] sign(String data, PrivateKey privateKey) 
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {
        if (data == null) {
            throw new IllegalArgumentException("Data to sign cannot be null");
        }
        if (privateKey == null) {
            throw new IllegalArgumentException("Private key cannot be null");
        }
        
        Signature signature = Signature.getInstance("Ed25519", "BC");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

    /**
     * Verifies a signature using EdDSA with the provided public key
     * 
     * @param data The original data that was signed (must not be null)
     * @param signatureBytes The signature bytes to verify (must not be null)
     * @param publicKey The public key for verification (must not be null)
     * @return true if the signature is valid, false otherwise
     * @throws IllegalArgumentException if any parameter is null
     */
    public static boolean verify(String data, byte[] signatureBytes, PublicKey publicKey) 
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {
        if (data == null) {
            throw new IllegalArgumentException("Data to verify cannot be null");
        }
        if (signatureBytes == null || signatureBytes.length == 0) {
            throw new IllegalArgumentException("Signature bytes cannot be null or empty");
        }
        if (publicKey == null) {
            throw new IllegalArgumentException("Public key cannot be null");
        }
        
        Signature signature = Signature.getInstance("Ed25519", "BC");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(signatureBytes);
    }

    /**
     * Converts a public key to Base64 string for easy sharing
     */
    public static String publicKeyToBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * Converts a signature byte array to Base64 string
     */
    public static String signatureToBase64(byte[] signature) {
        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * Reconstructs a PublicKey instance from a Base64-encoded X.509 key
     * 
     * @param base64 Base64-encoded public key string (must not be null or empty)
     * @return The reconstructed PublicKey
     * @throws IllegalArgumentException if base64 is null, empty, or invalid
     */
    public static PublicKey publicKeyFromBase64(String base64) throws Exception {
        if (base64 == null || base64.trim().isEmpty()) {
            throw new IllegalArgumentException("Base64 public key string cannot be null or empty");
        }
        
        try {
            byte[] decoded = Base64.getDecoder().decode(base64);
            if (decoded.length == 0) {
                throw new IllegalArgumentException("Decoded public key is empty");
            }
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("Ed25519", "BC");
            return kf.generatePublic(spec);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 encoding for public key: " + e.getMessage(), e);
        }
    }
}
