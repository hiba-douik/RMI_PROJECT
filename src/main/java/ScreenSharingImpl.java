import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ScreenSharingImpl extends UnicastRemoteObject implements ScreenSharingInterface {
    protected ScreenSharingImpl() throws RemoteException {
        super();
    }

    public byte[] requestScreenshot() throws RemoteException {
        try {
            Robot robot = new Robot();
            BufferedImage screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(screenshot, "jpg", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException | AWTException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
