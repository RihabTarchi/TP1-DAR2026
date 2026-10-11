package clientPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        System.out.println("Je suis connecté");
// 4. Envoyer la requête
        OutputStream os = socket.getOutputStream();
        os.write(10);
// 5. Attendre et lire la réponse
        InputStream is = socket.getInputStream();
        int resultat = is.read();
        System.out.println("Le triple de 10 est " + resultat);
// 6. Fermer la socket
        socket.close();

    }
}
