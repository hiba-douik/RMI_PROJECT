import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScreenSharingInterface extends Remote {
    byte[] requestScreenshot() throws RemoteException;
    Point requestMousePosition() throws RemoteException;
    void sendMouseClick(Point position) throws RemoteException; // Add this method
    void sendText(String text) throws RemoteException; // Add this method
}
