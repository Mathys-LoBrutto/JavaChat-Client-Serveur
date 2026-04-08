import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class MonServeurUDP {
    static void main(String[] args) {
        try {
            // Création du canal
            DatagramSocket socketServeur = new DatagramSocket();

            // Réservation du port
            InetSocketAddress adresse = new InetSocketAddress("localhost", 5555);
            socketServeur.bind(adresse);

            byte[] recues = new byte[1024];
            byte[] envoyees;

            // Réception du paquet
            DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
            socketServeur.receive(paquetRecu);
            String message = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
            System.out.println("Reçu: " + message);

            // Emission d'une réponse
            InetAddress adrClient = paquetRecu.getAddress();
            int prtClient = paquetRecu.getPort();
            String reponse = "Accusé de réception";
            envoyees = reponse.getBytes();
            DatagramPacket paquetEnvoye = new DatagramPacket(envoyees, envoyees.length, adrClient, prtClient);
            socketServeur.send(paquetEnvoye);

            // Fermeture du canal
            socketServeur.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
