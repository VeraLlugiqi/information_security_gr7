import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*;
import java.util.Base64;

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
     */
    public static byte[] sign(String data, PrivateKey privateKey) 
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {
        Signature signature = Signature.getInstance("Ed25519", "BC");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return signature.sign();
    }

    /**
     * Verifies a signature using EdDSA with the provided public key
     */
    public static boolean verify(String data, byte[] signatureBytes, PublicKey publicKey) 
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {
        Signature signature = Signature.getInstance("Ed25519", "BC");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
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
}
