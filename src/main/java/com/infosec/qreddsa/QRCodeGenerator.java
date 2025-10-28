package com.infosec.qreddsa;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for generating QR codes with configurable error correction levels
 */
public class QRCodeGenerator {
    
    /** Default width for QR code images in pixels */
    public static final int DEFAULT_WIDTH = 400;
    
    /** Default height for QR code images in pixels */
    public static final int DEFAULT_HEIGHT = 400;
    
    /** Maximum text length that can be encoded in a QR code (approximate for version 40) */
    public static final int MAX_TEXT_LENGTH = 2953;
    
    /**
     * Error correction levels for QR codes
     * L: ~7% error correction
     * M: ~15% error correction (default)
     * Q: ~25% error correction
     * H: ~30% error correction (highest reliability)
     */
    public enum ErrorCorrection {
        L(ErrorCorrectionLevel.L),
        M(ErrorCorrectionLevel.M),
        Q(ErrorCorrectionLevel.Q),
        H(ErrorCorrectionLevel.H);
        
        private final ErrorCorrectionLevel level;
        
        ErrorCorrection(ErrorCorrectionLevel level) {
            this.level = level;
        }
        
        ErrorCorrectionLevel getLevel() {
            return level;
        }
    }

    /**
     * Generates a QR code image from the given text with specified error correction level
     * 
     * @param text The text to encode in the QR code
     * @param width The width of the QR code image
     * @param height The height of the QR code image
     * @param filePath The path where the QR code image should be saved
     * @param errorCorrection The error correction level (L, M, Q, H)
     * @throws IllegalArgumentException if text is null or too long, or dimensions are invalid
     */
    public static void generateQRCode(String text, int width, int height, String filePath, 
                                     ErrorCorrection errorCorrection) 
            throws WriterException, IOException {
        // Input validation
        if (text == null) {
            throw new IllegalArgumentException("Text to encode cannot be null");
        }
        if (text.length() > MAX_TEXT_LENGTH) {
            throw new IllegalArgumentException("Text is too long for QR code encoding (max ~" + MAX_TEXT_LENGTH + " characters)");
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("QR code dimensions must be positive");
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        if (errorCorrection == null) {
            errorCorrection = ErrorCorrection.M;
        }
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        // Set encoding hints including error correction level
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection.getLevel());
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    /**
     * Generates a QR code with default dimensions (400x400) and specified error correction
     */
    public static void generateQRCode(String text, String filePath, ErrorCorrection errorCorrection) 
            throws WriterException, IOException {
        generateQRCode(text, DEFAULT_WIDTH, DEFAULT_HEIGHT, filePath, errorCorrection);
    }
}
