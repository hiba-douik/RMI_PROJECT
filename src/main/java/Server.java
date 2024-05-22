import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            // Créer une instance de l'implémentation du service
            ScreenSharingImpl service = new ScreenSharingImpl();

            // Créer un registre RMI sur le port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Lier l'instance du service au nom "ScreenSharing"
            registry.rebind("ScreenSharing", service);

            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
