package serverPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** Serveur TCP de calcul : renvoie au client x * 5 jusqu'à réception de 0. */
public class Server1_2 {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Serveur en attente d'un client sur le port " + PORT + "...");

        // Ce serveur traite un client, puis s'arrête après le marqueur 0.
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket clientSocket = serverSocket.accept();
             DataInputStream input = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {

            System.out.println("Client connecté : "
                    + clientSocket.getInetAddress().getHostAddress());

            while (true) {
                // readInt() lit l'entier envoyé par le client et attend s'il n'est pas encore reçu.
                int x = input.readInt();

                if (x == 0) {
                    System.out.println("Valeur 0 reçue : fin de la communication.");
                    break;
                }

                // Conversion en long avant la multiplication pour éviter le dépassement d'un int.
                long result = (long) x * 5L;
                System.out.println("Reçu : " + x + " ; résultat : " + result);

                output.writeLong(result);
                output.flush();
            }

            System.out.println("Connexion fermée. Arrêt du serveur.");
        } catch (EOFException e) {
            System.err.println("Le client a fermé la connexion sans envoyer 0.");
        } catch (IOException e) {
            System.err.println("Erreur du serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
