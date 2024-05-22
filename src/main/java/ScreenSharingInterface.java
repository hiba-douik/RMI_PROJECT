import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScreenSharingInterface extends Remote {
    byte[] requestScreenshot() throws RemoteException;
    Point requestMousePosition() throws RemoteException;
}
