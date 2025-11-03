package com.infosec.qreddsa;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Klasa utility për shfaqjen e imazheve të QR kodeve në një dritare grafike duke përdorur Swing
 */
public class QRCodeViewer {

    /**
     * Shfaq imazhin e QR kodit në një dritare grafike
     */
    public static void displayQRCode(String imagePath, String title) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Rruga e imazhit nuk mund të jetë null ose bosh");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Titulli i dritares nuk mund të jetë null ose bosh");
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                File imageFile = new File(imagePath);
                if (!imageFile.exists()) {
                    System.err.println("Gabim: Skedari i imazhit të QR kodit nuk u gjet: " + imagePath);
                    return;
                }
                
                BufferedImage image = ImageIO.read(imageFile);
                if (image == null) {
                    System.err.println("Gabim: Dështoi leximi i skedarit të imazhit: " + imagePath);
                    return;
                }
                
                JFrame frame = new JFrame(title);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                ImageIcon icon = new ImageIcon(image);
                JLabel label = new JLabel(icon);
                
                frame.getContentPane().add(label, BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
            } catch (Exception e) {
                System.err.println("Gabim në shfaqjen e QR kodit: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
