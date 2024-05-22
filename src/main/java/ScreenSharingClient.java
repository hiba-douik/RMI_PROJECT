import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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

            while (true) {
                // Demander une capture d'écran au serveur
                byte[] imageBytes = server.requestScreenshot();

                // Convertir les bytes en une image
                BufferedImage screenshot = ImageIO.read(new ByteArrayInputStream(imageBytes));

                // Obtenir la position de la souris
                Point mousePosition = server.requestMousePosition();

                // Redimensionner l'image pour l'adapter à la taille de la fenêtre
                Image resizedImage = screenshot.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);

                // Convertir l'image redimensionnée en BufferedImage pour dessiner dessus
                BufferedImage resizedBufferedImage = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = resizedBufferedImage.getGraphics();
                g.drawImage(resizedImage, 0, 0, null);

                // Dessiner la position de la souris redimensionnée
                double scaleX = (double) frame.getWidth() / screenshot.getWidth();
                double scaleY = (double) frame.getHeight() / screenshot.getHeight();
                int mouseX = (int) (mousePosition.x * scaleX);
                int mouseY = (int) (mousePosition.y * scaleY);

                g.setColor(Color.RED);
                g.fillOval(mouseX, mouseY, 10, 10);
                g.dispose();

                // Afficher l'image dans la fenêtre
                ImageIcon icon = new ImageIcon(resizedBufferedImage);
                label.setIcon(icon);

                // Rafraîchir la fenêtre pour afficher l'image mise à jour
                frame.repaint();

                // Attendre un court instant avant de demander la prochaine capture d'écran
                Thread.sleep(100); // Définir la fréquence de mise à jour de l'écran
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
