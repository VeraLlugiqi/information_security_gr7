package com.infosec.qreddsa;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for scanning and reading QR codes from image files
 */
public class QRCodeScanner {
    
    /**
     * Scans a QR code from an image file and returns the decoded text content
     * 
     * @param imagePath Path to the QR code image file
     * @return The decoded text content from the QR code
     * @throws IOException if the image file cannot be read
     * @throws NotFoundException if no QR code is found in the image
     */
    public static String scanQRCode(String imagePath) throws IOException, NotFoundException {
        // Input validation
        if (imagePath == null || imagePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Image path cannot be null or empty");
        }
        
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new IOException("QR code image file not found: " + imagePath);
        }
        
        if (!imageFile.canRead()) {
            throw new IOException("Cannot read QR code image file: " + imagePath);
        }
        
        // Read the image
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        if (bufferedImage == null) {
            throw new IOException("Failed to read image file: " + imagePath);
        }
        
        // Convert to binary bitmap for ZXing
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        
        // Decode the QR code
        MultiFormatReader reader = new MultiFormatReader();
        Result result = reader.decode(bitmap);
        
        return result.getText();
    }
    
    /**
     * Scans a QR code image and attempts to parse it as a SignedQRCode
     * 
     * @param imagePath Path to the QR code image file
     * @return A SignedQRCode object parsed from the scanned QR code
     * @throws IOException if the image file cannot be read
     * @throws NotFoundException if no QR code is found in the image
     * @throws IllegalArgumentException if the QR code content is not valid JSON or SignedQRCode format
     */
    public static SignedQRCode scanSignedQRCode(String imagePath) 
            throws IOException, NotFoundException, IllegalArgumentException {
        String jsonContent = scanQRCode(imagePath);
        
        if (jsonContent == null || jsonContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Scanned QR code content is empty");
        }
        
        try {
            return SignedQRCode.fromJSON(jsonContent);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Scanned QR code content is not a valid SignedQRCode format: " + e.getMessage(), e);
        }
    }
    
    /**
     * Scans a QR code image, parses it as a SignedQRCode, and verifies its signature
     * 
     * @param imagePath Path to the QR code image file
     * @return true if the QR code is valid and the signature is verified, false otherwise
     * @throws IOException if the image file cannot be read
     * @throws NotFoundException if no QR code is found in the image
     * @throws Exception if signature verification fails or other errors occur
     */
    public static boolean verifyScannedQRCode(String imagePath) 
            throws IOException, NotFoundException, Exception {
        SignedQRCode signedQRCode = scanSignedQRCode(imagePath);
        return signedQRCode.verify();
    }
}

