package serverPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/** Serveur TCP : calcule x + 5, x - 5, x * 5 ou x / 5 selon le choix reçu. */
public class Server1_3 {
    private static final int PORT = 5000;
    private static final double CONSTANTE = 5.0;

    public static void main(String[] args) {
        // Pour viser une seule interface, donner l'IP LAN du serveur en argument.
        // Sans argument, 0.0.0.0 écoute sur toutes les interfaces réseau disponibles.
        String bindAddress = args.length > 0 ? args[0] : "0.0.0.0";

        try {
            InetAddress serverIp = InetAddress.getByName(bindAddress);
            InetSocketAddress endpoint = new InetSocketAddress(serverIp, PORT);

            try (ServerSocket serverSocket = new ServerSocket()) {
                serverSocket.bind(endpoint);
                System.out.println("Serveur à l'écoute sur " + endpoint);
                System.out.println("En attente d'un client...");

                try (Socket clientSocket = serverSocket.accept();
                     DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                     DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {

                    System.out.println("Client connecté depuis "
                            + clientSocket.getInetAddress().getHostAddress());

                    while (true) {
                        int operation = input.readInt();
                        if (operation == 0) {
                            System.out.println("Le client a demandé l'arrêt.");
                            break;
                        }

                        int x = input.readInt();
                        double result;
                        switch (operation) {
                            case 1:
                                result = x + CONSTANTE;
                                break;
                            case 2:
                                result = x - CONSTANTE;
                                break;
                            case 3:
                                result = x * CONSTANTE;
                                break;
                            case 4:
                                result = x / CONSTANTE;
                                break;
                            default:
                                output.writeBoolean(false);
                                output.writeUTF("Choix d'opération invalide : " + operation);
                                output.flush();
                                continue;
                        }

                        output.writeBoolean(true);
                        output.writeDouble(result);
                        output.flush();
                        System.out.println("Opération " + operation + " sur x=" + x
                                + " ; résultat=" + result);
                    }
                }
                System.out.println("Client déconnecté. Sockets fermés.");
            }
        } catch (EOFException e) {
            System.err.println("Le client a fermé la connexion sans envoyer le choix 0.");
        } catch (IOException e) {
            System.err.println("Erreur serveur/réseau : " + e.getMessage());
            System.err.println("Vérifiez l'adresse IP, le port et le pare-feu.");
        }
    }
}

