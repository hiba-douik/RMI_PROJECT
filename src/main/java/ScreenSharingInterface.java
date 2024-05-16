import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScreenSharingInterface extends Remote {
    byte[] requestScreenshot() throws RemoteException;
    // Autres méthodes à définir si nécessaire
}
