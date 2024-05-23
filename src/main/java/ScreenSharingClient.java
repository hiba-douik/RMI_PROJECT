import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ScreenSharingClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ScreenSharingInterface server = (ScreenSharingInterface) registry.lookup("ScreenSharing");

            JFrame frame = new JFrame("Remote Screen");
            JLabel label = new JLabel();
            frame.setSize(800, 600);
            frame.getContentPane().add(label);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            // Add mouse listener
            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        // Request a screenshot to determine scale
                        byte[] imageBytes = server.requestScreenshot();
                        BufferedImage screenshot = ImageIO.read(new ByteArrayInputStream(imageBytes));

                        double scaleX = (double) screenshot.getWidth() / frame.getWidth();
                        double scaleY = (double) screenshot.getHeight() / frame.getHeight();
                        int mouseX = (int) (e.getX() * scaleX);
                        int mouseY = (int) (e.getY() * scaleY);

                        server.sendMouseClick(new Point(mouseX, mouseY));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Add key listener
            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    try {
                        server.sendText(Character.toString(e.getKeyChar()));
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            while (true) {
                // Request a screenshot from the server
                byte[] imageBytes = server.requestScreenshot();

                // Convert the bytes to an image
                BufferedImage screenshot = ImageIO.read(new ByteArrayInputStream(imageBytes));

                // Get the mouse position
                Point mousePosition = server.requestMousePosition();

                // Resize the image to fit the window
                Image resizedImage = screenshot.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);

                // Convert the resized image to a BufferedImage to draw on it
                BufferedImage resizedBufferedImage = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = resizedBufferedImage.getGraphics();
                g.drawImage(resizedImage, 0, 0, null);

                // Draw the mouse position
                double scaleX = (double) frame.getWidth() / screenshot.getWidth();
                double scaleY = (double) frame.getHeight() / screenshot.getHeight();
                int mouseX = (int) (mousePosition.x * scaleX);
                int mouseY = (int) (mousePosition.y * scaleY);

                g.setColor(Color.RED);
                g.fillOval(mouseX, mouseY, 10, 10);
                g.dispose();

                // Display the image in the window
                ImageIcon icon = new ImageIcon(resizedBufferedImage);
                label.setIcon(icon);

                // Refresh the window to display the updated image
                frame.repaint();

                // Wait a short time before requesting the next screenshot
                Thread.sleep(50); // Set the screen update frequency
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
