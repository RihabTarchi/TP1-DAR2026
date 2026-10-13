package clientPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/** Client TCP : choisit un service, transmet x et affiche la réponse du serveur. */
public class Client1_3 {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int PORT = 5000;
    private static final int TIMEOUT_MS = 5000;

    public static void main(String[] args) {
        // En réseau local, fournir ici l'IPv4 de la machine qui héberge le serveur.
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;

        try {
            InetAddress serverIp = InetAddress.getByName(host);
            InetSocketAddress endpoint = new InetSocketAddress(serverIp, PORT);

            try (Socket socket = new Socket()) {
                socket.connect(endpoint, TIMEOUT_MS);
                System.out.println("Connecté à " + endpoint);

                try (DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                     Scanner keyboard = new Scanner(System.in)) {

                    boolean running = true;
                    while (running) {
                        printMenu();
                        int operation = readInt(keyboard, "Votre choix : ");

                        if (operation == 0) {
                            output.writeInt(0); // marqueur d'arrêt convenu avec le serveur
                            output.flush();
                            System.out.println("Fin demandée. Fermeture de la connexion.");
                            running = false;
                        } else if (operation < 1 || operation > 4) {
                            System.out.println("Choix invalide : sélectionnez 1, 2, 3, 4 ou 0.");
                        } else {
                            int x = readInt(keyboard, "Saisissez l'entier x : ");
                            output.writeInt(operation);
                            output.writeInt(x);
                            output.flush();

                            boolean success = input.readBoolean();
                            if (success) {
                                double result = input.readDouble();
                                System.out.println(formatOperation(operation, x, result));
                            } else {
                                System.out.println("Erreur renvoyée par le serveur : "
                                        + input.readUTF());
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Connexion impossible vers " + host + ":" + PORT + ".");
            System.err.println("Vérifiez que le serveur est lancé, l'adresse IP correcte,"
                    + " et que le port est autorisé par le pare-feu.");
            System.err.println("Détail : " + e.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("\n=== CALCULATRICE (opérations avec la constante 5) ===");
        System.out.println("1. Addition       : x + 5");
        System.out.println("2. Soustraction   : x - 5");
        System.out.println("3. Multiplication : x * 5");
        System.out.println("4. Division       : x / 5");
        System.out.println("0. Quitter");
    }

    private static int readInt(Scanner keyboard, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (keyboard.hasNextInt()) {
                return keyboard.nextInt();
            }
            if (!keyboard.hasNext()) {
                return 0;
            }
            System.out.println("Entrée invalide : veuillez saisir un entier.");
            keyboard.next();
        }
    }

    private static String formatOperation(int operation, int x, double result) {
        String symbol;
        switch (operation) {
            case 1: symbol = "+"; break;
            case 2: symbol = "-"; break;
            case 3: symbol = "×"; break;
            case 4: symbol = "÷"; break;
            default: symbol = "?";
        }
        return x + " " + symbol + " 5 = " + result;
    }
}

