package com.infosec.qreddsa;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Utility class for displaying QR code images in a GUI window using Swing
 */
public class QRCodeViewer {
    
    /**
     * Displays the QR code image in a GUI window
     * 
     * @param imagePath Path to the QR code image file (must not be null or empty)
     * @param title Title for the window (must not be null or empty)
     * @throws IllegalArgumentException if imagePath or title is null or empty
     */
    public static void displayQRCode(String imagePath, String title) {
        // Input validation
        if (imagePath == null || imagePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Image path cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Window title cannot be null or empty");
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Check if file exists
                File imageFile = new File(imagePath);
                if (!imageFile.exists()) {
                    System.err.println("Error: QR code image file not found: " + imagePath);
                    return;
                }
                
                // Read the image
                BufferedImage image = ImageIO.read(imageFile);
                if (image == null) {
                    System.err.println("Error: Failed to read image file: " + imagePath);
                    return;
                }
                
                // Create frame
                JFrame frame = new JFrame(title);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                // Create image label
                ImageIcon icon = new ImageIcon(image);
                JLabel label = new JLabel(icon);
                
                // Add to frame
                frame.getContentPane().add(label, BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null); // Center on screen
                frame.setVisible(true);
                
            } catch (Exception e) {
                System.err.println("Error displaying QR code: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Displays the QR code with a default title
     */
    public static void displayQRCode(String imagePath) {
        displayQRCode(imagePath, "Signed QR Code");
    }
}
