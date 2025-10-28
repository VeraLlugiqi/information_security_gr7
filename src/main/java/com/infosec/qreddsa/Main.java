package com.infosec.qreddsa;

import java.security.KeyPair;
import java.util.Base64;

/**
 * Demo application for generating QR codes with EdDSA digital signatures
 */
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== QR Code with EdDSA Signature Demo ===");
            System.out.println();

            // Step 1: Generate EdDSA key pair
            System.out.println("1. Generating EdDSA (Ed25519) key pair...");
            KeyPair keyPair = EdDSAUtil.generateKeyPair();
            System.out.println("   ✓ Key pair generated successfully");
            System.out.println("   Public Key: " + EdDSAUtil.publicKeyToBase64(keyPair.getPublic()).substring(0, 40) + "...");
            System.out.println();

            // Step 2: Create data to sign
            String originalData = "Secure message: Hello from Information Security Group 7!";
            System.out.println("2. Original data to sign:");
            System.out.println("   \"" + originalData + "\"");
            System.out.println();

            // Step 3: Create signed QR code
            System.out.println("3. Creating digitally signed QR code...");
            SignedQRCode signedQRCode = SignedQRCode.create(originalData, keyPair);
            System.out.println("   Data signed with EdDSA");
            System.out.println("   Signature: " + signedQRCode.getSignature().substring(0, 40) + "...");
            System.out.println();

            // Step 4: Generate QR code image
            String qrCodeFile = "signed_qr_code.png";
            System.out.println("4. Generating QR code image...");
            signedQRCode.generateQRCode(qrCodeFile);
            System.out.println("   QR code saved to: " + qrCodeFile);
            System.out.println();
            
            // Display QR code in GUI
            System.out.println("   Opening QR code viewer...");
            QRCodeViewer.displayQRCode(qrCodeFile, "EdDSA Signed QR Code");
            System.out.println();

            // Step 5: Verify the signature (demonstration)
            System.out.println("5. Verifying signature...");
            byte[] signatureBytes = Base64.getDecoder().decode(signedQRCode.getSignature());
            boolean isValid = EdDSAUtil.verify(originalData, signatureBytes, keyPair.getPublic());
            System.out.println("   Signature valid: " + (isValid ? "✓ YES" : "✗ NO"));
            System.out.println();

            // Display JSON content
            System.out.println("6. QR Code content (JSON):");
            System.out.println("   " + signedQRCode.toJSON());
            System.out.println();

            // Step 7: Demonstrate QR code scanning and verification
            System.out.println("7. Scanning and verifying QR code from file...");
            try {
                SignedQRCode scannedQRCode = QRCodeScanner.scanSignedQRCode(qrCodeFile);
                System.out.println("   ✓ QR code scanned successfully");
                System.out.println("   Scanned data: \"" + scannedQRCode.getData() + "\"");
                
                boolean scannedValid = QRCodeScanner.verifyScannedQRCode(qrCodeFile);
                System.out.println("   Scanned signature valid: " + (scannedValid ? "✓ YES" : "✗ NO"));
            } catch (Exception e) {
                System.out.println("  Could not scan QR code: " + e.getMessage());
            }
            System.out.println();

            System.out.println("Scan the QR code to retrieve the signed data and verify its authenticity!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
}
