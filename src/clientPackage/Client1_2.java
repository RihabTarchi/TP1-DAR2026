package clientPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/** Client TCP : envoie des entiers et affiche leur produit par cinq. */
public class Client1_2 {
    private static final String DEFAULT_HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;

        try (Socket socket = new Socket(host, PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             Scanner keyboard = new Scanner(System.in)) {

            System.out.println("Connecté au serveur " + host + " sur le port " + PORT + ".");
            System.out.println("Entrez un entier (0 pour terminer).");

            while (true) {
                System.out.print("x = ");

                if (!keyboard.hasNextInt()) {
                    if (!keyboard.hasNext()) {
                        break; // Fin de l'entrée clavier.
                    }
                    System.out.println("Entrée invalide : veuillez saisir un entier.");
                    keyboard.next(); // Élimine le token incorrect.
                    continue;
                }

                int x = keyboard.nextInt();
                output.writeInt(x);
                output.flush();

                if (x == 0) {
                    System.out.println("Arrêt demandé. Fermeture de la connexion.");
                    break; // Le serveur ne renvoie pas de résultat pour le marqueur 0.
                }

                long result = input.readLong();
                System.out.println(x + " × 5 = " + result);
            }
        } catch (IOException e) {
            System.err.println("Erreur de communication avec le serveur : " + e.getMessage());
            System.err.println("Vérifiez que le serveur est démarré et écoute sur le port " + PORT + ".");
        }
    }
}

