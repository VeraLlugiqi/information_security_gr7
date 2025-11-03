package com.infosec.qreddsa;

import java.security.KeyPair;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println();

            System.out.println("1. Gjenerimi i çiftit të çelësave EdDSA (Ed25519)...");
            KeyPair keyPair = EdDSAUtil.generateKeyPair();
            System.out.println("   Çifti i çelësave u gjenerua me sukses");
            System.out.println("   Çelësi Publik: " + EdDSAUtil.publicKeyToBase64(keyPair.getPublic()).substring(0, 40) + "...");
            System.out.println();

            String originalData = "Mesazh i sigurt: Përshëndetje nga Grupi 7 i Sigurisë së Informacionit!";
            System.out.println("2. Të dhënat origjinale për nënshkrim:");
            System.out.println("   \"" + originalData + "\"");
            System.out.println();

            System.out.println("3. Krijimi i QR kodit me nënshkrim digjital...");
            SignedQRCode signedQRCode = SignedQRCode.create(originalData, keyPair);
            System.out.println("   Të dhënat u nënshkruan me EdDSA");
            System.out.println("   Nënshkrimi: " + signedQRCode.getSignature().substring(0, 40) + "...");
            System.out.println();

            String qrCodeFile = "signed_qr_code.png";
            System.out.println("4. Gjenerimi i imazhit të QR kodit...");
            signedQRCode.generateQRCode(qrCodeFile);
            System.out.println("   QR kodi u ruajt në: " + qrCodeFile);
            System.out.println();
            
            System.out.println("   Hapja e shfaqësit të QR kodit...");
            QRCodeViewer.displayQRCode(qrCodeFile, "QR Kod me Nënshkrim EdDSA");
            System.out.println();

            System.out.println("5. Verifikimi i nënshkrimit...");
            byte[] signatureBytes = Base64.getDecoder().decode(signedQRCode.getSignature());
            boolean isValid = EdDSAUtil.verify(originalData, signatureBytes, keyPair.getPublic());
            System.out.println("   Nënshkrimi i vlefshëm: " + (isValid ? "✓ PO" : "✗ JO"));
            System.out.println();

            System.out.println("6. Përmbajtja e QR kodit (JSON):");
            System.out.println("   " + signedQRCode.toJSON());
            System.out.println();

            System.out.println("7. Skanimi dhe verifikimi i QR kodit nga skedari...");
            try {
                SignedQRCode scannedQRCode = QRCodeScanner.scanSignedQRCode(qrCodeFile);
                System.out.println("   QR kodi u skanua me sukses");
                System.out.println("   Të dhënat e skanuara: \"" + scannedQRCode.getData() + "\"");
                
                boolean scannedValid = QRCodeScanner.verifyScannedQRCode(qrCodeFile);
                System.out.println("   Nënshkrimi i skanuar i vlefshëm: " + (scannedValid ? "✓ PO" : "✗ JO"));
            } catch (Exception e) {
                System.out.println("   Nuk mund të skanohej QR kodi: " + e.getMessage());
            }
            System.out.println();

            System.out.println("Skanoni QR kodin për të marrë të dhënat e nënshkruara dhe verifikoni autenticitetin e tyre!");

        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
