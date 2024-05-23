import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
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

    public Point requestMousePosition() throws RemoteException {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        return pointerInfo.getLocation();
    }

    public void sendMouseClick(Point position) throws RemoteException {
        try {
            Robot robot = new Robot();
            robot.mouseMove(position.x, position.y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to send mouse click: " + e.getMessage());
        }
    }

    public void sendText(String text) throws RemoteException {
        try {
            Robot robot = new Robot();
            for (char c : text.toCharArray()) {
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            }
        } catch (AWTException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to send text: " + e.getMessage());
        }
    }
}
