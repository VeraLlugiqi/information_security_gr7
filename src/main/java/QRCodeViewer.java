import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class QRCodeViewer {
    
    /**
     * Displays the QR code image in a GUI window
     */
    public static void displayQRCode(String imagePath, String title) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Read the image
                BufferedImage image = ImageIO.read(new File(imagePath));
                
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
