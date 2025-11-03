package com.infosec.qreddsa;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeGenerator {
    
    public static final int DEFAULT_WIDTH = 400;
    public static final int DEFAULT_HEIGHT = 400;
    public static final int MAX_TEXT_LENGTH = 2953;
    
    /**
     * Nivelet e korrigjimit të gabimeve për QR kodet:
     * L: ~7% korrigjim gabimesh
     * M: ~15% korrigjim gabimesh (parazgjedhje)
     * Q: ~25% korrigjim gabimesh
     * H: ~30% korrigjim gabimesh (besueshmëria më e lartë)
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
     * Gjeneron një imazh QR kod nga teksti i dhënë me nivel të specifikuar korrigjimi gabimesh
     */
    public static void generateQRCode(String text, int width, int height, String filePath, 
                                    ErrorCorrection errorCorrection) 
            throws WriterException, IOException {
        if (text == null) {
            throw new IllegalArgumentException("Teksti për kodim nuk mund të jetë null");
        }
        if (text.length() > MAX_TEXT_LENGTH) {
            throw new IllegalArgumentException("Teksti është shumë i gjatë për kodim në QR kod (maksimumi ~" + MAX_TEXT_LENGTH + " karaktere)");
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensionet e QR kodit duhet të jenë pozitive");
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Rruga e skedarit nuk mund të jetë null ose bosh");
        }
        if (errorCorrection == null) {
            errorCorrection = ErrorCorrection.M;
        }
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection.getLevel());
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    /**
     * Gjeneron një QR kod me dimensionet parazgjedhje (400x400) dhe nivel të specifikuar korrigjimi gabimesh
     */
    public static void generateQRCode(String text, String filePath, ErrorCorrection errorCorrection) 
            throws WriterException, IOException {
        generateQRCode(text, DEFAULT_WIDTH, DEFAULT_HEIGHT, filePath, errorCorrection);
    }
}
