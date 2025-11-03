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
 * Klasa utility për skanimin dhe leximin e QR kodeve nga skedarë imazhesh
 */
public class QRCodeScanner {
    
    /**
     * Skanon një QR kod nga një skedar imazhi dhe kthen përmbajtjen e dekoduar
     */
    public static String scanQRCode(String imagePath) throws IOException, NotFoundException {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Rruga e imazhit nuk mund të jetë null ose bosh");
        }
        
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new IOException("Skedari i imazhit të QR kodit nuk u gjet: " + imagePath);
        }
        
        if (!imageFile.canRead()) {
            throw new IOException("Nuk mund të lexohet skedari i imazhit të QR kodit: " + imagePath);
        }
        
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        if (bufferedImage == null) {
            throw new IOException("Dështoi leximi i skedarit të imazhit: " + imagePath);
        }
        
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        
        MultiFormatReader reader = new MultiFormatReader();
        Result result = reader.decode(bitmap);
        
        return result.getText();
    }
    
    /**
     * Skanon një QR kod dhe përpiqet ta parse si SignedQRCode
     */
    public static SignedQRCode scanSignedQRCode(String imagePath) 
            throws IOException, NotFoundException, IllegalArgumentException {
        String jsonContent = scanQRCode(imagePath);
        
        if (jsonContent == null || jsonContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Përmbajtja e QR kodit të skanuar është bosh");
        }
        
        try {
            return SignedQRCode.fromJSON(jsonContent);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Përmbajtja e QR kodit të skanuar nuk është në format të vlefshëm SignedQRCode: " + e.getMessage(), e);
        }
    }
    
    /**
     * Skanon një QR kod, e parse si SignedQRCode dhe verifikon nënshkrimin
     */
    public static boolean verifyScannedQRCode(String imagePath) 
            throws IOException, NotFoundException, Exception {
        SignedQRCode signedQRCode = scanSignedQRCode(imagePath);
        return signedQRCode.verify();
    }
}

