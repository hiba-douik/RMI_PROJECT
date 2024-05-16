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
            frame.setSize(800,600);
            frame.getContentPane().add(label);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            Robot robot = new Robot();
            while (true) {
                // Demander une capture d'écran au serveur
                byte[] imageBytes = server.requestScreenshot();

                // Convertir les bytes en une image
                BufferedImage screenshot = ImageIO.read(new ByteArrayInputStream(imageBytes));

                // Redimensionner l'image pour l'adapter à la taille de la fenêtre
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Image resizedImage = screenshot.getScaledInstance((int) screenSize.getWidth(), (int) screenSize.getHeight(), Image.SCALE_SMOOTH);

                // Afficher l'image dans la fenêtre
                ImageIcon icon = new ImageIcon(imageBytes);
                Image img = icon.getImage().getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));


                // Rafraîchir la fenêtre pour afficher l'image mise à jour
                frame.repaint();

                // Attendre un court instant avant de demander la prochaine capture d'écran
                Thread.sleep(1); // Définir la fréquence de mise à jour de l'écran
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
